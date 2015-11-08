# clj-icalendar

A Clojure library that is a thin wrapper around ical4j.
[![Clojars Project](http://clojars.org/org.clojars.jeffmad/clj-icalendar/latest-version.svg)](http://clojars.org/org.clojars.jeffmad/clj-icalendar)

## Usage

To create an empty calendar
```
(create-cal "Acme, Inc." "Acme Calendar" "V0.1" "EN")
```
which will return an object that, when converted to a string produce a calendar with no events
```
BEGIN:VCALENDAR
PRODID:-//Acme\\, Inc. //Acme Calendar V0.1//EN
VERSION:2.0
METHOD:PUBLISH
CALSCALE:GREGORIAN
END:VCALENDAR
```
To create an event with a duration, call `create-event` with start date, end date, title, and keyword args for unique-id, description, url, location, and organizer url. Start date and end date are of type `java.util.Date`.  Below `->date` is a utility method that converts an ISO8601 formatted date to a `java.util.Date`.
```
(create-event  (->date "2015-12-25T09:16:00-07:00") 
               (->date "2015-12-31T11:37:00-07:00") 
               "Family Vacation" 
               :unique-id "1234" 
               :description "Have fun." 
               :url "http://www.example.com/1234" 
               :location "1313 Mockingbird Lane" 
               :organizer "http://www.example.com")
```
One can also create an event that has a start time, but no duration. For this, call the function `create-event-no-duration`. 
```
(create-event-no-duration  (->date "2015-12-25T09:16:00-07:00") 
                           "Let the dog out" 
                           :unique-id "1234" 
                           :description "Dog must go out" 
                           :url "http://www.example.com/1234" 
                           :location "1313 Mockingbird Lane" 
                           :organizer "http://www.example.com")
```
To create an all day event, call `create-all-day-event`.  The time portion of the start arg will be truncated. 
```
(create-all-day-event  (->date "2015-11-16T09:16:00-07:00") 
                       "Attend Clojure Conj" 
                       :unique-id "1234" 
                       :description "Clojure/conj is the original conference for Clojure and its community." 
                       :url "http://clojure-conj.org/" 
                       :location "Philadelphia, Pennsylvania USA" 
                       :organizer "http://cognitect.com/")
```
To add an event to a calendar, call `add-event!`
```
(add-event! (create-cal "Acme, Inc." "Acme Calendar" "V0.1" "EN") event)
```
To print the output, call `output-calendar`, passing in the calendar, which will give an iCalendar compliant String. 
```
(let [cal  (ical/create-cal "Acme, Inc." "Acme Calendar" "V0.1" "EN")
      ical-events (map #(ical/create-event %) events))
      _ (reduce (fn [cal event] (ical/add-event! cal event)) cal ical-events)]
    (ical/output-calendar cal))
```

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
