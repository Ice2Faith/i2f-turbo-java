/**
 * 帮助在多页面html中使用VUE2进行开发
 * 这能够改善一些使用上的方式
 * 带来一些简化的便利
 * 但是，同样的也有一些约束
 * 比如，不能使用import和export语句进行导入导出其他模块
 * 仅支持使用export default 导出默认的VUE组件
 * 所以，你如果需要进行import其他模块，则需要在html中全局引入方式实现
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
 *
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
    let template=doc.querySelector('template')?.innerHTML||''
    let script=doc.querySelector('script')?.innerHTML||''
    let style=doc.querySelector('style')?.innerHTML||''
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
 * @param url
 * @return {Promise<string>}
 */
Vue2Loader.fetchUrl=function(url){
    if((typeof fetch)!=='undefined'){
        return fetch(url).then(res=>res.text())
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
 *
 * @param url {string}
 * @param appId {string|null}
 * @return {Promise<string>}
 */
Vue2Loader.createApp=function(url,appId='app'){
    if(!appId){
        appId='app'
    }
    return Vue2Loader.fetchUrl(url)
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
 *
 * @param appId {string|null}
 * @return {Promise<string>}
 */
Vue2Loader.createDefaultApp=function(appId='app'){
    let info = Vue2Loader.parseCurrentPageInfo();
    let fullAppUrl=info.pagePath+'/'+info.pageName+'.vue'
    return Vue2Loader.createApp(fullAppUrl,appId)
}

/**
 *
 * @param url {string}
 * @param componentName {string}
 * @return {Promise<string>}
 */
Vue2Loader.createComponent=function(url,componentName=null){
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
    return Vue2Loader.fetchUrl(url)
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
                scriptDom.remove()
                appDom.remove()
            })

            return appId
        })
        .catch(error=>{
            return Promise.resolve(appId)
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
                return Vue2Loader.createDefaultApp(config.appId)
            }else{
                return Vue2Loader.createApp(config.url,config.appId)
            }
        })
}
