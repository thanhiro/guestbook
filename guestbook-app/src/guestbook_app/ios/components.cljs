(ns guestbook-app.ios.components
  (:require [reagent.core :as r :refer [atom]]
            [guestbook-app.ios.styles :as s]))

(def ReactNative (js/require "react-native"))

(def text (r/adapt-react-class (.-Text ReactNative)))
(def text-input (r/adapt-react-class (.-TextInput ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(defn input-block [name]
  [view {:style (:input-block-wrapper s/styles)}
   [text {:style (:input-block-label s/styles)} name]
   [text-input
    {
     :style          (:input-block-input s/styles)
     :placeholder    name
     ;;:on-change-text #(str %)
     }]])

(defn button [label handle-press]
  [touchable-highlight {:style    (:button s/styles)
                        :on-press handle-press}
   [text {:style (:button-text s/styles)} label]])