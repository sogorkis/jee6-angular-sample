#!/bin/bash

[ $# == 1 ] || { echo "You should pass 1 arg: ssh-key-path"; exit 1; }

SSH_KEY=$1
WAR_NAME=wcsp.war
REMOTE_USER=jboss
REMOTE_HOST=ec2-54-200-187-38.us-west-2.compute.amazonaws.com

echo "Copying war: ${WAR_NAME}"
scp -i ${SSH_KEY} target/${WAR_NAME} ${REMOTE_USER}@${REMOTE_HOST}:~/deploy/wars

echo "Running remote deployment"
ssh -i ${SSH_KEY} ${REMOTE_USER}@${REMOTE_HOST} "cd ~/deploy/wars && ./deploy.sh ${WAR_NAME}"
