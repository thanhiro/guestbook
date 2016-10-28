(ns guestbook-app.ios.components
  (:require [reagent.core :as r :refer [atom]]
            [guestbook-app.ui :refer [view text text-input touchable-highlight]]
            [guestbook-app.ios.styles :as s]))

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

(defn tab-bar []
  (let [this (r/current-component)]
    (into [view {:style (:tab-bar s/styles)}]
          (r/children this))))

(defn tab-link [label handle-press]
  [touchable-highlight {
                        :style (:tab-link s/styles)
                        :on-press handle-press
                        }
   [view
    [text {:style (:tab-label s/styles)} label]]])