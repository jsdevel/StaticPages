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
         findConfig(CWD, CONFIG_FILE_NAME,
            function(configPath){
               startStaticPages(configPath);
            },
            function(){
               log(
                  "'"+CONFIG_FILE_NAME+"' wasn't found in the CWD, or any of it's"+
                  " parent directories.\nCWD was: "+CWD+"\n"+
                  "Creating it now at: "+CONFIG_FILE
               );
               var boo=fs.readFileSync(DEFAULT_CONFIG_FILE, 'utf-8');
               fs.writeFileSync(CONFIG_FILE, boo, 'utf-8');
               startStaticPages(CONFIG_FILE);
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
 * Scans a directory upwards through all ancestors searching for a file.
 *
 * @param {string} baseDir A path to a directory to begin searching for the file.
 * @param {string} fileName The name of the file to search for.
 * @param {function(string)} fnFound Accepts the absolute path of the file
 * searched for.
 * @param {function()} fnNotFound
 * @param {number} timesCalled When this exceeds 50, fnNotFound is called
 */
function findConfig(baseDir, fileName, fnFound, fnNotFound, timesCalled){
   var pathToConfig = path.join(baseDir, fileName);
   fs.stat(pathToConfig, function(err, stats){
      var i = (typeof timesCalled === 'number') ? timesCalled + 1 : 0;
      if(err){
         switch(err.errno){
         case 34:
            if(i > 50){
               log("Couldn't find '"+fileName+"' in any parent directory"+
               " starting in '"+CWD+"'");
               fnNotFound();
               return;
            }
            findConfig(path.dirname(baseDir), fileName, fnFound, fnNotFound, i);
            break;
         default:
            log("The following error occurred while trying to find: "+
            path.join(baseDir, fileName)+".  Exiting...");
            log(err);
            return;
         }
      } else {
         fnFound(pathToConfig);
      }
   });
}

function startStaticPages(configPath){
   var config = require(configPath);
   var option;
   var args = [ '-jar', path.join(__dirname, 'StaticPages.jar')];
   for(option in config){
      args.push('--'+option);
      switch(option){
      case "project-dir":
      case "new-project":
         args.push(path.resolve(path.dirname(configPath), config[option]));
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
