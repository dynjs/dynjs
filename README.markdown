# dyn.js - invokedynamic-based javascript impl

Requirements
------------

Download and install OpenJDK7, it's Java7 based:

    OpenJDK 7 can be downloaded at http://code.google.com/p/openjdk-osx-build/downloads/detail?name=OpenJDK-OSX-1.7-universal-20110825.dmg.
		Download and install it to your user(not to the entire machine) and before running mvn install run (or use Java7 as default compiler):
		
		 		export JAVA_HOME=$(/usr/libexec/java_home -v 1.7)
		
Installation
------------

Configure and add projects dependencies:

		Jitescript can be found at https://github.com/qmx/jitescript
			Clone it (https://github.com/qmx/jitescript.git) and run mvn install to build and add to your repository

		The 1.1.0-SNAPSHOT of aunit can be found at https://github.com/porcelli/aunit
			Clone it (https://github.com/porcelli/aunit.git) and run mvn install to build and add to your repository

    You must download asm-jar from http://forge.ow2.org/projects/asm/ version
    4.0_RC1 and manually install with maven

    mvn install:install-file -Dfile=asm-all-4.0_RC1.jar -DgroupId=asm
    -DartifactId=asm-all -Dversion=4.0_RC1 -Dpackaging=jar
