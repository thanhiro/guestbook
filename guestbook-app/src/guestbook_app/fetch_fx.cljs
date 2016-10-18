(ns guestbook-app.fetch-fx
  (:require
    [goog.net.ErrorCode :as errors]
    [re-frame.core      :refer [reg-fx dispatch console]]
    [cljs.spec          :as s]))

(defn ajax-xhrio-handler
  "ajax-request only provides a single handler for success and errors"
  [on-success on-failure xhrio [success? response]]
  ; see http://docs.closure-library.googlecode.com/git/class_goog_net_XhrIo.html
  (if success?
    (on-success response)
    (let [details (merge
                    {:uri             (.getLastUri xhrio)
                     :last-method     (.-lastMethod_ xhrio)
                     :last-error      (.getLastError xhrio)
                     :last-error-code (.getLastErrorCode xhrio)
                     :debug-message   (-> xhrio .getLastErrorCode (errors/getDebugMessage))}
                    response)]
      (on-failure details))))


(defn request->xhrio-options
  [{:as   request
    :keys [on-success on-failure]
    :or   {on-success      [:http-no-on-success]
           on-failure      [:http-no-on-failure]}}]
  ; wrap events in cljs-ajax callback
  (let [api (new js/goog.net.XhrIo)]
    (-> request
        (assoc
          :api     api
          :handler (partial ajax-xhrio-handler
                            #(dispatch (conj on-success %))
                            #(dispatch (conj on-failure %))
                            api))
        (dissoc :on-success :on-failure))))

(s/def ::sequential-or-map (s/or :list-or-vector sequential? :map map?))

(reg-fx
  :http-xhrio
  (fn http-effect [request]
    (when (= :cljs.spec/invalid (s/conform ::sequential-or-map request))
      (console :error (s/explain-str ::sequential-or-map request)))
    ;;TODO verify detail request(s) using spec for ajax-request api
    (cond
      (sequential? request) (doseq [each request] (http-effect each))
      (map? request)        (-> request request->xhrio-options ajax/ajax-request)
      :else
      (console :error "re-frame-http-fx: expected request to be a list or vector or map, but got: " request))))