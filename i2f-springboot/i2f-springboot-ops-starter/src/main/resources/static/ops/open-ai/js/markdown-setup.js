function setupMarkdown() {
    // 初始化 markdown-it
    let md = (typeof markdownit === 'function') ? markdownit({
        html: false,        // 禁止原始 HTML 防止 XSS
        linkify: true,      // 自动识别 URL
        typographer: true,  // 智能标点替换
        breaks: true,       // \n 转为 <br>
        highlight: function (str, lang) {
            if (!lang || lang == '' || lang.trim() == '') {
                lang = 'text';
            }

            let innerHtml = '';
            let actionsHtml = '';
            if (lang == 'echarts' && !window.app.$responsing) {

                let chartId = 'chart_' + new Date().getTime() + '_' + Math.random().toString(16).substring(2);
                innerHtml = `<div id="${chartId}" class="rich-code-block echart-code-block"></div>`;
                actionsHtml = `<i class="el-icon-download" onclick="onDownloadEchartsBlock(event,'${chartId}')"></i>`;
                let count = 10;
                let applyFunc = () => {
                    let dom = document.querySelector('#' + chartId);
                    if (!dom && count > 0) {
                        count--;
                        setTimeout(applyFunc, 300);
                        return;
                    }

                    if (!dom) {
                        return;
                    }
                    dom.chartCode = str;

                    let json = str.trim();
                    json = json.replace(/^\s*renderEcharts\s*\(/, '');
                    json = json.replace(/\s*\)\s*(;)?\s*$/, '');
                    ses_eval('return ' + json).then(options => {
                        renderEcharts(dom, options)
                    })

                };
                setTimeout(applyFunc, 300);
            } else if (lang == 'mermaid' && !window.app.$responsing) {

                let chartId = 'mermaid_' + new Date().getTime() + '_' + Math.random().toString(16).substring(2);
                innerHtml = `<div id="${chartId}" class="rich-code-block mermaid-code-block"></div>`;
                actionsHtml = `<i class="el-icon-download" onclick="onDownloadMermaidBlock(event,'${chartId}')"></i>`;
                let count = 10;
                let applyFunc = () => {
                    let dom = document.querySelector('#' + chartId);
                    if (!dom && count > 0) {
                        count--;
                        setTimeout(applyFunc, 300);
                        return;
                    }

                    if (!dom) {
                        return;
                    }
                    dom.chartCode = str;

                    let graph = str.trim();
                    renderMermaid(dom, graph)
                };
                setTimeout(applyFunc, 300);
            } else if (lang == 'canvas' && !window.app.$responsing) {

                let chartId = 'canvas_' + new Date().getTime() + '_' + Math.random().toString(16).substring(2);
                innerHtml = `<canvas id="${chartId}" width="480" height="360" class="rich-code-block canvas-code-block"></canvas>`;
                actionsHtml = `<i class="el-icon-download" onclick="onDownloadCanvasBlock(event,'${chartId}')"></i>`;
                let count = 10;
                let applyFunc = () => {
                    let dom = document.querySelector('#' + chartId);
                    if (!dom && count > 0) {
                        count--;
                        setTimeout(applyFunc, 300);
                        return;
                    }

                    if (!dom) {
                        return;
                    }
                    dom.chartCode = str;

                    let json = str.trim();
                    json = json.replace(/^\s*(function\s+)?drawCanvas\s*\(\s*dom\s*\)\s*\{/, '{');
                    json = json.replace(/\}\s*(;)?\s*$/, '}');
                    ses_eval('return (dom)=>' + json).then(fn => {
                        renderCanvas(dom, fn)
                    })

                };
                setTimeout(applyFunc, 300);
            } else if (lang == 'svg' && !window.app.$responsing) {

                let chartId = 'svg_' + new Date().getTime() + '_' + Math.random().toString(16).substring(2);
                innerHtml = `<div id="${chartId}" class="rich-code-block svg-code-block"></div>`;
                actionsHtml = `<i class="el-icon-download" onclick="onDownloadSvgBlock(event,'${chartId}')"></i>`;
                let count = 10;
                let applyFunc = () => {
                    let dom = document.querySelector('#' + chartId);
                    if (!dom && count > 0) {
                        count--;
                        setTimeout(applyFunc, 300);
                        return;
                    }

                    if (!dom) {
                        return;
                    }
                    dom.chartCode = str;

                    renderSvg(dom, str);

                };
                setTimeout(applyFunc, 300);
            } else if (lang == 'drawio' && !window.app.$responsing) {

                let chartId = 'drawio_' + new Date().getTime() + '_' + Math.random().toString(16).substring(2);
                innerHtml = `<div id="${chartId}" class="rich-code-block drawio-code-block"></div>`;
                actionsHtml = `<i class="el-icon-download" onclick="onDownloadDrawioBlock(event,'${chartId}')"></i>`;
                let count = 10;
                let applyFunc = () => {
                    let dom = document.querySelector('#' + chartId);
                    if (!dom && count > 0) {
                        count--;
                        setTimeout(applyFunc, 300);
                        return;
                    }

                    if (!dom) {
                        return;
                    }

                    // 大模型可能属性不会转义，这里进行处理转义
                    str = str.replace(/[a-zA-Z0-9\_\-@\.\:]+\s*=\s*\"[^\"]*\"/g, (match, offset) => {
                        let arr = match.split("=", 2);
                        let attrName = arr[0].trim();
                        let attrValue = arr[1].trim();
                        attrValue = attrValue.substring(1, attrValue.length - 1);
                        attrValue = attrValue.replace('&', '&amp;');
                        attrValue = attrValue.replace('<', '&lt;');
                        attrValue = attrValue.replace('>', '&gt;');
                        return `${attrName}="${attrValue}"`;
                    })

                    dom.chartCode = str;

                    renderDrawIo(dom, str);

                };
                setTimeout(applyFunc, 300);
            } else if (lang == 'threejs' && !window.app.$responsing) {

                let chartId = 'threejs_' + new Date().getTime() + '_' + Math.random().toString(16).substring(2);
                innerHtml = `<div id="${chartId}" class="rich-code-block threejs-code-block"></div>`;
                actionsHtml = `<i class="el-icon-download" onclick="onDownloadThreejsBlock(event,'${chartId}')"></i>`;
                let count = 10;
                let applyFunc = () => {
                    let dom = document.querySelector('#' + chartId);
                    if (!dom && count > 0) {
                        count--;
                        setTimeout(applyFunc, 300);
                        return;
                    }

                    if (!dom) {
                        return;
                    }
                    dom.chartCode = str;

                    let json = str.trim();
                    json = json.replace(/^\s*(function\s+)?renderThreeJs\s*\(\s*dom\s*\)\s*\{/, '{');
                    json = json.replace(/\}\s*(;)?\s*$/, '}');
                    ses_eval_threejs('return (dom)=>' + json).then(fn => {
                        renderThreejs(dom, fn)
                    })

                };
                setTimeout(applyFunc, 300);
            } else if (lang && hljs.getLanguage(lang)) {
                // 检查语言是否受支持
                try {
                    innerHtml = hljs.highlight(str, {language: lang}).value;
                } catch (__) {
                }
            } else {
                innerHtml = md.utils.escapeHtml(str);
            }
            // 如果未指定语言或解析出错，返回转义后的纯文本
            /*language=html*/
            let text = `
                <div class="markdown-code-block">
                    <div class="markdown-code-header">
                        <span class="markdown-header-lang">{{lang}}</span>
                        <span class="markdown-header-actions">
                            {{actionsHtml}}
                            <i class="el-icon-sold-out" onclick="onSaveMarkdownCodeBlock(event,'${lang}')"></i>
                            <i class="el-icon-view" onclick="onViewMarkdownCodeBlock(event,'${lang}')"></i>
                            <i class="el-icon-copy-document" onclick="onCopyMarkdownCodeBlock(event,'${lang}')"></i>
                        </span>
                    </div>
                    <pre class="hljs markdown-code-body">
                            <code>
                                {{innerHtml}}
                            </code>
                    </pre>
                </div>`;
            text = text.replaceAll(/\s*\n\s*/g, '');
            text = text.replaceAll('{{lang}}', lang);
            text = text.replaceAll('{{innerHtml}}', innerHtml);
            text = text.replaceAll('{{actionsHtml}}', actionsHtml);
            return text;
        }
    }) : null;
    if (window.texmath && window.katex) {
        // 集成 katex 显示公式
        md.use(window.texmath, {
            engine: window.katex,     // 明确指定使用 KaTeX 作为渲染引擎
            delimiters: 'dollars',     // 使用 $...$ 和 $$...$$ 语法
            katexOptions: {
                strict: false,       // 关闭严格模式，不再抛出 LaTeX 兼容性警告
                throwOnError: false  // 遇到真正的语法错误时不抛出异常，防止页面崩溃
            }
        });
    }
    return md;
}

