#!/bin/bash

[ $# == 1 ] || { echo "You should pass 1 arg: ssh-key-path"; exit 1; }

SSH_KEY=$1
WAR_NAME=wcsp-static-web-1.0-SNAPSHOT.jar
REMOTE_USER=jboss
REMOTE_HOST=ogorkis.net

echo "Copying war: ${WAR_NAME}"
scp -i ${SSH_KEY} target/${WAR_NAME} ${REMOTE_USER}@${REMOTE_HOST}:~/deploy/static

echo "Running remote deployment"
ssh -i ${SSH_KEY} ${REMOTE_USER}@${REMOTE_HOST} "cd ~/deploy/static && ./deploy.sh ${WAR_NAME}"
