(ns guestbook-app.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [guestbook-app.ios.styles :as s]
            [guestbook-app.ui :refer [ReactNative app-registry view
                                      text image list-view dimensions
                                      activity-indicator
                                      ]]
            [guestbook-app.ios.layout :refer [layout]]
            [guestbook-app.ios.components :refer [input-block button
                                                  tab-bar tab-link]]
            [guestbook-app.events]
            [guestbook-app.subs]))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

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
     [text {:style col-style} (.-firstName data)]
     [text {:style col-style} (.-lastName data)]
     [text {:style col-style} (.-company data)]
     [text {:style col-style} (.-host data)]
     [view {:style (assoc (:list-btn-col s/styles) :width width)}
      [button "Check-in"
        #(alert (str (.-lastName data) " " (.-firstName data)))]]]))

(defn add-form []
  [view {:style (:form-block s/styles)}
   [view {:style (:inputs s/styles)}
    [input-block "First name"]
    [input-block "Last name"]
    [input-block "Company"]
    [input-block "Host"]]
   [view {:style (:btn-block s/styles)}
    [button "Add" #(dispatch [:request-visitors-today])]]])

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

(defn data-source []
  (let [DataSource (.-DataSource (.-ListView ReactNative))]
    (DataSource.
         (clj->js {:rowHasChanged (fn [row1 row2] (not= row1 row2))}))))

(defn clone-ds-with-rows [ds rows]
  (.cloneWithRows ds (clj->js rows)))

(defn handle-tab-press [indx navi-state routes]
  #(reset! navi-state {:index indx :routes routes}))

(defn main-panel []
  (let [routes [{:key "1" :title "Visitors today"}
                {:key "2" :title "Past visitors"}]
        navi-state (r/atom
                     {
                      :index  0
                      :routes routes})
        greeting (subscribe [:get-greeting])
        visitors-today (subscribe [:get-visitors-today])
        ;; visitors-today (take 3 @visitors-all)
        ds (data-source)
        data-source-all (clone-ds-with-rows ds @visitors-today)
        data-source-today (clone-ds-with-rows ds @visitors-today)
        size (.get dimensions "window")
        width (- (.-width size) 80)
        col-width (/ width 5)]
    (fn []
      [layout
       [text {:style (:welcome s/styles)} @greeting]
       [text {:style (:info-text s/styles)}
        "Please leave a note of your visit"]

       [add-form]

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
      (let [th (r/current-component)]
        (if-not @ready?
          [loading-panel]
          [main-panel])))))

(defn init []
  (dispatch [:initialize-db])
  (dispatch [:request-visitors-today])
  (.registerComponent app-registry "GuestbookApp" #(r/reactify-component app-root)))
