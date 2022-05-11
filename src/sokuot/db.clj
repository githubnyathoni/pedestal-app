(ns sokuot.db
  (:require [datomic.api :as d]))

(def db-uri "datomic:mem://sokuot")

(d/create-database db-uri)

(def conn (d/connect db-uri))

(def sokuot-schema [{:db/id #db/id [:db.part/db]
                     :db/ident :sokuot/quote
                     :db/valueType :db.type/string
                     :db/cardinality :db.cardinality/one
                     :db/doc "Isi quote"}
                    {:db/id #db/id [:db.part/db]
                     :db/ident :sokuot/cite
                     :db/valueType :db.type/string
                     :db/cardinality :db.cardinality/one
                     :db/doc "Pencipta quote"}
                    {:db/id #db/id [:db.part/db]
                     :db/ident :sokuot/date
                     :db/valueType :db.type/string
                     :db/cardinality :db.cardinality/one
                     :db/doc "Tanggal dibuat data quote"}
                    {:db/id #db/id [:db.part/db]
                     :db/ident :sokuot/edited?
                     :db/valueType :db.type/boolean
                     :db/cardinality :db.cardinality/one
                     :db/doc "Keterangan udah diedit apa belum"}])

(d/transact conn sokuot-schema)

(def now
  (.format (java.text.SimpleDateFormat. "dd MMM yyyy 'at' hh:mm aaa") (new java.util.Date)))

(defn sokuot-tx [quote cite]
  [{:db/id (d/tempid :db.part/user)
    :sokuot/quote quote
    :sokuot/cite cite
    :sokuot/date now
    :sokuot/edited? false}])

(defn create-quote [quote cite]
  (d/transact conn (sokuot-tx quote cite)))

(defn all-quotes [db]
  (->> (d/q '[:find ?id
              :where [?id :sokuot/quote]]
            db)
       (map first)
       (map #(d/entity db %))
       (reverse)))

(defn view-quote [id]
  (d/pull (d/db conn) '[*] id))

(defn delete-quote [id]
  (d/transact conn [[:db.fn/retractEntity id]]))

(defn edited-quote [id quote cite]
  (d/transact conn [[:db/add id :sokuot/quote quote]
                    [:db/add id :sokuot/cite cite]
                    [:db/add id :sokuot/edited? true]]))