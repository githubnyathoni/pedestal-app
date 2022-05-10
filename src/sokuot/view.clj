(ns sokuot.view
  (:require [io.pedestal.http.route :refer [url-for]]
            [hiccup.page :refer [html5]]))

(defn sokuot-form []
  [:form
   {:action (url-for :sokuot#create)
    :method :post}
   [:p.lead "Yok mencatat quote seperti nak senja"]
   [:div.container
    [:div.form-floating.mb-3
     [:input.form-control {:type "text"
                           :name "quote"
                           :id "floatingQuote"
                           :placeholder "isi dengan quote andalanmu~"
                           :required "true"}]
     [:label {:for "floatingQuote"} "Quote"]]
    [:div.form-floating.mb-3
     [:input.form-control {:type "text"
                           :name "cite"
                           :id "floatingCite"
                           :placeholder "Dapet dari mana nih quotenyaa..."
                           :required "true"}]
     [:label {:for "floatingCite"} "Cite"]]
    [:div.form-group.mb-3
     [:div.d-md-flex.justify-content-md-end
      [:button.btn.btn-success {:type "submit"} "Simpen boy"]]]]])

(defn delete-sokuot-form [quote]
  [:form {:action (url-for :sokuot#delete
                           :params {:id (:db/id quote)}
                           :method-param "_method")
          :method :post}
   [:button.btn.btn-danger {:type "submit"} "Apus ngab"]])

(defn sokuot-index [quotes]
    (html5 {:lang "en"}
           [:head
            [:title "Sokuot"]
            [:meta {:name :viewport
                    :content "width=device-width, initial-scale=1"}]
            [:link {:href "/bootstrap/css/bootstrap.min.css"
                    :rel "stylesheet"}]]
           [:body
            [:body.container
             [:h1 "Sokuot"]
             (sokuot-form)
             [:h5.mb-3 "Quotenya..."]
             [:div.container
              (if (seq quotes)
                (for [quote quotes]
                  [:div.card.mb-3
                   [:div.card-header.d-flex.justify-content-between
                    "Quote"
                    [:p [:small [:em.text-secondary (:sokuot/date quote)]]]]
                   [:div.card-body
                    [:blockquote.blockquote
                     [:p (:sokuot/quote quote)]
                     [:footer.blockquote-footer (:sokuot/cite quote)]]
                    [:div.d-flex.justify-content-end
                     (delete-sokuot-form quote)]]])
                [:div
                 [:p.text-center.mb-0 "\"Aku wumbo, kau wumbo, dia wumbo, wumboing, wumboung, wumbology!\""]
                 [:p.text-center.text-secondary [:small [:em "— Patrick Star"]]]])]]]
           [:script {:src "http://code.jquery.com/jquery-2.1.0.min.js"}]
           [:script {:src "/bootstrap/js/bootstrap.bundle.min.js"}]))

(defn home []
  (html5 {:lang "en"}
         [:head
          [:title "Pedestal App"]
          [:meta {:name :viewport
                  :content "width=device-width, initial-scale=1"}]
          [:link {:href "/bootstrap/css/bootstrap.min.css"
                  :rel "stylesheet"}]]
         [:body
          [:body.container
           [:h1.mb-3.mt-3 "Pedestal App"]
           [:div.container
            [:div.card {:style "width: 18rem;
                                border-radius: 1rem;"}
             [:img.card-img-top {:src "/img/sokuot-bg.png"
                                 :style "border-top-left-radius: 1rem;
                                         border-top-right-radius: 1rem;"}]
             [:div.card-body
              [:h5.card-title "Sokuot"]
              [:p.card-text "Aplikasi untuk menyimpan quote-quote favorit agan-agan. 
                             CRD aja karena belum bisa Update."]
              [:div.d-flex.justify-content-end
               [:a.btn.btn-primary {:href "/sokuot"} "Kesini bang"]]]]]]]
         ))