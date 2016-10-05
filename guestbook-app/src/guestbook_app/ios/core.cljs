(ns guestbook-app.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [guestbook-app.events]
            [guestbook-app.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def text-input (r/adapt-react-class (.-TextInput ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(def logo-img (js/require "./images/arculogo.png"))
(def bg-img (js/require "./images/bg.png"))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(defn app-root []
  (let [greeting (subscribe [:get-greeting])]
    (fn []
      [image {:source bg-img
              :style  {:flex 1 :height nil :width nil}}
       [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
        [image {:source logo-img
                :style  {:width 157 :height 22 :margin-bottom 30}}]
        [text {:style
               {:color "#fff" :background-color "transparent" :font-size 30
                :font-weight "500" :font-family "Avenir" :margin-bottom 0 :text-align "center"}} @greeting]
        [text {:style
               {:color "#fff" :font-family "Avenir" :background-color "transparent" :font-size 20
                :font-weight "100" :margin-bottom 20 :text-align "center"}}
         "Please leave a note of your visit"]
        [view {:style {:background-color "rgba(255,255,255,0.1)"
                       :flex-direction "row" :width 660 :padding 20 :align-items "center"}}
         [text-input {:style {
                              :flex 1
                              :height 50
                              :background-color "#fff"
                              :padding 15}
                      :placeholder ""
                      :on-change-text #(str %)}]
         [text-input {:style {
                              :flex 1
                              :marginLeft 10
                              :height 50
                              :background-color "#fff"
                              :padding 15}
                      :placeholder ""
                      :on-change-text #(str %)}]
         [text-input {:style {
                              :flex 1
                              :marginLeft 10
                              :height 50
                              :background-color "#fff"
                              :padding 15}
                     :placeholder ""
                     :on-change-text #(str %)}]
         [text-input {:style {
                              :flex 1
                              :marginLeft 10
                              :height 50
                              :background-color "#fff"
                              :padding 15}
                      :placeholder ""
                      :on-change-text #(str %)}]

         [touchable-highlight {:style {
                                       :background-color "#72AF70"
                                       :padding 15
                                       :marginLeft 10
                                       :border-radius 2}
                               :on-press #(alert "foo")}
          [text {:style {:color "#fff" :text-align "center" :font-family "Avenir" :font-weight "100"}} "Add"]]]
        ]])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "GuestbookApp" #(r/reactify-component app-root)))
