#!/bin/bash

echo -e "\n################################################################################"
echo -e "Deploying node ${node.id} - ${host.user}@${host.name}"
echo -e "################################################################################\n"

echo -e "Creating directories..."
MKDIRS_SCRIPT="if [ ! -d ${node.scriptsPath} ]; then mkdir -pv ${node.scriptsPath}; fi; \
    if [ ! -d ${node.confPath} ]; then mkdir -pv ${node.confPath}; fi; \
    if [ ! -d ${node.workPath} ]; then mkdir -pv ${node.workPath}; fi; \
    if [ ! -d ${node.dataPath} ]; then mkdir -pv ${node.dataPath}; fi; \
    if [ ! -d ${node.logsPath} ]; then mkdir -pv ${node.logsPath}; fi; \
    if [ ! -d ${node.pluginsPath} ]; then mkdir -pv ${node.pluginsPath}; fi"
ssh ${host.user}@${host.name} "$MKDIRS_SCRIPT"
echo -e "... done!"

echo -e "Copying files..."
scp ${runtime.nodeFolder}/node_*.sh ${host.user}@${host.name}:${node.scriptsPath}
scp ${runtime.nodeFolder}/*.yml ${host.user}@${host.name}:${node.confPath}
echo -e "... done!"