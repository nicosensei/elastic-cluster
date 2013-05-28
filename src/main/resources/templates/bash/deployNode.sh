#!/bin/bash

echo -e "################################################################################"
echo -e "Deploying node ${node.id} - ${node.user}@${node.host}"
echo -e "################################################################################"

echo -e "Creating directories..."
MKDIRS_SCRIPT="if [ ! -d ${node.scriptsPath} ]; then mkdir -pv ${node.scriptsPath}; fi; \
    if [ ! -d ${node.elasticSearchHome} ]; then mkdir -pv ${node.elasticSearchHome}; fi; \
    if [ ! -d ${node.confPath} ]; then mkdir -pv ${node.confPath}; fi; \
    if [ ! -d ${node.workPath} ]; then mkdir -pv ${node.workPath}; fi; \
    if [ ! -d ${node.dataPath} ]; then mkdir -pv ${node.dataPath}; fi; \
    if [ ! -d ${node.logsPath} ]; then mkdir -pv ${node.logsPath}; fi; \
    if [ ! -d ${node.pluginsPath} ]; then mkdir -pv ${node.pluginsPath}; fi"
ssh ${node.user}@${node.host} "$MKDIRS_SCRIPT"

echo -e "... done!"

echo -e "Copying files..."
scp ${deploy.esZipFile} ${node.user}@${node.host}:${node.elasticSearchHome}
scp ${deploy.nodeFolder}/node_*.sh ${node.user}@${node.host}:${node.scriptsPath}
scp ${deploy.nodeFolder}/*.yml ${node.user}@${node.host}:${node.confPath}
echo -e "... done!"

echo -e "Unpacking ES package and moving files to the configured location..."
UNPACK_CMDS="unzip ${node.elasticSearchHome}/${deploy.esVersionName}.zip -d ${node.elasticSearchHome}; \
    mv -v ${node.elasticSearchHome}/${deploy.esVersionName}/* ${node.elasticSearchHome}; \
    rm -vf ${node.elasticSearchHome}/${deploy.esVersionName}"
ssh ${node.user}@${node.host} "$UNPACK_CMDS" 

echo -e "... done!"