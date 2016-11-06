(ns guestbook-app.fetch-fx
  (:require
    [re-frame.core :refer [reg-fx dispatch console]]
    [cljs.spec :as s]))

(defn request->fetch-options
  [{:as   request
    :keys [method on-success on-failure]
    :or   {on-success [:http-no-on-success]
           on-failure [:http-no-on-failure]}}]
  (-> request
      (assoc
        :method (name method))))

(s/def ::sequential-or-map (s/or :list-or-vector sequential? :map map?))

(defn fetch
  [opts]
  (-> (js/fetch (:uri opts) (clj->js opts))
      ;;(.then #(if (.ok %)) % (do (println %) (-.reject js/Promise %)))
      (.then #(js->clj (.json %)))
      (.then #(dispatch (conj (:on-success opts) %)))
      (.catch #(dispatch (conj (:on-failure opts) %)))))

(reg-fx
  :http-fetch
  (fn http-effect [request]
    (when (= :cljs.spec/invalid (s/conform ::sequential-or-map request))
      (console :error (s/explain-str ::sequential-or-map request)))
    (cond
      (sequential? request) (doseq [each request] (http-effect each))
      (map? request) (->
                       request
                       request->fetch-options
                       fetch)
      :else (console
              :error
              "fetch-fx: expected request to be a list
              or vector or map, but got: " request))))