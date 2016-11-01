;; Template for local variables, rename this to profiles.clj
;;
;; WARNING
;; The profiles.clj file is used for local environment variables, such as database credentials.
;; profiles.clj is listed in .gitignore and will be excluded from version control by Git.

{:profiles/dev  {:env {:database-url "mongodb://127.0.0.1/backend_dev"}}
 :profiles/test {:env {:database-url "mongodb://127.0.0.1/backend_test"}}}
