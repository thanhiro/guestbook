(ns guestbook-app.events
  (:require
    [re-frame.core :refer [reg-event-db after dispatch]]
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
        (assoc :visitors-today response))))

(reg-event-db
  :request-visitors-today
  (fn
    [db _]
    ;; kick off the GET, making sure to supply a callback for success and failure
    (.catch
      (.then
        (.then
          (js/fetch "https://jsonplaceholder.typicode.com/users")
          #(js->clj (.json %1)))
        #(dispatch [:process-response %1]))
      #(dispatch [:bad-response %1]))
    (assoc db :loading? true)))