(ns sokuot.view
  (:require [io.pedestal.http.route :refer [url-for]]
            [hiccup.page :refer [html5]]))

(defn sokuot-form [& [quote]]
  [:form
   {:action (if quote
              (url-for :sokuot#edited
                       :params {:id (:db/id quote)}
                       :method-param "_method")
              (url-for :sokuot#create))
    :method :post}
   [:p.lead "Yok mencatat quote seperti nak senja"]
   [:div.container
    [:div.form-floating.mb-3
     [:input.form-control {:type "text"
                           :name "quote"
                           :id "floatingQuote"
                           :placeholder "isi dengan quote andalanmu~"
                           :required "true"
                           :value (:sokuot/quote quote)}]
     [:label {:for "floatingQuote"} "Quote"]]
    [:div.form-floating.mb-3
     [:input.form-control {:type "text"
                           :name "cite"
                           :id "floatingCite"
                           :placeholder "Dapet dari mana nih quotenyaa..."
                           :required "true"
                           :value (:sokuot/cite quote)}]
     [:label {:for "floatingCite"} "Cite"]]
    [:div.form-group.mb-3
     [:div.d-md-flex.justify-content-md-end
      [:button.btn.btn-success {:type "submit"}
       (if quote
         "Edit nih?"
         "Simpen boy")]]]]])

(defn delete-sokuot-form [quote]
  [:form {:action (url-for :sokuot#delete
                           :params {:id (:db/id quote)}
                           :method-param "_method")
          :method :post}
   [:button.btn.btn-danger {:type "submit"} "Apus ngab"]])

(defn edit-sokuot-form [quote]
  [:form {:action (url-for :sokuot#edit
                           :params {:id (:db/id quote)}
                           :method-param "_method")
          :method :post}
   [:button.btn.btn-secondary {:type "submit"} "Edit dong"]])

(defn rand-quote []
  (zipmap [:quote :cite]
          (rand-nth [["\"Aku wumbo, kau wumbo, dia wumbo, wumboing, wumboung, wumbology!\"" "— Patrick Star"]
                     ["\"Kosong adalah isi, isi adalah kosong\"" "— Prajnaparamitahrdaya Sutra"]
                     ["\"Experience is the best teacher\"" "— Buku Sidu"]
                     ["\"Hey Patrick, kau tau apa yang lebih lucu dari 24? 25\"" "— Spongebob Squarepants"]])))

(defn sokuot-index [quotes & [quote]]
  (let [quote-nil (rand-quote)]
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
             (sokuot-form quote)
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
                    (if (:sokuot/edited? quote)
                      [:div.d-flex.justify-content-between
                       [:small.text-secondary "(Dah diedit)"]
                       [:div..d-flex.justify-content-end.gap-2
                        (edit-sokuot-form quote)
                        (delete-sokuot-form quote)]]
                      [:div..d-flex.justify-content-end.gap-2
                       (edit-sokuot-form quote)
                       (delete-sokuot-form quote)])]])
                [:div
                 [:p.text-center.mb-0 (quote-nil :quote)]
                 [:p.text-center.text-secondary [:small [:em (quote-nil :cite)]]]])]]]
           [:script {:src "http://code.jquery.com/jquery-2.1.0.min.js"}]
           [:script {:src "/bootstrap/js/bootstrap.bundle.min.js"}])))

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
              [:p.card-text "Aplikasi untuk menyimpan quote favorit agan-agan. 
                             CRUD sederhana tapi kok mayan syulit make Clojure"]
              [:div.d-flex.justify-content-end
               [:a.btn.btn-primary {:href "/sokuot"} "Kesini bang"]]]]]]]))