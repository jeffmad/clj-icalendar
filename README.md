# clj-icalendar

A Clojure library that is a very thin wrapper around ical4j.

[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.vitallabs/clj-icalendar.svg)] (https://clojars.org/org.clojars.vitallabs/clj-icalendar)

This is a fork with some enhancements from https://clojars.org/clj-icalendar

## Usage

To create an empty calendar, call
```
(create-cal "Acme, Inc." "Acme Calendar" "V0.1" "EN")
```
which will return an object that, when converted to a string, produces a valid iCalendar with no events.
```
BEGIN:VCALENDAR
PRODID:-//Acme\\, Inc. //Acme Calendar V0.1//EN
VERSION:2.0
METHOD:PUBLISH
CALSCALE:GREGORIAN
END:VCALENDAR
```
To create an event with a duration, call `create-event` with start date, end date, title, and keyword args for unique-id, description, url, location, and organizer url. Start date and end date are of type `java.util.Date`.  Below, `->date` is a utility method that converts an ISO8601 formatted date string to a `java.util.Date`.
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
To create an all day event, call `create-all-day-event`.  The time portion of the start date argument will be truncated. 
```
(create-all-day-event  (->date "2015-11-16T09:16:00-07:00") 
                       "Attend Clojure Conj" 
                       :unique-id "1234" 
                       :description "Clojure/conj is the original conference for Clojure and its community." 
                       :url "http://clojure-conj.org/" 
                       :location "Philadelphia, Pennsylvania USA" 
                       :organizer "http://cognitect.com/")
```
To add an event to a calendar, call `add-event!`, passing in the calendar and the event. 
```
(add-event! (create-cal "Acme, Inc." "Acme Calendar" "V0.1" "EN") event)
```
Finally, to print the output, call `output-calendar`, passing in the calendar, which will give an iCalendar compliant String. 
```
(let [cal  (ical/create-cal "Acme, Inc." "Acme Calendar" "V0.1" "EN")
      ical-events (map #(ical/create-event %) events))
      _ (reduce (fn [cal event] (ical/add-event! cal event)) cal ical-events)]
    (ical/output-calendar cal))
```
The output should look similar to this
```
BEGIN:VCALENDAR
PRODID:-//Acme\\, Inc. //Acme Calendar V0.1//EN
VERSION:2.0
METHOD:PUBLISH
CALSCALE:GREGORIAN
BEGIN:VEVENT
DTSTAMP:20151025T122307Z
DTSTART:20151225T161600Z
DTEND:20151231T183700Z
SUMMARY:Spain Vacation
UID:1234
ORGANIZER:http://www.example.com
URL:http://www.example.com/1234
LOCATION:Barcelona, Spain
DESCRIPTION:Spend time at vacation home.
END:VEVENT
END:VCALENDAR
```
## License

Copyright © 2015

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
