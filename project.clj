(defproject org.clojars.jeffmad/clj-icalendar "0.1.2"
  :description "wrapper over ical4j to generate ics"
  :url "http://github.com/jeffmad/clj-icalendar"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :scm {:name "git"
        :url "https://github.com/jeffmad/clj-icalendar"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.mnode.ical4j/ical4j "1.0.6"]]
  :global-vars {*warn-on-reflection* false
                *assert* false})
