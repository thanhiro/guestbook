(ns guestbook-app.ios.styles)

(def styles
  {
   :container           {
                         :flex   1
                         :height nil
                         :width  nil
                         }
   :main-view           {
                         :flex-direction "column"
                         :margin         40
                         :align-items    "center"
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
                         :width            660
                         :padding          20
                         :align-items      "center"}

   :button              {
                         :background-color "#72AF70"
                         :padding          13
                         :margin-top       30
                         :border-radius    2
                         }

   :button-text         {
                         :color       "#fff"
                         :text-align  "center"
                         :font-family "Avenir-Black"
                         :font-size   18
                         :font-weight "100"
                         }
   :input-block-wrapper {
                         :flex           1
                         :flex-direction "column"
                         :margin-right   15
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
                         :font-size        15
                         :height           50
                         :background-color "#fff"
                         :padding          15
                         }
   }
  )