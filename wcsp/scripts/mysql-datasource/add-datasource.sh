#!/bin/bash

[ -n "$JBOSS_HOME" ] || { echo "JBOSS_HOME variable not set" ;exit 1; }

CLIENT_BATCH_TEMPLATE="add-mysql-datasource.cli"
CLIENT_BATCH_FILE="/tmp/add-mysql-datasource.cli"

function seedEscape {
	echo "$1" | sed -e 's/[\/&]/\\&/g'
}

DATASOURCE_NAME=`seedEscape $1`
CONNECTION_URL=`seedEscape $2`
USERNAME=`seedEscape $3`
PASSWORD=`seedEscape $4`

[ $# == 4 ] || { echo "You should pass 4 args: datasourceName connectionUrl username password"; exit 1; }

echo "datasourceName = ${DATASOURCE_NAME}, \
connectionUrl = ${CONNECTION_URL}, \
username = ${USERNAME}, \
password = ${PASSWORD}"

echo "Filling template ${CLIENT_BATCH_FILE}"
cat ${CLIENT_BATCH_TEMPLATE} | sed -e "s/\${datasourceName}/${DATASOURCE_NAME}/" -e "s/\${connectionUrl}/${CONNECTION_URL}/" -e "s/\${username}/${USERNAME}/" -e "s/\${password}/${PASSWORD}/" | sed ':a;N;$!ba;s/\\\n//g' > ${CLIENT_BATCH_FILE}

echo "Creating new DS: ${DATASOURCE_NAME}"
#cat ${CLIENT_BATCH_FILE}
${JBOSS_HOME}/bin/jboss-cli.sh --connect --file=${CLIENT_BATCH_FILE}

echo "Cleanup"
rm ${CLIENT_BATCH_FILE}