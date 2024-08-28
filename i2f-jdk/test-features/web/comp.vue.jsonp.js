jsonp_callback('\n'+
'<template>\n'+
'  <div class="comp">\n'+
'    {{message}}\n'+
'  </div>\n'+
'</template>\n'+
'\n'+
'\n'+
'<script>\n'+
'export default {\n'+
'  name: "comp",\n'+
'  data(){\n'+
'    return {\n'+
'      message: "xxx",\n'+
'      timer:null\n'+
'    }\n'+
'  },\n'+
'  mounted(){\n'+
'    this.timer=setInterval(()=>{\n'+
'      this.message=new Date()+""\n'+
'    },1000)\n'+
'  },\n'+
'  destroyed(){\n'+
'    clearInterval(this.timer)\n'+
'  }\n'+
'}\n'+
'</script>\n'+
'\n'+
'<style>\n'+
'.comp{\n'+
'  color: red;\n'+
'}\n'+
'</style>\n'+
'\n'+
'\n')