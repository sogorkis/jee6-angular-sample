#!/bin/bash

[ -n "$JBOSS_HOME" ] || { echo "JBOSS_HOME variable not set" ;exit 1; }

MYSQL_DRIVER_DOWNLOAD_URL="http://cdn.mysql.com/Downloads/Connector-J/mysql-connector-java-5.1.26.zip"
MODULE_TEMPLATE_FILE_PATH="$PWD/mysql-module.xml.template"
MYSQL_ARCHIVE_FILE=`basename ${MYSQL_DRIVER_DOWNLOAD_URL}`

cd /tmp
echo "Downloading file: ${MYSQL_DRIVER_DOWNLOAD_URL}"
wget ${MYSQL_DRIVER_DOWNLOAD_URL}

echo "Extracting file: ${MYSQL_ARCHIVE_FILE}"
unzip ${MYSQL_ARCHIVE_FILE}

MODULE_DIRECTORY=${JBOSS_HOME}/modules/com/mysql/jdbc/main

echo "Creating module directory: ${MODULE_DIRECTORY}"
mkdir -p ${MODULE_DIRECTORY}

DRIVER_EXTRACTED_DIR="${MYSQL_ARCHIVE_FILE%.*}"
DRIVER_JAR_FILE_PATH=`ls ${MYSQL_ARCHIVE_FILE%.*}/*.jar`
DRIVER_JAR_FILE_NAME=`basename ${DRIVER_JAR_FILE_PATH}`

echo "Copying ${DRIVER_JAR_FILE_PATH} to ${MODULE_DIRECTORY}"
cp ${DRIVER_JAR_FILE_PATH} ${MODULE_DIRECTORY}

MODULE_FILE_PATH="${MODULE_DIRECTORY}/module.xml"

echo "Creating module file ${MODULE_FILE_PATH}"
cat ${MODULE_TEMPLATE_FILE_PATH} | sed -e "s/\${i}/1/" -e "s/\${mysqlDriverFilename}/${DRIVER_JAR_FILE_NAME}/" > ${MODULE_FILE_PATH}

echo "Cleanup - removing ${DRIVER_EXTRACTED_DIR} ${MYSQL_ARCHIVE_FILE}"
rm -r ${DRIVER_EXTRACTED_DIR} ${MYSQL_ARCHIVE_FILE}