function getMarkdownCodeBlockText(event) {
    return new Promise((resolve, reject) => {
        let searchDom = event.target;
        let findDom = null;
        let level = 10;
        while (searchDom) {
            if (level <= 0) {
                break;
            }
            findDom = searchDom.querySelector('.markdown-code-body');
            if (findDom) {
                break;
            }
            searchDom = searchDom.parentElement;
            level--;
        }
        if (!findDom) {
            reject('未找到代码块')
            return;
        }
        let text = findDom.innerText;
        let echartDom = findDom.querySelector('.rich-code-block');
        if (echartDom) {
            text = echartDom.chartCode;
        }
        resolve(text);
    })
}

function onCopyMarkdownCodeBlock(event, lang) {
    getMarkdownCodeBlockText(event).then(text => {
        copy2clipboard(text);
    }).catch(() => {
        window.app.$message.error('未找到代码块，复制失败')
    })
}

function onSaveMarkdownCodeBlock(event, lang) {
    getMarkdownCodeBlockText(event).then(text => {
        // 创建 Blob 并触发下载
        const blob = new Blob([text], {type: 'plain/text;charset=utf-8'});
        const url = URL.createObjectURL(blob);

        const link = document.createElement('a');
        link.href = url;
        link.download = (lang || 'text') + '_' + new Date().getTime() + '.txt';
        link.click();

        // 清理内存
        URL.revokeObjectURL(url);
    }).catch(() => {
        window.app.$message.error('未找到代码块，保存失败')
    })
}

