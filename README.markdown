# dynjs - invokedynamic-based javascript impl

![cloudbees rocks!](http://static-www.cloudbees.com/images/badges/BuiltOnDEV.png)

## Setting up environment


### Getting JDK7

OSX users can get it at [openjdk-osx-build].

Download and install it to your user(not to the entire machine) and before running `mvn install` run (or use Java7 as default compiler):
		
		export JAVA_HOME=$(/usr/libexec/java_home -v 1.7)
		
### Getting dependencies

	1. download and install **[jitescript]**
	1. download and install **[invokebinder]**


## Getting started


### Building from sources

1. fetch the dependencies: **[jitescript]** and **[invokebinder]**
1. `git clone https://github.com/dynjs/dynjs.git && cd dynjs`
1. `mvn install`
1. Get `dynjs-all.jar` into dynjs/target folder

### Download

Alternatively download the [latest version](https://dynjs.ci.cloudbees.com/job/dynjs/ws/target/dynjs-all.jar) from our CI job.

### Running 

Try `java -jar dynjs-all.jar --help`

Run the snippet below:

```javascript
var x = 1 + 1;
print(x);
```

[jitescript]:https://github.com/qmx/jitescript
[invokebinder]:https://github.com/headius/invokebinder
[openjdk-osx-build]:http://code.google.com/p/openjdk-osx-build
