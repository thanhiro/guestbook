(ns guestbook-app.events
  (:require
    [guestbook-app.fetch-fx :refer [fetch]]
    [re-frame.core :refer [reg-event-db reg-event-fx after dispatch console]]
    [clojure.spec :as s]
    [guestbook-app.db :as db :refer [app-db]]))

;; -- Middleware ------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/wiki/Using-Handler-Middleware
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check failed: " explain-data) explain-data)))))

(def validate-spec-mw
  (if goog.DEBUG
    (after (partial check-and-throw ::db/app-db))
    []))

;; -- Handlers --------------------------------------------------------------
(reg-event-db
  :initialize-db
  ;;validate-spec-mw
  (fn [_ _]
    app-db))

(reg-event-db
  :set-greeting
  ;;validate-spec-mw
  (fn [db [_ value]]
    (assoc db :greeting value)))

(reg-event-db
  :bad-response
  (fn
    [db [_ _]]
    (-> db
        (assoc :loading? false))))

(reg-event-db
  ;; when the GET succeeds
  :process-response
  (fn
    [db [_ response]]
    (-> db
        (assoc :loading? false)
        (assoc :visitors-all response))))

(reg-event-fx
  :request-visitors-all
  (fn
    [{db :db} _]
    {:http-fetch
         {:method     :get
          :uri        "https://jsonplaceholder.typicode.com/users"
          :on-success [:process-response]
          :on-failure [:bad-response]}
     :db (assoc db :loading? true)}))
