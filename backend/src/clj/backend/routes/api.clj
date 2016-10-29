(ns backend.routes.api
  (:require [backend.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn visitors []
  (layout/render "home.html"))

(defroutes api-routes
  (GET "/visitors/today" []
    (visitors))
  (GET "/visitors" []
    (visitors))
  (POST "/visitors" []
    (visitors)))

