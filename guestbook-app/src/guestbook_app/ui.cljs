(ns guestbook-app.ui
  (:require [reagent.core :as r]))

(def ReactNative (js/require "react-native"))

(def dimensions (.-Dimensions ReactNative))
(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def activity-indicator (r/adapt-react-class (.-ActivityIndicator ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def list-view (r/adapt-react-class (.-ListView ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def text-input (r/adapt-react-class (.-TextInput ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
