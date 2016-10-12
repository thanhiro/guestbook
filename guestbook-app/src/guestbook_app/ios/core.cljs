(ns guestbook-app.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [guestbook-app.ios.styles :as s]
            [guestbook-app.ui :refer [ReactNative app-registry view
                                      text image list-view
                                      tab-view-page tab-view-animated
                                      tab-bar-top
                                      ]]
            [guestbook-app.ios.components :refer [input-block button]]
            [guestbook-app.events]
            [guestbook-app.subs]))

(def logo-img (js/require "./images/arculogo.png"))
(def bg-img (js/require "./images/bg.png"))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(defn layout []
  (let [this (r/current-component)]
    [image {:source bg-img
            :style  (:container s/styles)}
     (into [view {:style (:main-view s/styles)}
            [image {:source logo-img
                    :style  (:logo s/styles)}]] (r/children this))]))

(defn loading-panel []
  (fn []
    [layout
     [text {:style (:loading-text s/styles)} "loading..."]]))

(defn is-odd? [num] (not= (mod num 2) 0))

(defn render-scene [{:keys [route]}]
  [text route])

(defn render-header [props]
  [tab-bar-top (js->clj props)])

(defn render-list-header []
  [view {:style (:list-head-row s/styles)}
   [text {:style (:list-col s/styles)} "First name"]
   [text {:style (:list-col s/styles)} "Last name"]
   [text {:style (:list-col s/styles)} "Company"]
   [text {:style (:list-col s/styles)} "Host"]])

(defn render-list-row [data _ id]
  (let [
        base-style (:list-row s/styles)
        rstyle (merge base-style (when (is-odd? id) (:list-row-odd s/styles)))]
    [view {:style rstyle}
     [text {:style (:list-col s/styles)} (.-name data)]
     [text {:style (:list-col s/styles)} (.-name data)]
     [text {:style (:list-col s/styles)} (.-name data)]
     [text {:style (:list-col s/styles)} (.-name data)]
     [button "Check-in" #(alert "clicked!")]]))

(defn main-panel []
  (let [
        greeting (subscribe [:get-greeting])
        visitors-today (subscribe [:get-visitors-today])
        DataSource (.-DataSource (.-ListView ReactNative))
        ds (new DataSource (clj->js {:rowHasChanged (fn [row1 row2] (not= row1 row2))}))
        data-source (.cloneWithRows ds (clj->js @visitors-today))
        ]
    (fn []
      [layout
       [text {:style (:welcome s/styles)} @greeting]
       [text {:style (:info-text s/styles)}
        "Please leave a note of your visit"]
       [view {:style (:form-block s/styles)}
        [input-block "First name"]
        [input-block "Last name"]
        [input-block "Company"]
        [input-block "Host"]
        [button "Add" #(alert "clicked!")]]
       ;[button "XHR" #(dispatch [:request-visitors-today])]
       [view {:style {:flex 1}}
        [list-view {
                    :style      (:list s/styles)
                    :renderHeader #(r/as-component (render-list-header))
                    :dataSource data-source
                    :renderRow  #(r/as-component (render-list-row %1 %2 %3))
                    }]]

       ;[tab-view-animated
       ; {
       ;  :style              {:flex 1}
       ;  :navigationState    (clj->js
       ;                        {
       ;                         :index  0
       ;                         :routes [
       ;                                  {:key "1" :title "MY"}
       ;                                  {:key "2" :title "OTHER"}
       ;                                  ]})
       ;  :renderScene        (fn [p] (r/as-component (render-scene p)))
       ;  :renderHeader       (fn [p] (r/as-component (render-header p)))
       ;  :onRequestChangeTab (fn [idx] (println idx))
       ;  }]

       ])))

(defn app-root []
  (let [ready? (subscribe [:initialised?])]
    (fn []
      (if-not @ready?
        [loading-panel]
        [main-panel]))))

(defn init []
  (dispatch [:initialize-db])
  (dispatch [:request-visitors-today])
  (.registerComponent app-registry "GuestbookApp" #(r/reactify-component app-root)))
