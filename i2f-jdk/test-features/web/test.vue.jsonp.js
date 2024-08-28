jsonp_callback('\n'+
'<template>\n'+
'  <div class="app">\n'+
'    {{message}}\n'+
'    <comp></comp>\n'+
'  </div>\n'+
'</template>\n'+
'\n'+
'\n'+
'<script>\n'+
'export default {\n'+
'  name: "test",\n'+
'  data(){\n'+
'    return {\n'+
'      message: "hello"\n'+
'    }\n'+
'  },\n'+
'  created(){\n'+
'    this.alertHello()\n'+
'  }\n'+
'}\n'+
'</script>\n'+
'\n'+
'<style>\n'+
'.app{\n'+
'  color: blue;\n'+
'}\n'+
'</style>\n'+
'\n')
