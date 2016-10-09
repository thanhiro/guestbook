(ns guestbook-app.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [guestbook-app.ios.styles :as s]
            [guestbook-app.events]
            [guestbook-app.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def text-input (r/adapt-react-class (.-TextInput ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def list-view (r/adapt-react-class (.-ListView ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(def logo-img (js/require "./images/arculogo.png"))
(def bg-img (js/require "./images/bg.png"))

(defn input-block [name]
  [view {:style {:flex           1
                 :flex-direction "column"
                 :margin-right   15}}
   [text {:style {:color "#fff" :font-family "Avenir" :font-size 18 :line-height 30 :font-weight "100"}} name]
   [text-input {:style          {
                                 :border-radius    2
                                 :font-size        15
                                 :height           50
                                 :background-color "#fff"
                                 :padding          15
                                 }
                :placeholder    name
                :on-change-text #(str %)}]])

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(defn app-root []
  (let [
        greeting (subscribe [:get-greeting])
        DataSource (.-DataSource (.-ListView ReactNative))
        list-stuff (clj->js [
                             {:firstName "Teppo" :lastName "Winnipegin" :company "Jets" :host "Spede Pasanen"}
                             {:firstName "Reijo" :lastName "Ruotsalainen" :company "Oilers" :host "Simo Salminen"}
                             {:firstName "Vellu" :lastName "Ketola" :company "Ässät" :host "Vesa-Matti Loiri"}
                             ])
        ds (new DataSource (clj->js {:rowHasChanged (fn [row1 row2] (not= row1 row2))}))
        data-source (.cloneWithRows ds list-stuff)
        ]
    (fn []
      [image {:source bg-img
              :style  (:container s/styles)}
       [view {:style (:main-view s/styles)}
        [image {:source logo-img
                :style  (:logo s/styles)}]
        [text {:style (:welcome s/styles)} @greeting]
        [text {:style (:info-text s/styles)}
         "Please leave a note of your visit"]
        [view {:style (:form-block s/styles)}
         [input-block "First name"]
         [input-block "Last name"]
         [input-block "Company"]
         [input-block "Host"]
         [touchable-highlight {:style    (:button s/styles)
                               :on-press #(alert "foo")}
          [text {:style (:button-text s/styles)} "Add"]]]
        (comment [view
                  ;[text (js/JSON.stringify data-source)]
                  [text (js/JSON.stringify [list-view {:style      {}
                                                       :dataSource data-source
                                                       :renderRow  #([text (:firstName %)])
                                                       }])]])
        ]])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "GuestbookApp" #(r/reactify-component app-root)))
