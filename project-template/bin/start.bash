#!/bin/bash
BIN_DIR=$(dirname $(readlink -f ${BASH_SOURCE[0]}));
PROJECT_DIR=$(dirname $BIN_DIR);
BUILD_DIR=$PROJECT_DIR/build;
REFRESH_FILE=$BUILD_DIR/refresh.js;

#override these in config
function preBuildPages(){
   echo > /dev/null;
}
function postBuildPages(){
   echo > /dev/null;
}
#handle config.bash
if [ -f $BIN_DIR/config.bash ];then
   . $BIN_DIR/config.bash;
   if [ "$assetPrefixInBrowser" != "" ];then
      validAssetPrefixInBrowser="--asset-prefix-in-browser $assetPrefixInBrowser";
   fi
fi

. $BIN_DIR/buildPages.bash;

case $1 in
   watchForChanges.bash)
      . $BIN_DIR/watchForChanges.bash;;
   deploy.bash)
      . $BIN_DIR/deploy.bash;;
   *)
      cat <<!

Unknown script: $1
Valid scripts are:
watchForChanges.bash
deploy.bash

!
      exit 1;
esac