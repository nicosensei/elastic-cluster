#!/bin/bash

PID_FILE="${node.scriptsPath}/${node.id}.pid"

ES_HOME="${host.elasticSearchHome}"

export JAVA_HOME="${host.javaHome}"
#export ES_CLASSPATH=""
export ES_HEAP_SIZE="${node.heapSize}"
export ES_JAVA_OPTS="-Des.path.conf=${node.confPath}"

RETVAL=$?

start() {
    if [  -f $PID_FILE  ]; then
		echo -e "ElasticSearch is already running: pid `cat $PID_FILE`"
    else
		$ES_HOME/bin/elasticsearch -p $PID_FILE
		echo -e "Started ElasticSearch: pid `cat $PID_FILE`"
	fi
    return 1
}

stop() {
    if [  -f $PID_FILE  ]; then
		curl -XPOST 'http://localhost:${node.httpPort}/_cluster/nodes/_local/_shutdown'
		echo -e "\nWaiting for ElasticSearch to stop..."
		sleep ${cluster.shutdownWaitInSeconds}
		if [  -f $PID_FILE  ]; then
			echo -e "ElasticSearch did not stop properly: pid `cat $PID_FILE`"
		fi		
    else
		echo -e "ElasticSearch is not running."
    fi
    return 1
}

status() {
    if [  -f $PID_FILE  ]; then
        echo -e "ElasticSearch is running: pid `cat $PID_FILE`"
    else
	echo -e "ElasticSearch is not running."
    fi
    return 1
}

plugins() {
    if ${node.installHeadPlugin} ; then
        if [ -d ${node.pluginsPath}/head ]; 
	  then rm -rf ${node.pluginsPath}/head
          $ES_HOME/bin/plugin -remove head 
        fi
        $ES_HOME/bin/plugin -install mobz/elasticsearch-head
        mv $ES_HOME/plugins/head ${node.pluginsPath}
    fi    
}

destroy() {
    rm -rvf ${node.confPath}
    rm -rvf ${node.workPath}
    rm -rvf ${node.dataPath}
    rm -rvf ${node.logsPath}
    rm -rvf ${node.pluginsPath}
    rm -rvf ${node.scriptsPath}
}

echo -e "\n################################################################################"
echo -e "Executing command '"$1"' on node ${node.id} (${host.user}@${host.name})"
echo -e "################################################################################\n"

case "$1" in
 start)
     start;
     ;;
 stop)
     stop;
     ;;
 restart)
     stop;
     start;
     ;;
 status)
    status;
    ;;
 destroy)
    stop;
    destroy;
    ;;
 plugins)
    plugins;
    ;;
 *)
     echo $"Usage: $0 {start|stop|restart|status|plugins|destroy}"
     exit 1
     ;;
esac


exit $RETVAL
