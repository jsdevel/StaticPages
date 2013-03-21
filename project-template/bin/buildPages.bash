#!/bin/bash
#This script expects start.bash to have been previously sourced.

function buildPages()
{
   clear
   java -jar $BIN_DIR/StaticPages.jar --project-dir $PROJECT_DIR $validAssetPrefixInBrowser;
   local key="sessionStorage['lastRefresh']";
   local stamp="`date +%N`";
   cat > $REFRESH_FILE << HERE
if($key && $key != $stamp){
   $key = $stamp;
   window.location=location.href.indexOf('?') > -1?
      location.href.replace(/(StaticPageRefresh=)[0-9]+/, '\$1$stamp'):
      location.href+'?StaticPageRefresh=$stamp';
} else {
   $key = $stamp;
}
HERE
}

