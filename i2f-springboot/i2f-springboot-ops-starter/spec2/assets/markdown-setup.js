function setupMarkdown() {
    // 初始化 markdown-it
    let md = (typeof markdownit === 'function') ? markdownit({
        html: true,         // 允许 HTML（静态文档场景，非用户内容）
        linkify: true,      // 自动识别 URL
        typographer: true,  // 智能标点替换
        breaks: true,       // \n 转为 <br>
        highlight: function (str, lang) {
            if (!lang || lang == '' || lang.trim() == '') {
                lang = 'text';
            }

            let innerHtml = '';
            let actionsHtml = '';
            if (lang == 'mermaid') {

                let chartId = 'mermaid_' + new Date().getTime() + '_' + Math.random().toString(16).substring(2);
                innerHtml = `<div id="${chartId}" class="rich-code-block mermaid-code-block"></div>`;
                actionsHtml = ``;
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
            } else if (lang == 'svg') {

                let chartId = 'svg_' + new Date().getTime() + '_' + Math.random().toString(16).substring(2);
                innerHtml = `<div id="${chartId}" class="rich-code-block svg-code-block"></div>`;
                actionsHtml = ``;
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
        let ext = (lang == 'svg') ? 'svg' : 'txt';
        link.download = (lang || 'text') + '_' + new Date().getTime() + '.' + ext;
        link.click();

        // 清理内存
        URL.revokeObjectURL(url);
    }).catch(() => {
        window.app.$message.error('未找到代码块，保存失败')
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


function renderMermaid(dom, graph) {
    let bubbleDom = dom;
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

function renderSvg(dom, html) {
    setTimeout(() => {
        try {
            if (html.trim().startsWith('<')) {
                // 围栏内为完整 SVG 代码，直接注入
                dom.innerHTML = html;
            } else {
                // 围栏内为 .svg 文件相对路径，fetch 后内联注入
                fetch(html.trim()).then((r) => {
                    if (!r.ok) throw new Error('HTTP ' + r.status);
                    return r.text();
                }).then((text) => {
                    dom.innerHTML = text;
                    dom.chartCode = text;
                }).catch((err) => {
                    dom.innerHTML = `<p style="color:red;">SVG 加载失败：${err.message}</p>`;
                });
            }
        } catch (e) {
            dom.innerHTML = `<p style="color:red;">SVG 渲染失败</p>`;
        }
    }, 0)
}
