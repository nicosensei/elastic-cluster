========================================================================
elastic-cluster
========================================================================

Utility to deploy an ElasticSearch cluster (generates configs and 
bash scripts). 

This tool assumes you are running a Linux systems, as it generates Bash 
scripts. It may be possible to run it on Windows using Cygwin, but this 
has not been tested.

========================================================================
Building the tool
========================================================================

Provided you have a functional Maven 3 installed on your system, 
building the tool is as simple as running 'mvn clean package' in the 
project root.

The build generates an executable JAR : 

target/elastic-cluster-<version>-jar-with-dependencies.jar

========================================================================
Setting up a cluster configuration
========================================================================

The JAR file contains a complete setup example with plenty of comments, 
that will walk you through the tool configuration:

src/main/resources/setups/example-beans.xml

That's your starting point.

========================================================================
Generated files
========================================================================

========================================================================
Using the scripts
========================================================================
