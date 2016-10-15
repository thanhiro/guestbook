(ns guestbook-app.ui
  (:require [reagent.core :as r]))

(def ReactNative (js/require "react-native"))
(def Tabs (js/require "react-native-tab-view"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def activity-indicator (r/adapt-react-class (.-ActivityIndicator ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def list-view (r/adapt-react-class (.-ListView ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def text-input (r/adapt-react-class (.-TextInput ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

;re-natal use-component react-native-tab-view
(def tab-view-page (r/adapt-react-class (.-TabViewPage Tabs)))
(def tab-view-animated (r/adapt-react-class (.-TabViewAnimated Tabs)))
(def tab-bar-top (r/adapt-react-class (.-TabBarTop Tabs)))
