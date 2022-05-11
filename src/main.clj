(ns main
  (:require
   [io.pedestal.http.route.definition :refer [defroutes]]
   [io.pedestal.http.route :as route :refer [router]]
   [io.pedestal.http :as http]
   [ns-tracker.core :refer [ns-tracker]]
   [sokuot.sokuot :as sokuot]
   [io.pedestal.http.ring-middlewares :as middleware]
   [io.pedestal.http.body-params :refer [body-params]]))


(defroutes routes
  [[["/" ^:interceptors [http/html-body (body-params)]
     ["/" {:get [:home sokuot/home]}]
     ["/sokuot" {:get [:sokuot sokuot/index]
                 :post [:sokuot#create sokuot/create]}
      ["/:id" {:delete [:sokuot#delete sokuot/delete]
               :edit [:sokuot#edit sokuot/edit]
               :edited [:sokuot#edited sokuot/edited]}]]]]])

(def modified-namespaces (ns-tracker "src"))

(def service
  {::http/interceptors [route/query-params
                        (middleware/file-info)
                        (middleware/resource "public")
                        (route/method-param :_method)
                        (router (fn []
                                  (doseq [ns-sym (modified-namespaces)]
                                    (require ns-sym :reload))
                                  routes))]
   ::http/port 8081
   ::http/type :jetty})

(defn -main [& arg]
  (-> service
      http/create-server
      http/start))