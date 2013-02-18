#!/bin/bash

SCRIPT_DIR=$(cd `dirname ${BASH_SOURCE[0]}` && pwd);
SRC_DIR=$(dirname $SCRIPT_DIR)/src;

java -jar $SCRIPT_DIR/BetterJargs.jar --input-xml $SRC_DIR/arguments.xml --output-directory $SRC_DIR/com/spencernetdevelopment/arguments
