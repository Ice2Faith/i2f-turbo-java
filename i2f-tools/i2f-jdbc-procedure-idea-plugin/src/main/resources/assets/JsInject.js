/**
 *
 * @constructor {JavaMap}
 * @return {JavaMap}
 * @return {JavaMap}
 */
function JavaMap(){

}

/**
 *
 * @param key {string}
 * @param value {Object}
 * @return {void}
 */
JavaMap.prototype.put=function(key,value){

}

/**
 *
 * @param key {string}
 * @return {Object}
 */
JavaMap.prototype.get=function(key){

}

/**
 *
 * @param key {string}
 * @return {Object}
 */
JavaMap.prototype.remove=function(key){

}

/**
 *
 * @param key {string}
 * @return {boolean}
 */
JavaMap.prototype.containsKey=function(key){

}

///////////////////////////////////////////////////////////////////////////////////////////////////

/**
 *
 * @constructor {JdbcProcedureContextRefreshListener}
 * @type {JdbcProcedureContextRefreshListener}
 * @return {JdbcProcedureContextRefreshListener}
 */
function JdbcProcedureContextRefreshListener(){

}

/**
 *
 * @param context {JdbcProcedureContext}
 * @return {void}
 */
JdbcProcedureContextRefreshListener.prototype.accept=function(context){

}

///////////////////////////////////////////////////////////////////////////////////////////////////

/**
 *
 * @constructor {ProcedureMeta}
 * @type {ProcedureMeta}
 * @return {ProcedureMeta}
 */
function ProcedureMeta(){

}

/**
 * @return {string}
 */
ProcedureMeta.prototype.getType=function(){

}

/**
 *
 * @param type {string}
 * @return {void}
 */
ProcedureMeta.prototype.setType=function(type){

}

/**
 * @return {string}
 */
ProcedureMeta.prototype.getName=function(){

}

/**
 *
 * @param name {string}
 * @return {void}
 */
ProcedureMeta.prototype.setName=function(name){

}

/**
 * @return {Object}
 */
ProcedureMeta.prototype.getTarget=function(){

}

/**
 * @param target {Object}
 * @return {void}
 */
ProcedureMeta.prototype.setTarget=function(target){

}

/**
 * @return {Array<String>}
 */
ProcedureMeta.prototype.getArguments=function(){

}

/**
 *
 * @param arguments {Array<String>}
 * @return {void}
 */
ProcedureMeta.prototype.setArguments=function(arguments){

}

/**
 * @return {JavaMap<String,Array<String>>}
 */
ProcedureMeta.prototype.getArgumentFeatures=function(){

}

/**
 * @param argumentFeatures {JavaMap<String,Array<String>>}
 * @return {void}
 */
ProcedureMeta.prototype.getArgumentFeatures=function(argumentFeatures){

}

///////////////////////////////////////////////////////////////////////////////////////////////////

/**
 *
 * @constructor {XmlNode}
 * @type {XmlNode}
 * @return {XmlNode}
 */
function XmlNode(){

}

/**
 * @return {string}
 */
XmlNode.prototype.getNodeType=function(){

}

/**
 * @param nodeType {string}
 * @return {void}
 */
XmlNode.prototype.setNodeType=function(nodeType){

}

/**
 * @return {string}
 */
XmlNode.prototype.getTagName=function(){

}

/**
 * @param tagName {string}
 * @return {void}
 */
XmlNode.prototype.setTagName=function(tagName){

}

/**
 * @return {JavaMap<String,String>}
 */
XmlNode.prototype.getTagAttrMap=function(){

}

/**
 * @param tagAttrMap {JavaMap<String,String>}
 * @return {void}
 */
XmlNode.prototype.setTagAttrMap=function(tagAttrMap){

}

/**
 * @return {JavaMap<String,Array<String>>}
 */
XmlNode.prototype.getAttrFeatureMap=function(){

}

/**
 * @param attrFeatureMap {JavaMap<String,Array<String>>}
 * @return {void}
 */
XmlNode.prototype.setAttrFeatureMap=function(attrFeatureMap){

}

/**
 * @return {string}
 */
XmlNode.prototype.getTagBody=function(){

}

/**
 * @param tagBody {string}
 * @return {void}
 */
XmlNode.prototype.setTagBody=function(tagBody){

}

/**
 * @return {string}
 */
XmlNode.prototype.getTextBody=function(){

}

/**
 * @param textBody {string}
 * @return {void}
 */
XmlNode.prototype.setTextBody=function(textBody){

}

/**
 * @return {Array<XmlNode>}
 */
XmlNode.prototype.getChildren=function(){

}

