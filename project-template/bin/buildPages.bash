#!/bin/bash
BIN_DIR=$(dirname $(readlink -f ${BASH_SOURCE[0]}));
PROJECT_DIR=$(dirname $BIN_DIR);
BUILD_DIR=$PROJECT_DIR/build;
REFRESH_FILE=$BUILD_DIR/refresh.js;

function buildPages()
{
   clear
   java -jar $BIN_DIR/StaticPages.jar --project-dir $PROJECT_DIR
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

buildPages
while RESULT=$(inotifywait -qr -e MODIFY --exclude .*\\.swp $PROJECT_DIR)
do
   buildPages
done