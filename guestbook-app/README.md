# Guestbook Mobile Client

ClojureScript React Native client for Guestbook, made with re-natal and 
re-frame. This has just iOS version so far.

## Usage

### First install the basics
 
* [node.js]/npm
* leiningen (and Java of course)
* re-natal for ClojureScript to React Native
* rlwrap for nicer repl experience
* Something I forgot here

###  Fetch dependencies

    npm i

### Initialize Figwheel

    re-natal use-figwheel

### Run in iPad

    react-native run-ios --simulator "iPad Air 2"

### Start Figwheel

    rlwrap lein figwheel ios

### Code!
    
Figwheel gives you nice, instant development experience. Goodies like
Chome debugging of Reat Native works.
    
## License

Copyright © 2016 Me

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[node.js]:https://nodejs.org
