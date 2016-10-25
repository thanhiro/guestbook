(ns guestbook-app.fetch-fx
  (:require
    [goog.net.ErrorCode :as errors]
    [re-frame.core :refer [reg-fx dispatch console]]
    [cljs.spec :as s]))

(defn ajax-xhrio-handler
  "ajax-request only provides a single handler for success and errors"
  [on-success on-failure xhrio [success? response]]
  ; see http://docs.closure-library.googlecode.com/git/class_goog_net_XhrIo.html
  (if success?
    (on-success response)
    (let [details
          (merge
            {:uri             (.getLastUri xhrio)
             :last-method     (.-lastMethod_ xhrio)
             :last-error      (.getLastError xhrio)
             :last-error-code (.getLastErrorCode xhrio)
             :debug-message   (-> xhrio .getLastErrorCode (errors/getDebugMessage))}
            response)]
      (on-failure details))))


(defn request->fetch-options
  [{:as   request
    :keys [uri method on-success on-failure]
    :or   {on-success [:http-no-on-success]
           on-failure [:http-no-on-failure]}}]
  (-> request
      (assoc
        :method (name method)
        :url uri)))

(s/def ::sequential-or-map (s/or :list-or-vector sequential? :map map?))

(fn fetch
  [opts]
  (.catch
    (.then
      (.then
        (js/fetch opts)
        #(js->clj (.json %1)))
      #(dispatch (conj (:on-success opts) %)))
    #(dispatch (conj (:on-failure opts) %))))

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
                       js/fetch)
      :else (console
              :error
              "fetch-fx: expected request to be a list
              or vector or map, but got: " request))))