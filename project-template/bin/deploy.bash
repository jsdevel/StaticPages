#!/bin/bash
#This script should only be sourced from start.bash.
if [ -n "$deploy_assetPrefixInBrowser" ];then
   validAssetPrefixInBrowser="--asset-prefix-in-browser $deploy_assetPrefixInBrowser";
fi

buildPages
#define deploy in config.
deploy