/**
 * @param children {Array<XmlNode>}
 * @return {void}
 */
XmlNode.prototype.setChildren=function(children){

}

/**
 * @return {string}
 */
XmlNode.prototype.getLocationFile=function(){

}

/**
 * @param locationFile {string}
 * @return {void}
 */
XmlNode.prototype.setLocationFile=function(locationFile){

}

/**
 * @return {int}
 */
XmlNode.prototype.getLocationLineNumber=function(){

}

/**
 * @param locationLineNumber {int}
 * @return {void}
 */
XmlNode.prototype.setLocationLineNumber=function(locationLineNumber){

}

///////////////////////////////////////////////////////////////////////////////////////////////////

/**
 *
 * @constructor {JdbcProcedureJavaCaller}
 * @type {JdbcProcedureJavaCaller}
 * @return {JdbcProcedureJavaCaller}
 */
function JdbcProcedureJavaCaller(){

}

/**
 * @return {Reference<?>}
 */
JdbcProcedureJavaCaller.prototype.nop=function(){

}

/**
 * @param obj {Object}
 * @return {boolean}
 */
JdbcProcedureJavaCaller.prototype.isNop=function(obj){

}

/**
 *
 * @param executor {JdbcProcedureExecutor}
 * @param params {JavaMap<String,Object>}
 * @return {Object}
 */
JdbcProcedureJavaCaller.prototype.exec=function(executor,params){

}

///////////////////////////////////////////////////////////////////////////////////////////////////

/**
 *
 * @constructor {JdbcProcedureContext}
 * @type {JdbcProcedureContext}
 * @return {JdbcProcedureContext}
 */
function JdbcProcedureContext(){

}

/**
 *
 * @param listener {JdbcProcedureContextRefreshListener}
 * @return {void}
 */
JdbcProcedureContext.prototype.listener=function(listener){

}

/**
 * @return {JavaMap<String,ProcedureMeta>}
 */
JdbcProcedureContext.prototype.getMetaMap=function(){

}

/**
 *
 * @param name {string}
 * @param meta {ProcedureMeta}
 * @return {void}
 */
JdbcProcedureContext.prototype.registry=function(name,meta){

}


/**
 *
 * @param name {string}
 * @return {void}
 */
JdbcProcedureContext.prototype.remove=function(name){

}

/**
 *
 * @param map {JavaMap<String,ProcedureMeta>}
 * @return {void}
 */
JdbcProcedureContext.prototype.registry=function(map){

}

/**
 *
 * @param list {Array<ProcedureMeta>}
 * @return {void}
 */
JdbcProcedureContext.prototype.registry=function(list){

}

/**
 *
 * @param args {ProcedureMeta}
 * @return {void}
 */
JdbcProcedureContext.prototype.registry=function(...args){

}

/**
 *
 * @param meta {ProcedureMeta}
 * @return {void}
 */
JdbcProcedureContext.prototype.registry=function(meta){

}


/**
 *
 * @param key {string}
 * @param value {XmlNode}
 * @return {void}
 */
JdbcProcedureContext.prototype.registry=function(key,value){

}

/**
 *
 * @param key {string}
 * @param value {XmlNode}
 * @return {void}
 */
JdbcProcedureContext.prototype.registry=function(key,value){

}

///////////////////////////////////////////////////////////////////////////////////////////////////

/**
 *
 * @constructor {Consumer}
 * @type {Consumer}
 * @return {Consumer}
 */
function Consumer(){

}

/**
 *
 * @param obj {Object}
 * @return {void}
 */
Consumer.prototype.accept=function(obj){

}

///////////////////////////////////////////////////////////////////////////////////////////////////

/**
 *
 * @constructor {ExecutorNode}
 * @type {ExecutorNode}
 * @return {ExecutorNode}
 */
function ExecutorNode(){

}

/**
 *
 * @param node {XmlNode}
 * @return {boolean}
 */
ExecutorNode.prototype.support=function(node){

}

/**
 *
 * @param node {XmlNode}
 * @param warnPoster {Consumer<String>}
 * @return {void}
 */
ExecutorNode.prototype.reportGrammar=function (node,warnPoster){

}

/**
 *
 * @param node {XmlNode}
 * @param context {JavaMap<String,Object>}
 * @param executor {JdbcProcedureExecutor}
 * @return {void}
 */
ExecutorNode.prototype.exec=function(node,context,executor){

}

///////////////////////////////////////////////////////////////////////////////////////////////////

/**
 *
 * @constructor {MapBuilder}
 * @type {MapBuilder}
 * @return {MapBuilder}
 */
function MapBuilder(){

}

