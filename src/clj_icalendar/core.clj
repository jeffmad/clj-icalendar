(ns clj-icalendar.core
  (:import (net.fortuna.ical4j.model Calendar DateTime Dur)
           (net.fortuna.ical4j.model.component VEvent)
           (net.fortuna.ical4j.model.property CalScale ProdId Uid Version XProperty Duration Description Method)
           (net.fortuna.ical4j.util UidGenerator)
           (java.util Date)))


(defn create-cal [^String org-name ^String product ^String version ^String lang]
  (let [c (Calendar.)
        props (.getProperties c)]
    (.add props (ProdId. (str "-//" org-name " //" product " " version "//" lang)))
    (.add props Version/VERSION_2_0)
    (.add props Method/PUBLISH)
    #_(.add props (XProperty. "X-PUBLISHED-TTL" (.getValue (Duration. (Dur. 1 0 0 0)))))
    (.add props CalScale/GREGORIAN) c))

(defn create-event [^String start ^String end ^String title ^String description]
   (let [sd (Date. start)
         ed (Date. end)
         st (DateTime. (.getTime sd))
         et (DateTime. (.getTime ed))
         vevent (VEvent. st et title)
         ug     (UidGenerator. "uidGen")
         uid    (.generateUid ug)
         props      (.getProperties vevent)]
         (.add props uid)
         (.add props (Description. description))
         vevent))

(defn add-event [^net.fortuna.ical4j.model.Calendar cal  ^VEvent vevent]
  (.add (.getComponents cal) vevent))
