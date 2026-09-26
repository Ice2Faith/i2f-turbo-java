package i2f.net.http.impl;


import i2f.net.http.consts.ContentTypeConstants;
import i2f.net.http.consts.HttpHeaderConstants;
import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.HttpResponse;
import i2f.net.http.interfaces.HttpProcessorProvider;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.net.http.interfaces.IHttpResponseExtractor;
import i2f.serialize.std.str.json.IJsonSerializer;

import java.io.IOException;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/3/26 20:12
 * @desc
 */
public class BasicHttpProcessorProvider implements HttpProcessorProvider {
    protected IHttpProcessor httpProcessor;

    public BasicHttpProcessorProvider(IHttpProcessor httpProcessor) {
        this.httpProcessor = httpProcessor;
    }

    @Override
    public <T> T postFormForObject(HttpRequest request, String charset, Class<T> clazz, IJsonSerializer processor) throws IOException {
        return request.toMutator()
                .set(u -> u::setMethod, HttpMethodConstants.POST)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Form)
                .done()
                .send(httpProcessor)
                .getContentAsObject(processor, clazz, charset);
    }

    @Override
    public String postFormForString(HttpRequest request, String charset) throws IOException {
        return request.toMutator()
                .set(u -> u::setMethod, HttpMethodConstants.POST)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Form)
                .done()
                .send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public HttpResponse postForm(HttpRequest request) throws IOException {
        return request.toMutator()
                .set(u -> u::setMethod, HttpMethodConstants.POST)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Form)
                .done().send(httpProcessor);
    }

    @Override
    public <T> T postJsonForObject(HttpRequest request, String charset, Class<T> clazz, IJsonSerializer jsonProcessor) throws IOException {
        return request.toMutator().set(u -> u::setMethod, HttpMethodConstants.POST)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Json)
                .done().send(httpProcessor)
                .getContentAsObject(jsonProcessor, clazz, charset);
    }

    @Override
    public String postJsonForString(HttpRequest request, String charset) throws IOException {
        return request.toMutator().set(u -> u::setMethod, HttpMethodConstants.POST)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Json)
                .done().send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public HttpResponse postJson(HttpRequest request) throws IOException {
        return request.toMutator().set(u -> u::setMethod, HttpMethodConstants.POST)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Json)
                .done().send(httpProcessor);
    }

    @Override
    public HttpResponse postJson(String url, Object data) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Json)
                .done().send(httpProcessor);
    }

    @Override
    public HttpResponse postJson(String url, Object data, Object params) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .set(u -> u::setParams, params)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Json)
                .done().send(httpProcessor);
    }

    @Override
    public HttpResponse postJson(String url, Object data, Object params, Map<String, Object> header) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .set(u -> u::setParams, params)
                .set(u -> u::setHeader, header)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Json)
                .done().send(httpProcessor);
    }

    @Override
    public String postJsonForString(String url, Object data, String charset) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Json)
                .done().send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public String postJsonForString(String url, Object data, Object params, String charset) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .set(u -> u::setParams, params)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Json)
                .done().send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public String postJsonForString(String url, Object data, Object params, Map<String, Object> header, String charset) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .set(u -> u::setParams, params)
                .set(u -> u::setHeader, header)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Json)
                .done().send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public <T> T postJsonForObject(String url, Object data, String charset, Class<T> clazz, IJsonSerializer processor) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Json)
                .done().send(httpProcessor)
                .getContentAsObject(processor, clazz, charset);
    }

    @Override
    public <T> T postJsonForObject(String url, Object data, Object params, String charset, Class<T> clazz, IJsonSerializer processor) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .set(u -> u::setParams, params)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Json)
                .done().send(httpProcessor)
                .getContentAsObject(processor, clazz, charset);
    }

    @Override
    public <T> T postJsonForObject(String url, Object data, Object params, Map<String, Object> header, String charset, Class<T> clazz, IJsonSerializer processor) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .set(u -> u::setParams, params)
                .set(u -> u::setHeader, header)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Json)
                .done().send(httpProcessor)
                .getContentAsObject(processor, clazz, charset);
    }

    @Override
    public HttpResponse postForm(String url, Object data) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Form)
                .done().send(httpProcessor);
    }

    @Override
    public HttpResponse postForm(String url, Object data, Object params) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .set(u -> u::setParams, params)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Form)
                .done().send(httpProcessor);
    }

    @Override
    public HttpResponse postForm(String url, Object data, Object params, Map<String, Object> header) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .set(u -> u::setParams, params)
                .set(u -> u::setHeader, header)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Form)
                .done().send(httpProcessor);
    }

    @Override
    public String postFormForString(String url, Object data, String charset) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Form)
                .done().send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public String postFormForString(String url, Object data, Object params, String charset) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .set(u -> u::setParams, params)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Form)
                .done().send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public String postFormForString(String url, Object data, Object params, Map<String, Object> header, String charset) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .set(u -> u::setParams, params)
                .set(u -> u::setHeader, header)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Form)
                .done().send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public <T> T postFormForObject(String url, Object data, String charset, Class<T> clazz, IJsonSerializer processor) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Form)
                .done().send(httpProcessor)
                .getContentAsObject(processor, clazz, charset);
    }

    @Override
    public <T> T postFormForObject(String url, Object data, Object params, String charset, Class<T> clazz, IJsonSerializer processor) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .set(u -> u::setParams, params)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Form)
                .done().send(httpProcessor)
                .getContentAsObject(processor, clazz, charset);
    }

    @Override
    public <T> T postFormForObject(String url, Object data, Object params, Map<String, Object> header, String charset, Class<T> clazz, IJsonSerializer processor) throws IOException {
        return HttpRequest.doPost()
                .set(u -> u::setUrl, url)
                .set(u -> u::setData, data)
                .set(u -> u::setParams, params)
                .set(u -> u::setHeader, header)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentType, ContentTypeConstants.Form)
                .done().send(httpProcessor)
                .getContentAsObject(processor, clazz, charset);
    }

    @Override
    public HttpResponse get(String url) throws IOException {
        return HttpRequest.doGet()
                .set(u -> u::setUrl, url)
                .done().send(httpProcessor);
    }

    @Override
    public HttpResponse get(String url, Object params) throws IOException {
        return HttpRequest.doGet()
                .set(u -> u::setUrl, url)
                .set(u -> u::setParams, params)
                .done().send(httpProcessor);
    }

    @Override
    public HttpResponse get(String url, Object params, Map<String, Object> header) throws IOException {
        return HttpRequest.doGet()
                .set(u -> u::setUrl, url)
                .set(u -> u::setParams, params)
                .set(u -> u::setHeader, header)
                .done().send(httpProcessor);
    }

    @Override
    public String getForString(String url, String charset) throws IOException {
        return HttpRequest.doGet()
                .set(u -> u::setUrl, url)
                .done().send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public String getForString(String url, Object params, String charset) throws IOException {
        return HttpRequest.doGet()
                .set(u -> u::setUrl, url)
                .set(u -> u::setParams, params)
                .done().send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public String getForString(String url, Object params, Map<String, Object> header, String charset) throws IOException {
        return HttpRequest.doGet()
                .set(u -> u::setUrl, url)
                .set(u -> u::setParams, params)
                .set(u -> u::setHeader, header)
                .done().send(httpProcessor)
                .getContentAsString(charset);
    }

    @Override
    public <T> T getForObject(String url, String charset, Class<T> clazz, IJsonSerializer processor) throws IOException {
        return HttpRequest.doGet()
                .set(u -> u::setUrl, url)
                .done().send(httpProcessor)
                .getContentAsObject(processor, clazz, charset);
    }

    @Override
    public <T> T getForObject(String url, Object params, String charset, Class<T> clazz, IJsonSerializer processor) throws IOException {
        return HttpRequest.doGet()
                .set(u -> u::setUrl, url)
                .set(u -> u::setParams, params)
                .done().send(httpProcessor)
                .getContentAsObject(processor, clazz, charset);
    }

    @Override
    public <T> T getForObject(String url, Object params, Map<String, Object> header, String charset, Class<T> clazz, IJsonSerializer processor) throws IOException {
        return HttpRequest.doGet()
                .set(u -> u::setUrl, url)
                .set(u -> u::setParams, params)
                .set(u -> u::setHeader, header)
                .done().send(httpProcessor)
                .getContentAsObject(processor, clazz, charset);
    }

    @Override
    public HttpResponse http(HttpRequest request) throws IOException {
        return httpProcessor.http(request);
    }

    @Override
    public <T> T http(HttpRequest request, IHttpResponseExtractor<T> extractor) throws IOException {
        return httpProcessor.http(request, extractor);
    }
}