/**
 * @return {JavaMap<String,Object>}
 */
MapBuilder.prototype.get=function(){

}

/**
 *
 * @param key {string}
 * @param value {Object}
 * @return {MapBuilder<String,Object>}
 */
MapBuilder.prototype.put=function(key,value){

}

///////////////////////////////////////////////////////////////////////////////////////////////////
/**
 *
 * @constructor {IEnvironment}
 * @type {IEnvironment}
 * @return {IEnvironment}
 */
function IEnvironment(){

}

/**
 *
 * @param name {string}
 * @param defVal {string}
 * @return {string}
 */
IEnvironment.prototype.getProperty=function(name,defVal){

}

/**
 * @return {JavaMap<String,String>}
 */
IEnvironment.prototype.getAllProperties=function(){

}

/**
 *
 * @param name {string}
 * @param defVal {Integer}
 */
IEnvironment.prototype.getInteger=function(name,defVal){

}

/**
 *
 * @param name {string}
 * @param defVal {Long}
 */
IEnvironment.prototype.getLong=function(name,defVal){

}

/**
 *
 * @param name {string}
 * @param defVal {Boolean}
 */
IEnvironment.prototype.getBoolean=function(name,defVal){

}

/**
 *
 * @param name {string}
 * @param defVal {Double}
 */
IEnvironment.prototype.getDouble=function(name,defVal){

}

///////////////////////////////////////////////////////////////////////////////////////////////////

/**
 *
 * @constructor {INamingContext}
 * @type {INamingContext}
 * @return {INamingContext}
 */
function INamingContext(){

}

/**
 *
 * @param name {string|Class<?>}
 * @return {Object}
 */
INamingContext.prototype.getBean=function(name){

}

/**
 *
 * @param clazz {Class<?>}
 * @return {Array<Object>}
 */
INamingContext.prototype.getBeans=function(clazz){

}

/**
 * @return {Array<Object>}
 */
INamingContext.prototype.getAllBeans=function(){

}

/**
 *
 * @param clazz {Class<?>}
 * @return {JavaMap<String,Object>}
 */
INamingContext.prototype.getBeansMap=function(clazz){

}

/**
 * @return {JavaMap<String,Object>}
 */
INamingContext.prototype.getAllBeansMap=function(){

}



///////////////////////////////////////////////////////////////////////////////////////////////////

/**
 *
 * @constructor {JdbcProcedureExecutor}
 * @type {JdbcProcedureExecutor}
 * @return {JdbcProcedureExecutor}
 */
function JdbcProcedureExecutor(){

}


/**
 * @return {Reference<?>}
 */
JdbcProcedureExecutor.prototype.nop=function(){

}

/**
 *
 * @param obj {Object}
 * @return {boolean}
 */
JdbcProcedureExecutor.prototype.isNop=function(obj){

}

/**
 * @return {JdbcProcedureContext}
 */
JdbcProcedureExecutor.prototype.getContext=function(){

}

/**
 * @return {JavaMap<String,ProcedureMeta>}
 */
JdbcProcedureExecutor.prototype.getMetaMap=function(){

}

/**
 *
 * @param procedureId {string}
 * @return {ProcedureMeta}
 */
JdbcProcedureExecutor.prototype.getMeta=function(procedureId){

}

/**
 * @return {ExecutorNode}
 */
JdbcProcedureExecutor.prototype.getNodes=function(){

}

/**
 * @return {IEnvironment}
 */
JdbcProcedureExecutor.prototype.getEnvironment=function(){

}

/**
 *
 * @param environment {IEnvironment}
 * @retrn {void}
 */
JdbcProcedureExecutor.prototype.setEnvironment=function(environment){

}

/**
 *
 * @param property {string}
 * @param defVal {string}
 * @return {string}
 */
JdbcProcedureExecutor.prototype.env=function(property,defVal){

}

/**
 *
 * @param property {string}
 * @param targetType {Class<?>}
 * @param defVal {Object}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.envAs=function(property,targetType,defVal){

}

/**
 * @return {INamingContext}
 */
JdbcProcedureExecutor.prototype.getNamingContext=function(){

}

/**
 *
 * @param context {INamingContext}
 * @return {void}
 */
JdbcProcedureExecutor.prototype.setNamingContext=function(context){

}


/**
 *
 * @param name {String|Class<?>}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.getBean=function(name){

}

/**
 *
 * @param obj {Object}
 * @param type {Class<?>}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.convertAs=function (obj,type){

}

/**
 * @return {MapBuilder<String,Object>}
 */
JdbcProcedureExecutor.prototype.mapBuilder=function(){

}

