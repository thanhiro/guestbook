(ns backend.routes.api
  (:require [backend.layout :as layout]
            [backend.db.core :as db]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :refer :all]
            [clojure.java.io :as io]))

(defn visitors []
  (ok (db/get-visitors)))

(defroutes api-routes
  (GET "/visitors/today" []
    (visitors))
  (GET "/visitors" []
    (visitors))
  (POST "/visitors" []
    (visitors)))

