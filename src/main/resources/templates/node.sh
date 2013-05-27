#!/bin/bash

PID_FILE="${node.path.pidFile}"

WORK_DIR="$(dirname $PID_FILE)"
if [ ! -d $WORK_DIR ]; then mkdir -p $WORK_DIR; fi

ES_HOME="${node.elasticSearchHome}"

export JAVA_HOME="${node.javaHome}"
#export ES_CLASSPATH=""
export ES_HEAP_SIZE="${node.heapSize}"

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
		curl -XPOST 'http://localhost:${node.http.port}/_cluster/nodes/_local/_shutdown'
		echo -e "\nWaiting for ElasticSearch to stop..."
		sleep ${cluster.shutdown.waitInSeconds}
		if [  -f $PID_FILE  ]; then
			echo -e "ElasticSearch did not stop properly: pid `cat $PID_FILE`"
		fi		
    else
		echo -e "ElasticSearch is not running."
    fi
    return 1
}

cleanup() {
    echo $"Cleaning up..."
    rm -rf ${node.path.work}
    echo $"... done!"
}

status() {
    if [  -f $PID_FILE  ]; then
        echo -e "ElasticSearch is running: pid `cat $PID_FILE`"
    else
	echo -e "ElasticSearch is not running."
    fi
    return 1
}

case "$1" in
 start)
#     cleanup;
     start;
     ;;
 stop)
     stop;
     ;;
 restart)
     stop;
     cleanup;
     start;
     ;;
 status)
    status;
    ;;
 *)
     echo $"Usage: $0 {start|stop|restart|status}"
     exit 1
     ;;
esac


exit $RETVAL
