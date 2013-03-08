!function(){
   var s=sessionStorage;
   var x='CheckForChange_SX';
   var y='CheckForChange_SY';
   var script;
   var body;

   setTimeout(function(){
      if(s[x]){
         window.scrollTo(~~s[x],~~s[y]);
      }
   }, 500);
   setTimeout(function(){
      body=document.body;
      insertScript();

   }, 5000);
   setInterval(function(){
      if(body){
         body.removeChild(script);
         s[x]=window.scrollX;
         s[y]=window.scrollY;
         insertScript();
      }
   }, 2000);
   function insertScript(){
      script = document.createElement('script');
      script.src="refresh.js?"+Date.now();
      body.appendChild(script);
   }
}();