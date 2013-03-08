#!/bin/bash
BIN_DIR=$(dirname $(readlink -f ${BASH_SOURCE[0]}));
PROJECT_DIR=$(dirname $BIN_DIR);

function buildPages()
{
java -jar $BIN_DIR/StaticPages.jar --project-dir $PROJECT_DIR
}

buildPages
while RESULT=$(inotifywait -qr -e MODIFY --exclude .*\\.swp $PROJECT_DIR)
do
   buildPages
done