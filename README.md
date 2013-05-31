elastic-cluster
=============

Utility to deploy an ElasticSearch cluster (generates configs and 
bash scripts). 

This tool assumes you are running Linux systems on your local machine and
remote hosts, as it generates Bash scripts. It may be possible to run it on Windows using Cygwin, but this 
has not been tested.

Building the tool
-------------

Provided you have a functional Maven 3 installed on your system, 
building the tool is as simple as running 'mvn clean package' in the 
root folder of your local Git repository.

Let's start with an example, and please bear with the "Star Wars fanboy" tone of it ;-p:

    obiwan@coruscant:~ cd /home/obiwan/git/
    obiwan@coruscant:~ git clone https://github.com/nicosensei/elastic-cluster.git 
    obiwan@coruscant:~ cd elastic-cluster
    obiwan@coruscant:~ mvn clean package

The build generates an executable JAR : 

    /home/obiwan/git/elastic-cluster/target/elastic-cluster-<version>-jar-with-dependencies.jar

Setting up a cluster configuration
-------------

The JAR file contains a [complete setup example](https://github.com/nicosensei/elastic-cluster/blob/master/src/main/resources/setups/example-beans.xml "example cluster setup") with plenty of comments, that will walk you through the tool configuration:

    src/main/resources/setups/example-beans.xml    

That's your starting point.

File generation
-------------

Based on the above example configuration, open a terminal and navigate 
to the root folder of your local Git repository:

    obiwan@coruscant:~ cd /home/obiwan/git/elastic-cluster

Next execute the JAR file as follows

    obiwan@coruscant:~ java -jar  target/elastic-cluster-<version>-jar-with-dependencies.jar src/main/resources/setups/example-beans.xml

The following files will be generated in the 
*/home/obiwan/git/elastic-cluster/target* folder:

    imperial
     |
     |__ tatooine
     |    |
     |    |__ loadBalancer
     |    |    |
     |    |    |__ deploy_node_loadBalancer.sh
     |    |    |__ elasticsearch.yml
     |    |    |__ logging.yml
     |    |    |__ node_loadBalancer.sh
     |    |
     |    |__ master
     |    |    |
     |    |    |__ deploy_node_master.sh
     |    |    |__ elasticsearch.yml
     |    |    |__ logging.yml
     |    |    |__ node_master.sh
     |    |
     |    |__ host_setup_tatooine.sh
     |
     |__ hoth
     |    |
     |    |__ data1
     |    |    |
     |    |    |__ deploy_node_data1.sh
     |    |    |__ elasticsearch.yml
     |    |    |__ logging.yml
     |    |    |__ node_data.sh
     |    |
     |    |__ host_setup_hoth.sh
     |
     |__ yavin
     |    |
     |    |__ data2
     |    |    |
     |    |    |__ deploy_node_data2.sh
     |    |    |__ elasticsearch.yml
     |    |    |__ logging.yml
     |    |    |__ node_data2.sh
     |    |
     |    |__ host_setup_yavin.sh
     |
     |__ cluster_imperial.sh            
     |
     |__ deploy_cluster_imperial.sh
 
 Scripts are generated as executable for the user.
 
 You are welcome to browse the contents of the files to get a better 
 understanding of how they are tied together.
 
Using the scripts
-------------

Still following on the same example, deploying the cluster to the remote
hosts is performed by executing the *deploy_cluster_imperial.sh* script. 

It will install (or freshen) the ElasticSearch installation on every host,
copy configuration files and node management scripts, e.g. *node_xxx.sh* files,
to the configured folders, through SSH.

The cluster management script comes next, its name would be 
*cluster_imperial.sh*.

It takes one parameter among these:

- *start*: starts the cluster
- *status*: shows if the cluster nodes are up
- *stop* : stops the cluster (note that stop commands are issued in reverse order compared to start commands)
- *restart* : stops the starts the cluster
- *plugins*: installs the head plugin (if the node was defined as needing it)
- *destroy*: shuts down the cluster  and removes all files and folders for the cluster

Finally...
-------------

Create an index and start throwing documents at your ES cluster!

Have fun!
