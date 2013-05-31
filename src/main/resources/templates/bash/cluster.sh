#!/bin/bash

RETVAL=$?

execInAscOrder() {
    ${runtime.clusterOrderAscCommands}
}

execInDescOrder() {
    ${runtime.clusterOrderDescCommands}
}

case "$1" in
 start)
     execInAscOrder start;
     ;;
 stop)
     execInDescOrder stop;
     ;;
 restart)
     execInDescOrder stop;
     execInAscOrder start;
     ;;
 status)
    execInAscOrder status;
    ;;
 destroy)
    execInDescOrder stop;
    execInDescOrder destroy;
    ;;
 plugins)
    execInAscOrder plugins;
    ;;
 *)
     echo $"Usage: $0 {start|stop|restart|status|plugins|destroy}"
     exit 1
     ;;
esac


exit $RETVAL