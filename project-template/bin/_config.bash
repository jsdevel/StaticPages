#!/bin/bash
#Set environment values in this file.
assetPrefixInBrowser="";
deploy_assetPrefixInBrowser="";
#This needs to be the alias set in your ~/.ssh/config file.  Your alias should
#use identity keys to automate the PROD deploy script.
PROD_SSH_ALIAS="";
#This should be relative to the user's home directory where you'll be uploading
#to.
PROD_PATH_TO_BUILD="";

#You can override the hooks defined in buildPages.bash
function preBuildPages(){
   echo > /dev/null;
}
function postBuildPages(){
   echo > /dev/null;
}