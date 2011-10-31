# dyn.js - invokedynamic-based javascript impl

![cloudbees rocks!](http://static-www.cloudbees.com/images/badges/BuiltOnDEV.png)

Setting up environment
------------

1. Download and install OpenJDK7, it's Java7 based:

    OpenJDK 7 can be downloaded at http://code.google.com/p/openjdk-osx-build/downloads/detail?name=OpenJDK-OSX-1.7-universal-20110825.dmg.
		Download and install it to your user(not to the entire machine) and before running mvn install run (or use Java7 as default compiler):
		
		 		export JAVA_HOME=$(/usr/libexec/java_home -v 1.7)
		
2. Configure and add projects dependencies:

		Jitescript can be found at https://github.com/qmx/jitescript
			Clone it (https://github.com/qmx/jitescript.git) and run mvn install to build and add to your repository

Getting started
------------

1. git clone https://github.com/dynjs/dyn.js.git && cd dynjs
2. mvn install
3. Get into dynjs/target folder
4. Try java -jar dynjs-all.jar --help
5. Run the snippet below:

```javascript
var x = 1 + 1;
print(x);
```
