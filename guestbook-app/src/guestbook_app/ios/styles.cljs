(ns guestbook-app.ios.styles)

(def styles
  {
   :container           {
                         :flex   1
                         :height nil
                         :width  nil
                         }
   :main-view           {
                         :margin      40
                         :align-items "center"
                         }
   :loading-text        {
                         :color            "#fff"
                         :background-color "transparent"
                         }
   :logo                {
                         :width         157
                         :height        22
                         :margin-bottom 30
                         :margin-top    20
                         }
   :welcome             {
                         :color            "#fff"
                         :background-color "transparent"
                         :font-size        30
                         :font-weight      "500"
                         :font-family      "Avenir-Black"
                         :margin-bottom    0
                         :text-align       "center"
                         }
   :info-text           {
                         :color            "#fff"
                         :font-family      "Avenir-Black"
                         :background-color "transparent"
                         :font-size        20
                         :font-weight      "100"
                         :margin-bottom    20
                         :text-align       "center"
                         }
   :form-block          {
                         :background-color "rgba(255,255,255,0.1)"
                         :flex-direction   "row"
                         :padding          15
                         }
   :inputs              {
                         :flex           1
                         :flex-direction "row"
                         }
   :button              {
                         :background-color "#72AF70"
                         :padding-left     13
                         :padding-right    13
                         :padding-top      10
                         :padding-bottom   10
                         :border-radius    2
                         :margin           2
                         }

   :button-text         {
                         :color       "#fff"
                         :text-align  "center"
                         :font-family "Avenir-Black"
                         :font-size   16
                         :font-weight "100"
                         }
   :input-block-wrapper {
                         :flex-grow 1
                         }
   :input-block-label   {
                         :color       "#fff"
                         :font-family "Avenir-Black"
                         :font-size   18
                         :line-height 30
                         :font-weight "100"
                         }
   :input-block-input   {
                         :border-radius    2
                         :font-size        16
                         :height           50
                         :background-color "#fff"
                         :padding          15
                         :margin-right     10
                         }
   :btn-block           {
                         :width      70
                         :margin-top 30
                         }
   :list                {
                         :flex             1
                         :background-color "transparent"
                         :margin-top       10
                         }
   :list-head-row       {
                         :flex-direction      "row"
                         :border-bottom-width 1
                         :border-bottom-color "rgba(255, 255, 255, 0.1)"
                         }
   :list-row            {
                         :flex-direction "row"
                         }
   :list-row-odd        {
                         :background-color "rgba(255, 255, 255, 0.1)"
                         }
   :list-col            {
                         :color   "#fff"
                         :padding 10
                         }
   :list-btn-col        {
                         :padding 10
                         }
   }
  )