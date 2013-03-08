!function(){
var script;
var body;
setTimeout(function(){
   body=document.body;
   insertScript();
}, 5000);
setInterval(function(){
   if(body){
      body.removeChild(script);
      insertScript();
   }
}, 2000);
   function insertScript(){
      script = document.createElement('script');
      script.src="refresh.js?"+Date.now();
      body.appendChild(script);
   }
}();