(ns sokuot.sokuot
  (:require
   [io.pedestal.interceptor :refer [defhandler]]
   [datomic.api :as d]
   [sokuot.db :as db]
   [sokuot.view :as view]
   [io.pedestal.http.route :refer [url-for]]
   [ring.util.response :refer [response redirect]]))

(defhandler index [req]
  (let [quotes (db/all-quotes (d/db db/conn))]
    (response (view/sokuot-index quotes))))

(defhandler create [req]
  (let [quote (get-in req [:form-params "quote"])
        cite (get-in req [:form-params "cite"])]
    (db/create-quote quote cite)
    (redirect (url-for :sokuot))))

(defhandler delete [req]
  (let [id (get-in req [:path-params :id])]
    (db/delete-quote (Long/parseLong id))
    (redirect (url-for :sokuot))))

(defhandler home [req]
  (response (view/home)))

(defhandler edit [req]
  (let [quotes (db/all-quotes (d/db db/conn))
        id (Long/parseLong (get-in req [:path-params :id]))
        quote (db/view-quote id)]
    (response (view/sokuot-index quotes quote))))

(defhandler edited [req]
  (let [id (Long/parseLong (get-in req [:path-params :id]))
        quote (get-in req [:form-params "quote"])
        cite (get-in req [:form-params "cite"])]
    (db/edited-quote id quote cite)
    (redirect (url-for :sokuot))))