(ns clj-icalendar.core
  (:import (net.fortuna.ical4j.model Calendar DateTime Dur)
           (net.fortuna.ical4j.model.component VEvent)
           (net.fortuna.ical4j.model.property CalScale ProdId Uid Version XProperty Duration Description Method Url Location Organizer Name)
           (net.fortuna.ical4j.data CalendarOutputter)
           (java.io StringWriter)
           (java.util Date TimeZone)))

(defn create-cal
  "create an empty calendar container. it is assumed to be
   Gregorian ical 2.0 and a published calendar "
  [^String org-name ^String product ^String version ^String lang & rest]
  (let [c (Calendar.)
        props (.getProperties c)
        opts (apply hash-map rest)
        name (:name opts)
        ttl  (:ttl opts)]
    (.add props (ProdId. (str "-//" org-name " //" product " " version "//" lang)))
    (.add props Version/VERSION_2_0)
    (.add props Method/PUBLISH)
    (.add props CalScale/GREGORIAN)
    (if ttl (.add props (XProperty. "X-PUBLISHED-TTL" ttl)))
    (if name (do  (.add props (Name. name))
                  (.add props (XProperty. "X-WR-CALNAME" name))))
    c))

(defn- add-properties
  "take a vevent and add properties to it.
  the supported properties are url unique-id description and location.
  If no unique-id is supplied UUID will be generated"
  [vevent {:keys [^String unique-id ^String description ^String url ^String location ^String organizer]
           :or {unique-id (str (java.util.UUID/randomUUID))}}]
  (let [u (if (seq unique-id) unique-id (str (java.util.UUID/randomUUID)))
        props (.getProperties vevent)]
    (.add props (Uid. u))
    (.add props (Organizer. organizer))
    (when (seq url) (.add props (Url. (java.net.URI. url))))
    (when (seq location) (.add props (Location. location)))
    (when (seq description) (.add props (Description. description)))
    vevent))

(defn- truncate-time
  "function to take a java.util.Date object and return a date
   with the time portion truncated."
  [^Date d]
  (-> d
      .toInstant
      (.truncatedTo java.time.temporal.ChronoUnit/DAYS)
      Date/from))

(defn create-all-day-event
  "create a vevent with start date and title.
   the time portion of the start date will be truncated.
   Optionally, one can pass in keyword args for unique-id,
   description, url, and location. vevent is returned "
  [^Date start ^String title & {:keys [^String unique-id ^String description ^String url ^String location ^String organizer] :as all}]
  (let [trunc (truncate-time start)
        st (doto (DateTime. trunc) (.setUtc true))
        vevent (VEvent. st title)]
    (add-properties vevent all)))

(defn create-event-no-duration
  "create and return a vevent based on input params.
   The start date is a date with time, and since there
   is no end date specified, this event blocks no time on the calendar.
   Optional keyword parameters are unique-id, description, url, and location"
  [^Date start ^String title & {:keys [^String unique-id ^String description ^String url ^String location ^String organizer] :as all}]
  (let [st (doto  (DateTime. start) (.setUtc true))
        vevent (VEvent. st title)]
    (add-properties vevent all)))

(defn create-event [^Date start ^Date end ^String title & {:keys [^String unique-id ^String description ^String url ^String location ^String organizer] :as all}]
  (let [st (doto  (DateTime. start) (.setUtc true))
        et (doto  (DateTime. end) (.setUtc true))
        vevent (VEvent. st et title)]
    (add-properties vevent all)))

(defn add-event!
  "take a calendar and a vevent, add the event to the calendar, and return the calendar"
  [^net.fortuna.ical4j.model.Calendar cal  ^VEvent vevent]
  (.add (.getComponents cal) vevent) cal)

(defn output-calendar
  "output the calendar to a string, using a folding writer,
   which will limit the line lengths as per ical spec."
  [^net.fortuna.ical4j.model.Calendar cal]
  (let [co (CalendarOutputter.)
        sw (StringWriter.)
        output (.output co cal sw)
        _ (.close sw)]
    (.replaceAll (.toString sw) "\r" "")))
