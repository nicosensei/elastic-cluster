elastic-cluster
=============

Utility to deploy an ElasticSearch cluster (generates configs and 
bash scripts). 

This tool assumes you are running a Linux systems, as it generates Bash 
scripts. It may be possible to run it on Windows using Cygwin, but this 
has not been tested.

Building the tool
-------------

Provided you have a functional Maven 3 installed on your system, 
building the tool is as simple as running 'mvn clean package' in the 
root folder of your local Git repository.

Let's start with an example :

    cd /home/obiwan/git/elastic-cluster
    mvn clean package

The build generates an executable JAR : 

    /home/obiwan/git/elastic-cluster/target/elastic-cluster-<version>-jar-with-dependencies.jar

Setting up a cluster configuration
-------------

The JAR file contains a complete setup example with plenty of comments, 
that will walk you through the tool configuration:

    src/main/resources/setups/example-beans.xml

That's your starting point.

And please bear with the "Star Wars fanboy" tone of the sample ;-p

File generation
-------------

Based on the above example configuration, open a terminal and navigate 
to the root folder of your local Git repository:

    cd /home/obiwan/git/elastic-cluster

Next execute the JAR file as follows

    java -jar  target/elastic-cluster-<version>-jar-with-dependencies.jar \
    src/main/resources/setups/example-beans.xml

The following files will be generated in the 
*/home/obiwan/git/elastic-cluster/target* folder:

    myCluster
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
     |__ cluster_myCluster.sh            
     |
     |__ deploy_cluster_myCluster.sh
 
 Scripts are generated as executable for the user.
 
 You are welcome to browse the contents of the files to get a better 
 understanding of how they are tied together.
 
Using the scripts
-------------

Still following on the same example, deploying the cluster to the remote
hosts is performed by executing the deploy_cluster_myCluster.sh script. 

It will install (or freshen) the ElasticSearch installation on every host,
copy configuration files and node management scripts (node_xxx.sh files)
to the configured folders, through SSH.

The cluster management script comes next, its name would be 
*cluster_myCluster.sh*.

It takes one parameter among these:

- *start*: starts the cluster
- *status*: shows if the cluster nodes are up
- *plugins*: installs the head plugin (if the node was defined as needing it)
- *destroy*: should only be called AFTER the cluster has been shut down. 
            Removes all files and folders for the cluster
            
Individual node scripts also handle *stop* and *restart* commands. Currently 
the main script can also forward these commands, but since you should 
always stop the master after all other nodes have been shut down, don't use
it. This is a know defect in this version. A later version will provide a
proper cluster shutdown script.

The easiest and safest way to shut your cluster down is to do it from the *head*
site of your ES cluster (it would make sense to deploy it on the master node(s)).

Finally...
-------------

Create an index and start throwing documents at your ES cluster!

Have fun!