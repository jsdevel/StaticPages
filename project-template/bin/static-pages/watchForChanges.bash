#!/bin/bash
#!
# Copyright 2012 Joseph Spencer.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#This script expects buildPages.bash to have been previously sourced.

buildPages
while RESULT=$(inotifywait -qr -e MODIFY --exclude .*\\.swp $PROJECT_DIR)
do
   buildPages
done