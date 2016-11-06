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
    [db [_ response]]
    (-> db
        (assoc
          :loading? false
          :error response))))

(defn response-handler [key]
  (fn
    [db [_ response]]
    (println response)
    (-> db
        (assoc :loading? false)
        (assoc key response))))

(reg-event-db
  :process-response-all
  (response-handler :visitors-all))

(reg-event-db
  :process-response-today
  (response-handler :visitors-today))

(reg-event-db
  :process-response-post
  (response-handler :posted-visitor))

(defn http-action
  [opts]
  (fn
    [{db :db} _]
    {:http-fetch
         (assoc opts
           :on-failure [:bad-response])
     :db (assoc db :loading? true)}))

(reg-event-fx
  :request-visitors-today
  (http-action
    {:method     :get
     :uri        "http://localhost:3000/api/visitors/today"
     :on-success [:process-response-today]
     }))

(reg-event-fx
  :request-visitors-all
  (http-action
    {:method     :get
     :uri        "http://localhost:3000/api/visitors"
     :on-success [:process-response-all]
     }))

(reg-event-fx
  :post-visitor
  (fn
    [{db :db} data]
    (let [body (js/JSON.stringify (clj->js (last data)))]
      {:http-fetch
           {:method     :post
            :uri        "http://localhost:3000/api/visitors"
            :body       body
            :on-success [:process-response-post]
            :on-failure [:bad-response]
            }
       :db (assoc db :loading? true)})))