(ns guestbook-app.ios.layout
  (:require [reagent.core :as r :refer [atom]]
            [guestbook-app.ui :refer [view image]]
            [guestbook-app.ios.styles :as s]))

(def logo-img (js/require "./images/arculogo.png"))
(def bg-img (js/require "./images/bg.png"))

(defn layout []
  (let [this (r/current-component)]
    [image {:source bg-img
            :style  (:container s/styles)}
     (into [view {:style (:main-view s/styles)}
            [image {:source logo-img
                    :style  (:logo s/styles)}]] (r/children this))]))