/**
 *
 * @param procedureId {string}
 * @param consumer {Consumer<MapBuilder<String,Object>>}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.invoke=function(procedureId,consumer){

}

/**
 *
 * @param procedureId {string}
 * @param params {JavaMap<String,Object>}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.invoke=function(procedureId,params){

}

/**
 *
 * @param procedureId {string}
 * @param args {Object}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.invoke=function(procedureId,...args){

}

/**
 *
 * @param procedureId {string}
 * @param args {Array<Object>}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.invoke=function(procedureId,args){

}

/**
 *
 * @param procedureId {string}
 * @param consumer {Consumer<MapBuilder<String,Object>>}
 * @return {JavaMap<String,Object>}
 */
JdbcProcedureExecutor.prototype.call=function(procedureId,consumer){

}

/**
 *
 * @param procedureId {string}
 * @param params {JavaMap<String,Object>}
 * @return {JavaMap<String,Object>}
 */
JdbcProcedureExecutor.prototype.call=function(procedureId,params){

}

/**
 *
 * @param procedureId {string}
 * @param args {Object}
 * @return {JavaMap<String,Object>}
 */
JdbcProcedureExecutor.prototype.call=function(procedureId,...args){

}

/**
 *
 * @param procedureId {string}
 * @param args {Array<Object>}
 * @return {JavaMap<String,Object>}
 */
JdbcProcedureExecutor.prototype.call=function(procedureId,args){

}

/**
 *
 * @param procedureId {string}
 * @param params {JavaMap<String,Object>}
 * @return {JavaMap<String,Object>}
 */
JdbcProcedureExecutor.prototype.exec=function(procedureId,params){

}

/**
 *
 * @param params {JavaMap<String,Object>}
 * @return {JavaMap<String,Object>}
 */
JdbcProcedureExecutor.prototype.prepareParams=function(params){

}

/**
 *
 * @param node {XmlNode}
 * @param params {JavaMap<String,Object>}
 * @return {JavaMap<String,Object>}
 */
JdbcProcedureExecutor.prototype.exec=function(node,params){

}

/**
 *
 * @param node {XmlNode}
 * @param params {JavaMap<String,Object>}
 * @param beforeNewConnection {boolean}
 * @param afterCloseConnection {boolean}
 * @return {JavaMap<String,Object>}
 */
JdbcProcedureExecutor.prototype.exec=function(node,params,beforeNewConnection,afterCloseConnection){

}

/**
 *
 * @param procedureId {string}
 * @param params {JavaMap<String,Object>}
 * @return {JavaMap<String,Object>}
 */
JdbcProcedureExecutor.prototype.execAsProcedure=function(procedureId,params){

}

/**
 *
 * @param node {XmlNode}
 * @param params {JavaMap<String,Object>}
 * @return {JavaMap<String,Object>}
 */
JdbcProcedureExecutor.prototype.execAsProcedure=function(node,params){

}

/**
 *
 * @param node {XmlNode}
 * @param params {JavaMap<String,Object>}
 * @param beforeNewConnection {boolean}
 * @param afterCloseConnection {boolean}
 * @return {JavaMap<String,Object>}
 */
JdbcProcedureExecutor.prototype.execAsProcedure=function(node,params,beforeNewConnection,afterCloseConnection){

}

/**
 *
 * @param attr {string}
 * @param action {string}
 * @param node {XmlNode}
 * @param params {JavaMap<String,Object>}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.attrValue=function (attr,action,node,params){

}

/**
 *
 * @param value {Object}
 * @param features {Array<String>}
 * @param node {XmlNode}
 * @param params {JavaMap<String,Object>}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.resultValue=function(value,features,node,params){

}

/**
 *
 * @param params {JavaMap<String,Object>}
 * @param result {string}
 * @param value {Object}
 */
JdbcProcedureExecutor.prototype.visitSet=function(params,result,value){

}

/**
 *
 * @param params {JavaMap<String,Object>}
 * @param result {string}
 */
JdbcProcedureExecutor.prototype.visitDelete=function(params,result){

}

/**
 * @return {JavaMap<String,Object>}
 */
JdbcProcedureExecutor.prototype.createParams=function(){

}

/**
 *
 * @param params {JavaMap<String,Object>}
 * @return {JavaMap<String,Object>}
 */
JdbcProcedureExecutor.prototype.newParams=function(params){

}

/**
 *
 * @param enable {boolean}
 * @return {void}
 */
JdbcProcedureExecutor.prototype.debug=function(enable){

}

/**
 * @return {boolean}
 */
JdbcProcedureExecutor.prototype.isDebug=function(){

}

