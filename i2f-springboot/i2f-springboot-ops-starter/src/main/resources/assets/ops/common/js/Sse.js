/**
 *
 * @param url {string}
 * @param options {{method:string,headers: object,body: object,signal: AbortSignal}}
 * @param callback {function(data:{done: boolean,content: string})}
 * @returns {Promise<void>}
 */
async function sse(url, options, callback) {
    const abortController = new AbortController();
    if (!options.signal) {
        options.signal = abortController.signal;
    }
    if (options.body && typeof options.body !== 'string') {
        options.body = JSON.stringify(options.body)
    }
    if (!options.method) {
        if (options.body) {
            options.method = 'POST'
        } else {
            options.method = 'GET'
        }
    }
    if (!options.headers) {
        options.headers = {}
    }
    let contentType = null
    Object.keys(options.headers).forEach(k => {
        if (k.toLowerCase() === 'content-type') {
            contentType = k;
        }
    })
    if (!contentType) {
        options.headers['Content-Type'] = 'application/json'
    }
    let fetchOptions = {}
    if (options.method) {
        fetchOptions.method = options.method
    }
    if (options.headers) {
        fetchOptions.headers = options.headers
    }
    if (options.body) {
        fetchOptions.body = options.body
    }
    if (options.signal) {
        fetchOptions.signal = options.signal
    }
    if (options.params) {
        let paramStr = null
        if (typeof options.params === 'string') {
            paramStr = options.params
        } else {
            let searchParams = new URLSearchParams();
            Object.keys(options.params).forEach(k => {
                searchParams.set(k, options.params[k] + '')
            })
            paramStr = searchParams.toString();
        }
        if (url.indexOf('?') >= 0) {
            url = url + '&' + paramStr
        } else {
            url = url + '?' + paramStr
        }
    }
    const response = await fetch(url, fetchOptions)
    if (!response.ok) {
        let errData = {};
        try {
            errData = await response.json();
        } catch (e) {
        }
        throw new Error((errData.error && errData.error.message) ? errData.error.message : ('HTTP ' + response.status));
    }
    const reader = response.body.getReader();
    const decoder = new TextDecoder('utf-8');
    let buf = '';
    while (true) {
        const {done, value} = await reader.read();
        if (done) {
            callback({
                done: true,
                content: null
            })
            break;
        }
        buf += decoder.decode(value, {stream: true});
        let lines = buf.split('\n');
        buf = lines.pop(); // 保留未完整的行
        for (let line of lines) {
            line = line.trim();
            if (!line || !line.startsWith('data:')) {
                continue;
            }
            callback({
                done: false,
                content: line.slice(5).trim()
            });
        }
    }
}
