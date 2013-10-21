#!/bin/bash

[ -n "$JBOSS_HOME" ] || { echo "JBOSS_HOME variable not set" ;exit 1; }

CLIENT_BATCH_TEMPLATE="add-database-security-domain.cli"
CLIENT_BATCH_FILE="/tmp/add-database-security-domain.cli"

function seedEscape {
	echo "$1" | sed -e 's/[\/&]/\\&/g'
}

DATASOURCE_NAME=`seedEscape $1`

[ $# == 1 ] || { echo "You should pass 1 args: datasourceName"; exit 1; }

echo "datasourceName = ${DATASOURCE_NAME}"

echo "Filling template ${CLIENT_BATCH_FILE}"
cat ${CLIENT_BATCH_TEMPLATE} | sed -e "s/\${datasourceName}/${DATASOURCE_NAME}/" | sed ':a;N;$!ba;s/\\\n//g' > ${CLIENT_BATCH_FILE}

echo "Creating security-domain with datasource: ${DATASOURCE_NAME}"
${JBOSS_HOME}/bin/jboss-cli.sh --connect --file=${CLIENT_BATCH_FILE}

echo "Cleanup"
rm ${CLIENT_BATCH_FILE}