function onViewMarkdownCodeBlock(event, lang) {
    let _this = window.app;
    getMarkdownCodeBlockText(event).then(text => {
        if (['json', 'js', 'ts', 'typescript', 'canvas', 'echarts', 'threejs'].includes(lang)) {
            lang = 'javascript';
        }
        if (['svg', 'drawio'].includes(lang)) {
            lang = 'htmlmixed';
        }
        if (['mermaid'].includes(lang)) {
            lang = 'markdown';
        }

        _this.dialog.messageEditor.type = 'view';
        _this.dialog.messageEditor.text = text;
        _this.dialog.messageEditor.lang = lang || 'text';
        _this.dialog.messageEditor.show = true;
        setTimeout(() => {
            _this.initMessageEditor();
        }, 200)
    }).catch(() => {
        window.app.$message.error('未找到代码块，查看失败')
    })


}

function copy2clipboard(text) {
    const textarea = document.createElement("textarea");
    textarea.value = text;
    // 将元素移出可视区域，避免页面闪烁或滚动
    textarea.style.position = "fixed";
    textarea.style.opacity = "0";
    document.body.appendChild(textarea);

    textarea.select();
    try {
        document.execCommand("copy");
        window.app.$message.success('复制成功')
    } catch (err) {
        window.app.$message.success('复制失败')
    } finally {
        document.body.removeChild(textarea); // 清理临时元素
    }
}

