(ns guestbook-app.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [guestbook-app.ios.styles :as s]
            [guestbook-app.ios.components :refer [input-block button]]
            [guestbook-app.events]
            [guestbook-app.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def list-view (r/adapt-react-class (.-ListView ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))

(def logo-img (js/require "./images/arculogo.png"))
(def bg-img (js/require "./images/bg.png"))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(defn app-root []
  (let [
        greeting (subscribe [:get-greeting])
        visitors-today (subscribe [:get-visitors-today])
        DataSource (.-DataSource (.-ListView ReactNative))
        ds (new DataSource (clj->js {:rowHasChanged (fn [row1 row2] (not= row1 row2))}))
        data-source (.cloneWithRows ds visitors-today)
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
         [button "Add" #(alert "clicked!")]]
        [text (js/JSON.stringify @visitors-today)]
        (comment [view
                  [text (js/JSON.stringify [list-view {:style      {}
                                                       :dataSource data-source
                                                       :renderRow  #([text (:firstName %)])
                                                       }])]])
        ]])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "GuestbookApp" #(r/reactify-component app-root)))
