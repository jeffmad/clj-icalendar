(ns clj-icalendar.core-test
  (:require [clojure.test :refer :all]
            [clj-icalendar.core :refer :all]))

(deftest a-test
  (testing "Placeholder."
    (is (= 1 1))))

(deftest create-empty-cal-test
  (testing "basic calendar tags"
    (let [cal "BEGIN:VCALENDAR\r\nPRODID:-//Acme\\, Inc. //Acme Calendar V0.1//EN\r\nVERSION:2.0\r\nMETHOD:PUBLISH\r\nCALSCALE:GREGORIAN\r\nEND:VCALENDAR\r\n"]
      (is (= cal (.toString (create-cal "Acme, Inc." "Acme Calendar" "V0.1" "EN")))))))

(defn- ->date
  "take in a date that looks like 2015-03-31T23:59:50-07:00
  and convert it into a java.util.Date"
  [^String s] (-> (java.time.ZonedDateTime/parse s java.time.format.DateTimeFormatter/ISO_OFFSET_DATE_TIME)
                             java.time.Instant/from
                             java.util.Date/from))

(def a-vacation (create-event  (->date "2015-12-25T09:16:00-07:00") (->date "2015-12-31T11:37:00-07:00") "Homer Vacation" :unique-id "1234" :description "During the last week of the year, it is traditional to go on vacation for Homer and his family. This year they will be going to Santa Fe, New Mexico." :url "http://www.example.com/1234" :location "1313 Mockingbird Lane"))

(def an-all-day-event (create-all-day-event  (->date "2015-12-25T09:16:00-07:00")  "Homer Vacation" :unique-id "1234" :description "During the last week of the year, it is traditional to go on vacation for Homer and his family. This year they will be going to Santa Fe, New Mexico." :url "http://www.example.com/1234" :location "1313 Mockingbird Lane"))

(def an-event-with-no-duration (create-event-no-duration  (->date "2015-12-25T09:16:00-07:00")  "Christmas party" :unique-id "1234" :description "This is the description" :url "http://www.example.com/1234" :location "this is the location"))

(deftest create-all-day-event-test
  (testing "an all day event has no endtime and the time portion of the start is truncated"
    (let [event #"BEGIN:VEVENT\r\nDTSTAMP:(\w+)\r\nDTSTART:20151225T000000Z\r\nSUMMARY:Homer Vacation\r\nUID:1234\r\nURL:http://www.example.com/1234\r\nLOCATION:1313 Mockingbird Lane\r\nDESCRIPTION:During the last week of the year\\, it is traditional to go on vacation for Homer and his family. This year they will be going to Santa Fe\\, New Mexico.\r\nEND:VEVENT\r\n"]
      (is (re-matches event (.toString an-all-day-event))))))

(deftest create-event-with-no-duration-test
  (testing "an event with no duration has no endtime but the time portion
            of the start is used to mark the beginning of the event"
    (let [event #"BEGIN:VEVENT\r\nDTSTAMP:(\w+)\r\nDTSTART:20151225T161600Z\r\nSUMMARY:Christmas party\r\nUID:1234\r\nURL:http://www.example.com/1234\r\nLOCATION:this is the location\r\nDESCRIPTION:This is the description\r\nEND:VEVENT\r\n"]
      (is (re-matches event (.toString an-event-with-no-duration))))))

(deftest create-basic-event-test
  (testing "basic event with start and end"
    (let [event #"BEGIN:VEVENT\r\nDTSTAMP:(\w+)\r\nDTSTART:20151225T161600Z\r\nDTEND:20151231T183700Z\r\nSUMMARY:Homer Vacation\r\nUID:1234\r\nURL:http://www.example.com/1234\r\nLOCATION:1313 Mockingbird Lane\r\nDESCRIPTION:During the last week of the year\\, it is traditional to go on vacation for Homer and his family. This year they will be going to Santa Fe\\, New Mexico.\r\nEND:VEVENT\r\n"]
      (is (re-matches event (.toString a-vacation))))))

(deftest add-event-to-cal-test
  (testing "add a basic event to cal"
    (let [event a-vacation
          exp #"BEGIN:VCALENDAR\r\nPRODID:-//Acme\\, Inc. //Acme Calendar V0.1//EN\r\nVERSION:2.0\r\nMETHOD:PUBLISH\r\nCALSCALE:GREGORIAN\r\nBEGIN:VEVENT\r\nDTSTAMP:(\w+)\r\nDTSTART:20151225T161600Z\r\nDTEND:20151231T183700Z\r\nSUMMARY:Homer Vacation\r\nUID:1234\r\nURL:http://www.example.com/1234\r\nLOCATION:1313 Mockingbird Lane\r\nDESCRIPTION:During the last week of the year\\, it is traditional to go on vacation for Homer and his family. This year they will be going to Santa Fe\\, New Mexico.\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"]
      (is (re-matches exp  (.toString (add-event! (create-cal "Acme, Inc." "Acme Calendar" "V0.1" "EN") event)))))))
