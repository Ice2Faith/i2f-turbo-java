/**
 * help use vue2 in multi-page-application website<br/>
 * this could improve coding style & structure<br/>
 * bring out simplify , purify vue code like vue-cli<br/>
 * but also, it has some restrict with it<br/>
 * such as, could not use 'import' and 'export' segments for process other modules<br/>
 * only support use 'export default' to  export default vue component(or instance)<br/>
 * so that, import other modules must by global '<script>' in html<br/>
 * <hr>
 * this is a <b>comp.vue</b> file
 * <pre>
 * <template>
 *   <div class="comp">
 *     {{message}}
 *   </div>
 * </template>
 *
 *
 * <script>
 * export default {
 *   name: "comp",
 *   data(){
 *     return {
 *       message: 'xxx',
 *       timer:null
 *     }
 *   },
 *   mounted(){
 *     this.timer=setInterval(()=>{
 *       this.message=new Date()+''
 *     },1000)
 *   },
 *   destroyed(){
 *     clearInterval(this.timer)
 *   }
 * }
 * </script>
 *
 * <style>
 * .comp{
 *   color: red;
 * }
 * </style>
 * </pre>
 *
 * <hr/>
 * this is a <b>comp.html</b> file
 * <pre>
 * <!DOCTYPE html>
 * <html>
 * <head>
 *     <script src="https://cdn.jsdelivr.net/npm/vue@2/dist/vue.js"></script>
 *     <!-- <script src="./vue@2_dist_vue.js"></script> -->
 *     <title>vue2</title>
 *     <script src="./vue2-loader.js"></script>
 *
 * </head>
 * <body>
 *
 * </body>
 * <script>
 *     Vue2Loader.setupVueApp({
 *         url:'./comp.vue'
 *     })
 *
 * </script>
 * <style>
 *
 * </style>
 * </html>
 * </pre>
 *
 * <hr/>
 * now, you got an vue page <b>comp.html</b>,and could be view it in browser
 *
 * @return {Vue2Loader}
 * @constructor {Vue2Loader}
 */
function Vue2Loader(){

}

/**
 *
 * @type {DOMParser}
 */
Vue2Loader.parser=new DOMParser()

/**
 *
 * @param html {string}
 * @return {Document}
 */
Vue2Loader.parseHtmlDom=function(html){
    return Vue2Loader.parser.parseFromString(html,"text/html")
}

/**
 * use random number as uuid
 * this is uuid-3 implements
 * @return {string}
 */
Vue2Loader.randomUUID=function(){
    let ret=''
    let codes='0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
    for (let i = 0; i < 32; i++) {
        let num=Math.floor(Math.random()*codes.length)
        ret+=codes.charAt(num)
    }
    return ret
}

/**
 *
 * @param html {string}
 * @return {{template: (*|string), varName: string, style: (*|string), script: string}}
 */
