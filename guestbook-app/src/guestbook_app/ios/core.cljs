(ns guestbook-app.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [guestbook-app.ios.styles :as s]
            [guestbook-app.ui :refer [ReactNative app-registry view
                                      text image list-view dimensions
                                      activity-indicator
                                      ]]
            [guestbook-app.ios.components :refer [input-block button
                                                  tab-bar tab-link]]
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
     [activity-indicator {:style {} :color "white" :size "large"}]]))

(defn is-odd? [num] (not= (mod num 2) 0))

(defn render-list-header [width]
  (let [
        col-style (assoc (:list-col s/styles) :width width)]
    [view {:style (:list-head-row s/styles)}
     [text {:style col-style} "First name"]
     [text {:style col-style} "Last name"]
     [text {:style col-style} "Company"]
     [text {:style col-style} "Host"]
     [text {:style col-style} ""]]))

(defn render-list-row [data _ id width]
  (let [
        base-style (:list-row s/styles)
        rstyle (merge base-style (when (is-odd? id) (:list-row-odd s/styles)))
        col-style (assoc (:list-col s/styles) :width width)]
    [view {:style rstyle}
     [text {:style col-style} (.-name data)]
     [text {:style col-style} (.-name data)]
     [text {:style col-style} (.-name data)]
     [text {:style col-style} (.-name data)]
     [view {:style (assoc (:list-btn-col s/styles) :width width)}
      [button "Check-in" #(alert "clicked!")]]]))

;; {:strs [key {:strs [route]}]}
(defn render-scene [ds col-width]
  [view {:style {:flex 1}}
   [list-view
    {
     :style        (:list s/styles)
     :renderHeader #(r/as-component (render-list-header col-width))
     :dataSource   ds
     :renderRow    #(r/as-component (render-list-row %1 %2 %3 col-width))
     }]
   ])

(defn handle-tab-press [indx navi-state routes]
  #(reset! navi-state {:index indx :routes routes}))

(defn main-panel []
  (let [
        routes [
                {:key "1" :title "Visitors today"}
                {:key "2" :title "Past visitors"}
                ]
        navi-state (r/atom
                     {
                      :index  0
                      :routes routes})
        greeting (subscribe [:get-greeting])
        visitors-all (subscribe [:get-visitors-all])
        visitors-today (take 3 @visitors-all)
        DataSource (.-DataSource (.-ListView ReactNative))
        ds (new DataSource (clj->js {:rowHasChanged (fn [row1 row2] (not= row1 row2))}))
        data-source-all (.cloneWithRows ds (clj->js @visitors-all))
        data-source-today (.cloneWithRows ds (clj->js visitors-today))
        size (.get dimensions "window")
        width (- (.-width size) 80)
        col-width (/ width 5)
        ]
    (fn []
      [layout
       [text {:style (:welcome s/styles)} @greeting]
       [text {:style (:info-text s/styles)}
        "Please leave a note of your visit"]
       [view {:style (:form-block s/styles)}
        [view {:style (:inputs s/styles)}
         [input-block "First name"]
         [input-block "Last name"]
         [input-block "Company"]
         [input-block "Host"]]
        [view {:style (:btn-block s/styles)}
         [button "Add" #(dispatch [:request-visitors-today])]]]
       ;;#(swap! navi-state update :index 1)

       (comment
         [tab-view-animated
          {
           :style              {:flex 1}
           :navigation-state   (clj->js @navi-state)
           :render-scene       #(r/as-component
                                 [view {:style {:width width}}])
           :render-header      #(r/as-component
                                 (render-header %))
           :onRequestChangeTab #(reset! navi-state {:index (js->clj %) :routes routes})
           }])
       [tab-bar
        [tab-link "visitors today" (handle-tab-press 0 navi-state routes)]
        [tab-link "past visitors" (handle-tab-press 1 navi-state routes)]]
       [view
        (r/as-component
          (render-scene
            (case (:index @navi-state)
              0 data-source-today
              1 data-source-all
              nil) col-width))]
       ])))

(defn app-root []
  (let [
        ready? (subscribe [:initialised?])
        ]
    (fn []
      (let [th (r/current-component)
            ]
        (if-not @ready?
          [loading-panel]
          [main-panel])))))

(defn init []
  (dispatch [:initialize-db])
  (dispatch [:request-visitors-all])
  (.registerComponent app-registry "GuestbookApp" #(r/reactify-component app-root)))
