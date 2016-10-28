(ns backend.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [backend.core-test]))

(doo-tests 'backend.core-test)