function getParentMessageBubbleDom(dom) {
    let searchDom = dom;
    let findDom = null;
    let level = 10;
    while (searchDom) {
        if (level <= 0) {
            break;
        }
        findDom = searchDom.querySelector('.message-bubble');
        if (findDom) {
            break;
        }
        searchDom = searchDom.parentElement;
        level--;
    }
    return findDom;
}

function renderEcharts(dom, options) {
    let bubbleDom = getParentMessageBubbleDom(dom);
    if (bubbleDom) {
        if (bubbleDom.rendering) {
            setTimeout(() => {
                renderEcharts(dom, options);
            }, 90);
            return;
        }
    }
    setTimeout(() => {
        if (bubbleDom) {
            bubbleDom.rendering = true;
        }
        doRenderEcharts(dom, options)
        if (bubbleDom) {
            bubbleDom.rendering = false;
        }
    }, 0)

}

function doRenderEcharts(dom, options) {
    if (dom.chart) {
        dom.chart.dispose();
    }
    let chart = echarts.init(dom, null, {
        renderer: 'canvas',
        useDirtyRect: false
    });
    dom.chart = chart;
    if (options) {
        chart.setOption(options);
    }
    window.addEventListener('resize', chart.resize);
}

function onDownloadEchartsBlock(event, domId) {
    let dom = document.querySelector('#' + domId);
    let canvas = dom.querySelector('canvas');
    canvas.toBlob(function (blob) {
        // 将 Blob 对象转换为本地 URL
        const url = URL.createObjectURL(blob);

        const link = document.createElement('a');
        link.href = url;
        link.download = 'echarts_' + new Date().getTime() + '.png';
        link.click();

        // 释放内存
        URL.revokeObjectURL(url);
    }, 'image/png', 0.9); // 指定格式和画质
}

function renderMermaid(dom, graph) {
    let bubbleDom = getParentMessageBubbleDom(dom);
    if (bubbleDom) {
        if (bubbleDom.rendering) {
            setTimeout(() => {
                renderMermaid(dom, graph);
            }, 90);
            return;
        }
    }
    if (!window.mermaid) {
        setTimeout(() => {
            renderMermaid(dom, graph);
        }, 90);
        return;
    }
    setTimeout(async () => {
        if (bubbleDom) {
            bubbleDom.rendering = true;
        }
        try {
            // 核心：调用 render 方法
            // 参数1: 唯一ID (用于内部生成临时DOM)
            // 参数2: 图表定义文本
            const {svg, bindFunctions} = await mermaid.render('render_' + dom.id, graph);

            // 将生成的 SVG 代码插入到目标容器中
            dom.innerHTML = svg;

            // 如果图表包含交互（如点击事件、工具提示），需要绑定函数
            if (bindFunctions) {
                bindFunctions(dom);
            }
        } catch (error) {
            // 处理语法错误等异常情况
            console.error('Mermaid 渲染失败:', error);
            dom.innerHTML = `<p style="color:red;">图表语法错误，请检查代码！</p>`;
        } finally {
            const panzoom = Panzoom(dom, {
                maxScale: 5,       // 最大放大倍数
                minScale: 0.25,    // 最小缩小倍数
                contain: 'outside' // 可选：限制拖拽边界，防止拖出视野
            });

            dom.parentElement.addEventListener('wheel', panzoom.zoomWithWheel);
        }
        if (bubbleDom) {
            bubbleDom.rendering = false;
        }
    }, 0)
}

function onDownloadMermaidBlock(event, domId) {
    onDownloadSvgBlock(event, domId)
}

function renderCanvas(dom, fn) {
    let bubbleDom = getParentMessageBubbleDom(dom);
    if (bubbleDom) {
        if (bubbleDom.rendering) {
            setTimeout(() => {
                renderCanvas(dom, fn);
            }, 90);
            return;
        }
    }
    setTimeout(() => {
        if (bubbleDom) {
            bubbleDom.rendering = true;
        }
        fn(dom)
        if (bubbleDom) {
            bubbleDom.rendering = false;
        }
    }, 0)
}

