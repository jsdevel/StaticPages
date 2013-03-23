#!/bin/bash
#Set environment values in this file.
assetPrefixInBrowser="";
deploy_assetPrefixInBrowser="";

#You can override the hooks defined in buildPages.bash
function preBuildPages(){
   echo > /dev/null;
}
function postBuildPages(){
   echo > /dev/null;
}