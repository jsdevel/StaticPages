#!/bin/bash
BIN_DIR=$(`cd $(dirname ${BASH_SOURCE[0]})` && pwd);
PROJECT_DIR=$(dirname $BIN_DIR);

java -jar $BIN_DIR/StaticPages.jar --project-dir $PROJECT_DIR
