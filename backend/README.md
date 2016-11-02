# Backend

JSON API and small UI for guestbook use.

## Prerequisites

* Java and [Leiningen][1] 2.0 or above installed.
* Copy `profiles_tpl.clj` as `profiles.clj` and customize DB address if needed.
* [MongoDB][2] must be running on the address in profiles.clj.

```
mongod --storageEngine=wiredTiger
```

[1]: https://github.com/technomancy/leiningen
[2]: https://www.mongodb.com


## Running

To start a web server for the application, run:

    lein run

## License

Copyright © 2016 Me
