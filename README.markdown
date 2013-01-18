# dynjs - invokedynamic-based javascript impl

![cloudbees rocks!](http://static-www.cloudbees.com/images/badges/BuiltOnDEV.png)

## Setting up environment


### Getting JDK7

OSX users can get it at [java site](http://java.sun.com).

Download and install it to your user(not to the entire machine) and before running `mvn install` run (or use Java7 as default compiler):
		
		export JAVA_HOME=$(/usr/libexec/java_home -v 1.7)
		

## Getting started


### Building from sources

1. `git clone https://github.com/dynjs/dynjs.git && cd dynjs`
2. `mvn install`

### Download

Alternatively download the [latest version](https://dynjs.ci.cloudbees.com/job/dynjs/ws/target/dynjs-all.jar) from our CI job.

### Running 

Run `./run-repl` for the REPL and try the snippet below:

```javascript
var x = 1 + 1;
print(x);
```

For more options, run `java -jar target/dynjs-all.jar --help`.

