#!/usr/bin/env node
var fs = require('fs');
var path = require('path');
var spawn = require('child_process').spawn;
var java = spawn('java', ['-version']);
var javaVersion="";
var CWD = process.cwd();
var CONFIG_FILE_NAME = 'static-pages.json';
var CONFIG_FILE=path.resolve(CWD, CONFIG_FILE_NAME);
var DEFAULT_CONFIG_FILE = path.resolve(__dirname, 'default.config.json');

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
         fs.stat(CONFIG_FILE, function(err, stats){
            var config;
            if(err){
               switch(err.errno){
               case 34:
                  log(
                     "'"+CONFIG_FILE_NAME+"' wasn't found next to node_modules. "+
                     "Creating it now..."
                  );
                  var boo=fs.readFileSync(DEFAULT_CONFIG_FILE, 'utf-8');
                  fs.writeFileSync(CONFIG_FILE, boo, 'utf-8');
                  break;
               default:
                  log("An unknown error occurred.  Exiting...");
                  log(err);
                  return;
               }
            }
            config = require(CONFIG_FILE);
            startStaticPages(config);
         });
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

function startStaticPages(config){
   var option;
   var args = [ '-jar', 'StaticPages.jar'];
   for(option in config){
      args.push('--'+option);
      switch(option){
      case "project-dir":
      case "new-project":
         args.push(path.resolve(CWD, config[option]));
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
   console.log(a);
}