#!/bin/bash
PROJECT_DIR=$(dirname $(readlink -f ${BASH_SOURCE[0]}));

rm -rf $PROJECT_DIR/project-template $PROJECT_DIR/StaticPages.jar $PROJECT_DIR/lib;

cp -r $PROJECT_DIR/dist/lib $PROJECT_DIR;
cp -r $PROJECT_DIR/dist/project-template $PROJECT_DIR;
cp -r $PROJECT_DIR/dist/StaticPages.jar $PROJECT_DIR;