Vue2Loader.parseVueTemplate=function(html){
    const doc=Vue2Loader.parseHtmlDom(html)
    let template = doc.querySelector('template')
    if (template) {
        template = template.innerHTML
    } else {
        template = ''
    }
    let script = doc.querySelector('script')
    if (script) {
        script = script.innerHTML
    } else {
        script = ''
    }
    let style = doc.querySelector('style')
    if (style) {
        style = style.innerHTML
    } else {
        style = ''
    }
    let varName='vueComponent_'+Vue2Loader.randomUUID()
    script=script.replace(/export\s+default\s+\{/,'let '+varName+' = {')
    return {
        template: template,
        script: script,
        style: style,
        varName: varName
    }
}

/**
 *
 * @param url {string}
 * @param options {object|null}
 * @return {Promise<Object>}
 */
Vue2Loader.fetchJsonp=function(url,options={}){
    if(!options){
        options={}
    }
    return new Promise((resolve, reject)=>{
        let callbackFunctionName=options.callbackFunctionName || 'jsonp_callback'
        if(options.randomCallbackFunctionName){
            callbackFunctionName=callbackFunctionName+'_'+Vue2Loader.randomUUID()
        }
        window[callbackFunctionName]=(response)=>{
            resolve({
                ok:true,
                json:()=>Promise.resolve(response),
                text:()=>Promise.resolve(response),
            })
        }

        let src=url+''
        if(src.indexOf('?')>=0){
            src+='&'
        }else{
            src+='?'
        }
        src+='jsonp_callback='+callbackFunctionName

        let jsonpScriptId='jsonp_script_'+Vue2Loader.randomUUID()
        let scriptDom=document.createElement('script')
        scriptDom.id=jsonpScriptId
        scriptDom.src=src
        scriptDom.charset='UTF-8'
        scriptDom.nonce=jsonpScriptId
        if(options.referrerPolicy){
            scriptDom.referrerPolicy=options.referrerPolicy // 'same-origin'
        }
        if(options.crossOrigin){
            scriptDom.crossOrigin=options.crossOrigin // 'true'
        }

        scriptDom.onerror=(event, source, lineno, colno, error)=>{
            reject({
                event,
                source,
                lineno,
                colno,
                error
            })
        }
        document.body.append(scriptDom)

        let timeout=options.timeout || -1

        setTimeout(()=>{
            if(timeout>0){
                reject(new Error('fetch jsonp timeout of '+timeout+'!'))
            }
            try {
                scriptDom.remove()
            } catch (e) {
                scriptDom.parentNode.removeChild(scriptDom)
            }

        },timeout>0?timeout:1500)
    })
}

/**
 *
 * @param url {string}
 * @return {Promise<string>}
 */
Vue2Loader.fetchUrl=function(url){
    if(window.location.protocol==='file:'){
        let idx=url.lastIndexOf('?')
        if(idx>=0){
            url=url.substring(0,idx)+'.jsonp.js'+url.substring(idx)
        }else{
            url=url+'.jsonp.js'
        }
        return Vue2Loader.fetchJsonp(url)
            .then(res=>res.text())
    }
    if((typeof fetch)!=='undefined'){
        return fetch(url,{
            mode: 'no-cors'
        }).then(res=>res.text())
    }
    if((typeof  axios)!=='undefined'){
        return axios({
            url: url,
            method: 'get',
            responseType: 'text'
        }).then(res=>res.data)
    }
    throw Error('not found any support fetch tool!')
}

/**
 * mixins=[
 *         {
 *             url: './test.js'
 *         },
 *         './hello.js'
 *     ]
 * @param url {string}
 * @param appId {string|null}
 * @param mixins {string[]|null}
 * @return {Promise<string>}
 */
Vue2Loader.createApp=function(url,appId='app',mixins=[]){
    if(!appId){
        appId='app'
    }
    let mixinVarName='mixins_'+Vue2Loader.randomUUID()
    window[mixinVarName]=[]
    let mixinList=[]
    if(mixins){
        for (let i = 0; i < mixins.length; i++) {
            let item=mixins[i]
            if(!item){
                continue
            }
            if(item.url){
                mixinList.push(Vue2Loader.loadObject(item.url))
            }else{
                mixinList.push(Vue2Loader.loadObject(item))
            }
        }
    }
    return Promise.all(mixinList)
        .then(vueMixins=>{
            for (let i = 0; i < vueMixins.length; i++) {
                let item = vueMixins[i];
                if(item){
                    window[mixinVarName].push(item)
                }
            }
        }).then(()=>{
            return Vue2Loader.fetchUrl(url)
        })
        .then(vueHtml=>{
            const vueTemplate = Vue2Loader.parseVueTemplate(vueHtml);

            let appDom = document.querySelector(`#${appId}`);
            if(appDom){
                appDom.innerHTML=''
            }else{
                appDom = document.createElement('div');
                appDom.id=appId
                document.body.append(appDom)
            }
            appDom.innerHTML=vueTemplate.template


            let styleDom = document.querySelector(`#${appId}Style`);
            if(styleDom){
                styleDom.innerHTML=''
            }else{
                styleDom = document.createElement('style');
                styleDom.id=`${appId}Style`
                document.body.append(styleDom)
            }
            styleDom.innerHTML=vueTemplate.style

            let script=vueTemplate.script
            script+='\n'
            script+='   '+vueTemplate.varName+'.el="#'+appId+'"\n' +
                    '   '+vueTemplate.varName+'.mixins=window.'+mixinVarName+'||[] \n'+
                    '   window.vueApp_'+vueTemplate.varName+'=new Vue('+vueTemplate.varName+')\n'

            let scriptDom = document.querySelector(`#${appId}Script`);
            if(scriptDom){
                scriptDom.innerHTML=''
            }else{
                scriptDom = document.createElement('script');
                scriptDom.id=`${appId}Script`
                document.body.append(scriptDom)
            }
            scriptDom.innerHTML=script

            return new Promise((resolve, reject)=>{
                let spyAppSetupCall=()=>{
                    if(window['vueApp_'+vueTemplate.varName]){
                        resolve(window['vueApp_'+vueTemplate.varName])
                    }else{
                        setTimeout(spyAppSetupCall,30)
                    }
                }
                setTimeout(spyAppSetupCall,30)
            })
        })
}

/**
 *
 * @return {{pagePath: string, pageName: string}}
 */
Vue2Loader.parseCurrentPageInfo=function(){
    let path=window.location.pathname
    let app='index'
    let idx=path.lastIndexOf('/')
    if(idx>=0){
        app=path.substring(idx+1)
        path=path.substring(0,idx)
    }
    idx=app.lastIndexOf(".")
    if(idx>=0){
        app=app.substring(0,idx)
    }
    if(!app || app==''){
        app='index'
    }
    return {
        pagePath: path,
        pageName: app
    }
}

/**
 * @return {void}
 */
Vue2Loader.loadDefaultResources=function(){
    let info = Vue2Loader.parseCurrentPageInfo();
    document.write(
        '<script type="text/javascript" src="'+info.pagePath+'/'+info.pageName+'.js" charset="utf-8"></script>'
    );
    document.write('<link rel="stylesheet" href="'+info.pagePath+'/'+info.pageName+'.css">');

    document.write(
        '<script type="text/javascript" src="'+info.pagePath+'/'+'index.js" charset="utf-8"></script>'
    );
    document.write('<link rel="stylesheet" href="'+info.pagePath+'/'+'index.css">');

}

/**
 * mixins=[
 *         {
 *             url: './test.js'
 *         },
 *         './hello.js'
 *     ]
 * @param appId {string|null}
 * @param mixins {string[]|null}
 * @return {Promise<string>}
 */
Vue2Loader.createDefaultApp = function (appId = 'app', mixins = []) {
    let info = Vue2Loader.parseCurrentPageInfo();
    let fullAppUrl=info.pagePath+'/'+info.pageName+'.vue'
    return Vue2Loader.createApp(fullAppUrl, appId, mixins)
}

/**
 * mixins=[
 *         {
 *             url: './test.js'
 *         },
 *         './hello.js'
 *     ]
 * @param url {string}
 * @param componentName {string}
 * @param mixins {string[]|null}
 * @return {Promise<string>}
 */
Vue2Loader.createComponent=function(url,componentName=null,mixins=[]){
    if(!componentName){
        componentName=url
        let idx=componentName.lastIndexOf('/')
        if(idx>=0){
            componentName=componentName.substring(idx+1)
        }
        idx=componentName.lastIndexOf('.vue')
        if(idx>=0){
            componentName=componentName.substring(0,idx)
        }
    }
    let appId=`component_`+componentName+'_'+Vue2Loader.randomUUID()
    let mixinVarName='mixins_'+Vue2Loader.randomUUID()
    window[mixinVarName]=[]
    let mixinList=[]
    if(mixins){
        for (let i = 0; i < mixins.length; i++) {
            let item=mixins[i]
            if(!item){
                continue
            }
            if(item.url){
                mixinList.push(Vue2Loader.loadObject(item.url))
            }else{
                mixinList.push(Vue2Loader.loadObject(item))
            }
        }
    }
    return Promise.all(mixinList)
        .then(vueMixins=>{
            for (let i = 0; i < vueMixins.length; i++) {
                let item = vueMixins[i];
                if(item){
                    window[mixinVarName].push(item)
                }
            }
        }).then(()=>{
            return Vue2Loader.fetchUrl(url)
        })
        .then(vueHtml=>{
            const vueTemplate = Vue2Loader.parseVueTemplate(vueHtml);

            let appDom = document.querySelector(`#${appId}`);
            if(appDom){
                appDom.innerHTML=''
            }else{
                appDom = document.createElement('div');
                appDom.id=appId
                document.body.append(appDom)
            }
            appDom.innerHTML=vueTemplate.template

            let styleDom = document.querySelector(`#${appId}Style`);
            if(styleDom){
                styleDom.innerHTML=''
            }else{
                styleDom = document.createElement('style');
                styleDom.id=`${appId}Style`
                document.body.append(styleDom)
            }
            styleDom.innerHTML=vueTemplate.style

            let script=vueTemplate.script
            script+='\n'
            script+='let compDom'+vueTemplate.varName+'=document.querySelector("#'+appId+'")\n' +
                '            '+vueTemplate.varName+'.name="'+componentName+'"\n' +
                '            '+vueTemplate.varName+'.mixins=window.'+mixinVarName+'||[] \n'+
                '            '+vueTemplate.varName+'.template=compDom'+vueTemplate.varName+'.innerHTML\n' +
                '            Vue.component("'+componentName+'",'+vueTemplate.varName+')\n'

            let scriptDom = document.querySelector(`#${appId}Script`);
            if(scriptDom){
                scriptDom.innerHTML=''
            }else{
                scriptDom = document.createElement('script');
                scriptDom.id=`${appId}Script`
                document.body.append(scriptDom)
            }
            scriptDom.innerHTML=script

            setTimeout(()=>{
                try {
                    scriptDom.remove()
                } catch (e) {
                    scriptDom.parentNode.removeChild(scriptDom)
                }
                try {
                    appDom.remove()
                } catch (e) {
                    appDom.parentNode.removeChild(appDom)
                }
            })

            return appId
        })
        .catch(error=>{
            return Promise.resolve(appId)
        })

}

/**
 *
 * @param url {string}
 * @return {Promise<Object | null>}
 */
Vue2Loader.loadObject=function(url){
    return Vue2Loader.fetchUrl(url)
        .then(script=>{
            let varName='obj_'+Vue2Loader.randomUUID()
            script=script.replace(/export\s+default\s+\{/,'window.'+varName+' = {')
            let scriptDom = document.querySelector(`#${varName}Script`);
            if(scriptDom){
                scriptDom.innerHTML=''
            }else{
                scriptDom = document.createElement('script');
                scriptDom.id=`${varName}Script`
                document.body.append(scriptDom)
            }
            scriptDom.innerHTML=script
            return new Promise((resolve, reject)=>{
                let spyAppSetupCall=()=>{
                    if(window[varName]){
                        resolve(window[varName])
                    }else{
                        setTimeout(spyAppSetupCall,30)
                    }
                }
                setTimeout(spyAppSetupCall,30)
            })
        }).catch(err=>{
            return Promise.resolve(null)
        })
}

/**
 * config={
 *     url: './app.vue',
 *     appId: 'app',
 *     components:[
 *         {
 *             url: './comp.vue',
 *             name: 'comp'
 *         },
 *         {
 *             url: './test.vue'
 *         },
 *         './hello.vue'
 *     ],
 *     mixins:[
 *         {
 *             url: './test.js'
 *         },
 *         './hello.js'
 *     ]
 * }
 * @param config {object}
 * @return {Promise<string>}
 */
Vue2Loader.setupVueApp=function(config={}){
    let components=[]
    if(config.components){
        for (let i = 0; i < config.components.length; i++) {
            let item=config.components[i]
            if(!item){
                continue
            }
            if(item.url){
                components.push(Vue2Loader.createComponent(item.url,item.name))
            }else{
                components.push(Vue2Loader.createComponent(item))
            }
        }
    }
    return Promise.all(components)
        .then(()=>{
            if(!config.url || config.url==''){
                return Vue2Loader.createDefaultApp(config.appId, config.mixins)
            }else{
                return Vue2Loader.createApp(config.url,config.appId,config.mixins)
            }
        })
}
