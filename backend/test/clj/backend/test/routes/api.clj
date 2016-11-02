(ns backend.test.routes.api
    (:require [clojure.test :refer :all]
      [ring.mock.request :refer :all]
      [backend.handler :refer :all]))

(deftest test-app
  (testing "visitors route"
    (let [response ((app) (request :get "/api/visitors"))]
         (is (= 200 (:status response))))))
