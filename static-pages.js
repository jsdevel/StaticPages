#!/usr/bin/env node
var fs         = require('fs');
var path       = require('path');
var configTools= require('config-tools');
var spawn      = require('child_process').spawn;
var java = spawn('java', ['-version']);
var javaVersion="";
var CWD = process.cwd();
var CONFIG_FILE_NAME = 'static-pages.json';
var DEFAULT_CONFIG_DIR=path.resolve(CWD, 'config');
var DEFAULT_CONFIG_FILE=path.resolve(DEFAULT_CONFIG_DIR, CONFIG_FILE_NAME);
var CONFIG_FILE_TEMPLATE = path.resolve(__dirname, 'default.config.json');

java.stdout.on('data', function(data){
   javaVersion+=data;
});
java.stderr.on('data', function(data){
   javaVersion+=data;
});
java.on('close', function(code, signal){
   switch(code){
   case 0:
      if(/^java\sversion\s"1.7./.test(javaVersion)){
         configTools.getConfig(
            CONFIG_FILE_NAME,
            function(config){
               startStaticPages(config);
            },
            function(path){
               log([
                  "'config/"+CONFIG_FILE_NAME+"' wasn't found in the CWD, or ",
                  "any parent directory.\n",
                  "CWD was: "+CWD+"\n",
                  "Creating the default config file at: \n",
                  DEFAULT_CONFIG_FILE
               ]);
               var boo=fs.readFileSync(CONFIG_FILE_TEMPLATE, 'utf8');
               fs.writeFileSync(DEFAULT_CONFIG_FILE, boo, 'utf8');
               startStaticPages({
                  dir:DEFAULT_CONFIG_DIR,
                  path:DEFAULT_CONFIG_FILE,
                  config:JSON.parse(boo)
               });
            }
         );
      } else {
         log("Java 1.7 at a minimum is required to run static-pages.  Exiting...");
         return;
      }
      break;
   default:
      log(
         "Java doesn't appear to be in your path using `java -version`."+
         "Please ensure that java is installed on your system prior to "+
         "running static-pages"
      );
   }
});

/**
 * @param {Object} configToolsObject
 */
function startStaticPages(configToolsObject){
   var option;
   var config = configToolsObject.config;
   log("config file found at: ");
   log(configToolsObject.path);
   var args = [ '-jar', path.join(__dirname, 'StaticPages.jar')];
   for(option in config){
      args.push('--'+option);
      switch(option){
      case "project-dir":
      case "new-project":
         args.push(path.resolve(configToolsObject.dir, config[option]));
         break;
      default:
         args.push(config[option]);
         break;
      }
   }
   var java = spawn('java', args, {cwd:__dirname});
   java.stdout.pipe(process.stdout);
   java.stderr.pipe(process.stderr);
}

function log(a){
   var i,len,item;
   if(a instanceof Array){
      len=a.length;
      for(i=0;i<len;i++){
         log(a[i]);
      }
   } else {
      console.log("static-pages: ",a);
   }
}