/**
 *
 * @param tag {string}
 * @param context {Object}
 * @param conditionExpression {string}
 * @return {void}
 */
JdbcProcedureExecutor.prototype.openDebugger=function(tag,context,conditionExpression){

}

/**
 *
 * @param className {string}
 * @return {Class<?>}
 */
JdbcProcedureExecutor.prototype.loadClass=function(className){

}

/**
 *
 * @param test {string}
 * @param params {Object}
 * @return {boolean}
 */
JdbcProcedureExecutor.prototype.test=function(test,params){

}

/**
 *
 * @param script {string}
 * @param params {Object}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.evalAs=function(script,params){

}

/**
 *
 * @param script {string}
 * @param params {Object}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.eval=function(script,params){

}

/**
 *
 * @param script {string}
 * @param params {Object}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.visitAs=function (script,params){

}

/**
 *
 * @param script {string}
 * @param params {Object}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.visit=function (script,params){

}

/**
 *
 * @param script {string}
 * @param params {Object}
 * @return {string}
 */
JdbcProcedureExecutor.prototype.render=function (script,params){

}

/**
 *
 * @param datasource {string}
 * @param params {JavaMap<String,Object>}
 * @return {Connection}
 */
JdbcProcedureExecutor.prototype.getConnection=function(datasource,params){

}

/**
 *
 * @param datasource {string}
 * @param bql {BindSql}
 * @param params {JavaMap<String,Object>}
 * @param resultType {Class<?>}
 * @return {Array<?>}
 */
JdbcProcedureExecutor.prototype.sqlQueryList=function(datasource,bql,params,resultType){

}

/**
 *
 * @param datasource {string}
 * @param bql {BindSql}
 * @param params {JavaMap<String,Object>}
 * @param resultType {Class<?>}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.sqlQueryObject=function(datasource,bql,params,resultType){

}

/**
 *
 * @param datasource {string}
 * @param bql {BindSql}
 * @param params {JavaMap<String,Object>}
 * @param resultType {Class<?>}
 * @return {Object}
 */
JdbcProcedureExecutor.prototype.sqlQueryRow=function(datasource,bql,params,resultType){

}

/**
 *
 * @param datasource {string}
 * @param bql {BindSql}
 * @param params {JavaMap<String,Object>}
 * @return {int}
 */
JdbcProcedureExecutor.prototype.sqlUpdate=function(datasource,bql,params){

}

/**
 *
 * @param datasource {string}
 * @param bql {BindSql}
 * @param page {ApiOffsetSize}
 * @param params {JavaMap<String,Object>}
 * @reutrn {BindSql}
 */
JdbcProcedureExecutor.prototype.sqlWrapPage=function(datasource,bql,page,params){

}

/**
 *
 * @param datasource {string}
 * @param bql {BindSql}
 * @param params {JavaMap<String,Object>}
 * @reutrn {BindSql}
 */
JdbcProcedureExecutor.prototype.sqlWrapCount=function(datasource,bql,params){

}


/**
 *
 * @param datasource {string}
 * @param dialectScriptList {Array<Entry<String,String>>}
 * @param params {JavaMap<String,Object>}
 * @param page {ApiOffsetSize}
 * @return {int}
 */
JdbcProcedureExecutor.prototype.sqlScript=function(datasource,dialectScriptList,params,page){

}

/**
 *
 * @param datasource {string}
 * @param bql {BindSql}
 * @param params {JavaMap<String,Object>}
 * @param resultType {Class<?>}
 * @param pageIndex {int}
 * @param pageSize {int}
 * @return {Array<?>}
 */
JdbcProcedureExecutor.prototype.sqlQueryPage=function(datasource,bql,params,resultType,pageIndex,pageSize){

}

/**
 *
 * @param datasource {string}
 * @param isolation {int}
 * @param params {JavaMap<String,Object>}
 * @return {void}
 */
JdbcProcedureExecutor.prototype.sqlTransBegin=function(datasource,isolation,params){

}

/**
 *
 * @param datasource {string}
 * @param params {JavaMap<String,Object>}
 * @return {void}
 */
JdbcProcedureExecutor.prototype.sqlTransCommit=function(datasource,params){

}

/**
 *
 * @param datasource {string}
 * @param params {JavaMap<String,Object>}
 * @return {void}
 */
JdbcProcedureExecutor.prototype.sqlTransRollback=function(datasource,params){

}

/**
 *
 * @param datasource {string}
 * @param params {JavaMap<String,Object>}
 * @return {void}
 */
JdbcProcedureExecutor.prototype.sqlTransNone=function(datasource,params){

}

let executor=new JdbcProcedureExecutor();
let params=new JavaMap();
