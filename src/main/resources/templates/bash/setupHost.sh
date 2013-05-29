#!/bin/bash

echo -e "\n################################################################################"
echo -e "Setting up deployment host ${host.user}@${host.name}"
echo -e "################################################################################\n"

echo -e "Creating directories..."
MKDIRS_SCRIPT="if [ -d ${host.elasticSearchHome} ]; then \
    echo Will wipe contents of ${host.elasticSearchHome} ; \
    rm -rf ${host.elasticSearchHome}/*; \
    else \
    mkdir -pv ${host.elasticSearchHome}; \
    fi"
ssh ${host.user}@${host.name} "$MKDIRS_SCRIPT"
echo -e "... done!"

echo -e "Copying files..."
scp ${cluster.esZipFile} ${host.user}@${host.name}:${host.tmpPath}
echo -e "... done!"

echo -e "Unpacking ES package and moving files to the configured location..."
UNPACK_CMDS="unzip -oq ${host.tmpPath}/${cluster.esVersionName}.zip -d ${host.elasticSearchHome}; \
    mv ${host.elasticSearchHome}/${cluster.esVersionName}/* ${host.elasticSearchHome}; \
    rm -rf ${host.elasticSearchHome}/${cluster.esVersionName} \
    rm -rf ${host.tmpPath}/${cluster.esVersionName}.zip"
ssh ${host.user}@${host.name} "$UNPACK_CMDS" 
echo -e "... done!"