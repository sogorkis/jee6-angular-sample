#!/bin/sh

LIB_DIRECTORY=/usr/local/lib/crawljs

echo "Creating directory $LIB_DIRECTORY"
mkdir $LIB_DIRECTORY

echo "Copy config to $LIB_DIRECTORY"
cp crawl.js $LIB_DIRECTORY

echo "Create runnable crawljs"
cp crawljs /usr/local/bin/
sudo chmod +x /usr/local/bin/crawljs