function onDownloadCanvasBlock(event, domId) {
    let canvas = document.querySelector('#' + domId);
    canvas.toBlob(function (blob) {
        // 将 Blob 对象转换为本地 URL
        const url = URL.createObjectURL(blob);

        const link = document.createElement('a');
        link.href = url;
        link.download = 'canvas_' + new Date().getTime() + '.png';
        link.click();

        // 释放内存
        URL.revokeObjectURL(url);
    }, 'image/png', 0.9); // 指定格式和画质
}

function renderSvg(dom, html) {
    let bubbleDom = getParentMessageBubbleDom(dom);
    if (bubbleDom) {
        if (bubbleDom.rendering) {
            setTimeout(() => {
                renderSvg(dom, fn);
            }, 90);
            return;
        }
    }
    setTimeout(() => {
        if (bubbleDom) {
            bubbleDom.rendering = true;
        }
        try {
            safeSvgPurifier(html).then((text) => {
                dom.innerHTML = text;
            })
        } finally {
            const panzoom = Panzoom(dom, {
                maxScale: 5,       // 最大放大倍数
                minScale: 0.25,    // 最小缩小倍数
                contain: 'outside' // 可选：限制拖拽边界，防止拖出视野
            });

            dom.parentElement.addEventListener('wheel', panzoom.zoomWithWheel);
        }
        if (bubbleDom) {
            bubbleDom.rendering = false;
        }
    }, 0)
}

function onDownloadSvgBlock(event, domId) {
    let dom = document.querySelector('#' + domId);

    let svgStr = dom.innerHTML;
    if (!svgStr.startsWith('<?xml')) {
        svgStr = '<?xml version="1.0" standalone="no"?>\r\n' + svgStr;
    }

    // 创建 Blob 并触发下载
    const blob = new Blob([svgStr], {type: 'image/svg+xml;charset=utf-8'});
    const url = URL.createObjectURL(blob);

    const link = document.createElement('a');
    link.href = url;
    link.download = 'svg_' + new Date().getTime() + '.svg';
    link.click();

    // 清理内存
    URL.revokeObjectURL(url);
}

function renderThreejs(dom, fn) {
    let bubbleDom = getParentMessageBubbleDom(dom);
    if (bubbleDom) {
        if (bubbleDom.rendering) {
            setTimeout(() => {
                renderThreejs(dom, fn);
            }, 90);
            return;
        }
    }
    if (!window.THREE) {
        setTimeout(() => {
            renderThreejs(dom, fn);
        }, 90);
        return;
    }
    setTimeout(() => {
        if (bubbleDom) {
            bubbleDom.rendering = true;
        }
        fn(dom)
        if (bubbleDom) {
            bubbleDom.rendering = false;
        }
    }, 0)
}

function onDownloadThreejsBlock(event, domId) {
    let dom = document.querySelector('#' + domId);
    let canvas = dom.querySelector('canvas');
    canvas.toBlob(function (blob) {
        // 将 Blob 对象转换为本地 URL
        const url = URL.createObjectURL(blob);

        const link = document.createElement('a');
        link.href = url;
        link.download = 'threejs_' + new Date().getTime() + '.png';
        link.click();

        // 释放内存
        URL.revokeObjectURL(url);
    }, 'image/png', 0.9); // 指定格式和画质
}


function renderDrawIo(dom, xml) {
    let bubbleDom = getParentMessageBubbleDom(dom);
    if (bubbleDom) {
        if (bubbleDom.rendering) {
            setTimeout(() => {
                renderDrawIo(dom, xml);
            }, 90);
            return;
        }
    }
    setTimeout(() => {
        if (bubbleDom) {
            bubbleDom.rendering = true;
        }
        renderDrawIoDiagrams(dom, xml);
        if (bubbleDom) {
            bubbleDom.rendering = false;
        }
    }, 0)
}

