#!/bin/bash
#This script expects buildPages.bash to have been previously sourced.

buildPages
while RESULT=$(inotifywait -qr -e MODIFY --exclude .*\\.swp $PROJECT_DIR)
do
   buildPages
done