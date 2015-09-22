(ns clj-icalendar.core-test
  (:require [clojure.test :refer :all]
            [clj-icalendar.core :refer :all]))

(deftest a-test
  (testing "Placeholder."
    (is (= 1 1))))

(deftest create-empty-cal
  (testing "basic calendar tags"
    (let [cal "BEGIN:VCALENDAR\r\nPRODID:-//Acme\\, Inc. //Acme Calendar V0.1//EN\r\nVERSION:2.0\r\nMETHOD:PUBLISH\r\nCALSCALE:GREGORIAN\r\nEND:VCALENDAR\r\n"]
      (is (= cal (.toString (create-cal "Acme, Inc." "Acme Calendar" "V0.1" "EN")))))))

(deftest create-basic-event
  (testing "basic event with start and end"
    (let [event #"BEGIN:VEVENT\r\nDTSTAMP:(\w+)\r\nDTSTART:20151225T000000\r\nDTEND:20151231T000000\r\nSUMMARY:Homer Vacation\r\nUID:(.*)\r\nDESCRIPTION:During the last week of the year\\, it is traditional to go on vacation for Homer and his family. This year they will be going to Santa Fe\\, New Mexico.\r\nEND:VEVENT\r\n"]
      (is (re-matches event (.toString (create-event "25-DEC-2015" "31-DEC-2015" "Homer Vacation" "During the last week of the year, it is traditional to go on vacation for Homer and his family. This year they will be going to Santa Fe, New Mexico.")))))))