function renderDrawIoDiagrams(dom, xmlContent) {
    window.addEventListener('message', function (evt) {
        console.log('evt', evt, dom, xmlContent)
        // 1. 通过 origin 判断：只处理来自 diagrams.net 的消息
        if (evt.origin !== 'https://embed.diagrams.net') {
            return;
        }

        // 2. 或者通过 evt.source 判断：确认消息来自你指定的那个 iframe
        const drawioFrame = dom.querySelector('iframe');
        if (!drawioFrame) {
            return;
        }
        if (evt.source !== drawioFrame.contentWindow) {
            return;
        }

        if (evt.data.length > 0) {
            let msg;
            try {
                msg = JSON.parse(evt.data);
            } catch (e) {
                return;
            }

            // 当 iframe 准备就绪后，加载你的 XML
            if (msg.event === 'init') {
                drawioFrame.contentWindow.postMessage(JSON.stringify({
                    action: 'load',
                    xml: xmlContent,
                    autosave: 0 // 关闭自动保存，因为不需要编辑
                }), '*');
            }
        }
    });

    dom.innerHTML = `<iframe
                        src="https://embed.diagrams.net/?embed=1&modified=0&libraries=0&spin=1&saveAndExit=0&noSaveBtn=1&noExitBtn=1&proto=json"
                        class="drawio-code-block"
                        >
                </iframe>`;
}

function onDownloadDrawioBlock(event, domId) {
    getMarkdownCodeBlockText(event).then(text => {
        // 创建 Blob 并触发下载
        const blob = new Blob([text], {type: 'plain/text;charset=utf-8'});
        const url = URL.createObjectURL(blob);

        const link = document.createElement('a');
        link.href = url;
        link.download = 'drawio_' + new Date().getTime() + '.xml.drawio';
        link.click();

        // 清理内存
        URL.revokeObjectURL(url);
    }).catch(() => {
        window.app.$message.error('未找到代码块，保存失败')
    })
}


/**
 * 使用 DOMParser 提取并净化 SVG
 * @param {string} html - 后端返回的原始字符串
 * @returns {Promise<string>} - 净化后的 SVG 字符串
 */
function safeSvgPurifier(html) {
    return new Promise((resolve, reject) => {
        if (!html || typeof html !== 'string') {
            reject('empty content or not string!')
            return;
        }

        // 1. 提取 SVG 标签 (防止后端返回了整段 HTML 导致 DOMParser 解析异常)
        const svgMatch = html.match(/<svg[\s\S]*?<\/svg>/i);
        if (!svgMatch) {
            reject('未在返回内容中找到 SVG 标签');
            return;
        }

        try {
            // 2. 使用 DOMParser 将字符串解析为 XML 文档
            // 注意：这里使用 'image/svg+xml' 而不是 'text/html'
            const parser = new DOMParser();
            const doc = parser.parseFromString(svgMatch[0], 'image/svg+xml');
            const svgElement = doc.querySelector('svg');

            // 检查是否解析失败 (DOMParser 不会抛出异常，而是返回包含 <parsererror> 的文档)
            const parseError = doc.querySelector('parsererror');
            if (!svgElement || parseError) {
                reject('SVG 解析失败，内容不符合 XML 规范');
                return;
            }

            // --- 3. 安全清洗 ---

            // A. 移除 SVG 内部的所有 <script> 标签
            const scripts = svgElement.querySelectorAll('script,iframe');
            scripts.forEach(script => script.remove());

            // B. 移除根元素及所有子元素的 on* 事件属性
            const allElements = [svgElement, ...svgElement.querySelectorAll('*')];
            allElements.forEach(el => {
                Array.from(el.attributes).forEach(attr => {
                    if (attr.name.toLowerCase().startsWith('on')) {
                        el.removeAttribute(attr.name);
                    }
                });
            });

            // --- 4. 规范化输出 ---
            if (!svgElement.hasAttribute('xmlns')) {
                svgElement.setAttribute('xmlns', 'http://www.w3.org/2000/svg');
            }

            resolve(svgElement.outerHTML);
        } catch (error) {
            reject('处理 SVG 时发生异常');
        }
    })

}