function createLangEditorMixin() {
    return {
        methods: {
            onEditorContextmenu(editor, categoryList) {
                let _this = this
                let convertText = function (convertor) {
                    try {
                        let text = editor.getSelection();
                        if (!text || text == '') {
                            text = editor.getValue()
                            text = convertor(text);
                            editor.setValue(text)
                        } else {
                            text = convertor(text);
                            editor.replaceSelection(text)
                        }
                    } catch (e) {
                        _this.$message.error('操作失败')
                    }
                }

                let presetMenusMap = {
                    text: [
                        {label: '', divided: true}, // 分割线
                        {
                            label: `upper`,
                            icon: 'el-icon-magic-stick',
                            onClick: () => {
                                convertText(function (s) {
                                    if (!s) {
                                        return s;
                                    }
                                    return s.toUpperCase();
                                })
                            }
                        },
                        {
                            label: `lower`,
                            icon: 'el-icon-magic-stick',
                            onClick: () => {
                                convertText(function (s) {
                                    if (!s) {
                                        return s;
                                    }
                                    return s.toLowerCase();
                                })
                            }
                        },
                        {
                            label: `replace`,
                            icon: 'el-icon-magic-stick',
                            onClick: () => {
                                let searchText = '';
                                let replaceText = '';
                                this.$prompt('请输入要查找的字符串', '输入框', {
                                    confirmButtonText: '确定',
                                    cancelButtonText: '取消',
                                }).then(({value}) => {
                                    searchText = value;
                                    this.$prompt('请输入要替换为的字符串', '输入框', {
                                        confirmButtonText: '确定',
                                        cancelButtonText: '取消',
                                    }).then(({value}) => {
                                        replaceText = value;
                                        convertText(function (s) {
                                            if (!s) {
                                                return s;
                                            }
                                            return s.replaceAll(searchText, replaceText);
                                        })
                                    })
                                })


                            }
                        },
                        {
                            label: `regexp_replace`,
                            icon: 'el-icon-magic-stick',
                            onClick: () => {
                                let searchText = '';
                                let replaceText = '';
                                this.$prompt('请输入要查找的正则', '输入框', {
                                    confirmButtonText: '确定',
                                    cancelButtonText: '取消',
                                }).then(({value}) => {
                                    searchText = value;
                                    this.$prompt('请输入要替换为的字符串', '输入框', {
                                        confirmButtonText: '确定',
                                        cancelButtonText: '取消',
                                    }).then(({value}) => {
                                        replaceText = value;
                                        convertText(function (s) {
                                            if (!s) {
                                                return s;
                                            }
                                            return s.replaceAll(new RegExp(searchText, 'g'), replaceText);
                                        })
                                    })
                                })


                            }
                        },
                        {
                            label: 'encode/decode',
                            icon: 'el-icon-magic-stick',
                            children: [
                                {label: '', divided: true}, // 分割线
                                {
                                    label: `encode_base64`,
                                    icon: 'el-icon-magic-stick',
                                    onClick: () => {
                                        convertText(function (s) {
                                            if (!s) {
                                                return s;
                                            }
                                            return btoa(s);
                                        })
                                    }
                                },
                                {
                                    label: `decode_base64`,
                                    icon: 'el-icon-magic-stick',
                                    onClick: () => {
                                        convertText(function (s) {
                                            if (!s) {
                                                return s;
                                            }
                                            return atob(s);
                                        })
                                    }
                                },

                            ]
                        },
                        {
                            label: 'SQL escape',
                            icon: 'el-icon-magic-stick',
                            children: [
                                {label: '', divided: true}, // 分割线
                                {
                                    label: `escape_sql`,
                                    icon: 'el-icon-magic-stick',
                                    onClick: () => {
                                        convertText(function (s) {
                                            if (!s) {
                                                return s;
                                            }
                                            return "'" + s.replaceAll("'", "''") + "'";
                                        })
                                    }
                                },
                                {
                                    label: `unescape_sql`,
                                    icon: 'el-icon-magic-stick',
                                    onClick: () => {
                                        convertText(function (s) {
                                            if (!s) {
                                                return s;
                                            }
                                            if (s.startsWith("'")) {
                                                s = s.substring(1);
                                            }
                                            if (s.endsWith("'")) {
                                                s = s.substring(0, s.length - 1);
                                            }
                                            return s.replaceAll("''", "'");
                                        })
                                    }
                                },
                            ]
                        },
                        {
                            label: 'Json Convert',
                            icon: 'el-icon-magic-stick',
                            children: [
                                {label: '', divided: true}, // 分割线
                                {
                                    label: `to_json`,
                                    icon: 'el-icon-magic-stick',
                                    onClick: () => {
                                        convertText(function (s) {
                                            if (!s) {
                                                return s;
                                            }
                                            return JSON.stringify(s);
                                        })
                                    }
                                },
                                {
                                    label: `parse_json`,
                                    icon: 'el-icon-magic-stick',
                                    onClick: () => {
                                        convertText(function (s) {
                                            if (!s) {
                                                return s;
                                            }
                                            let obj = JSON.parse(s);
                                            if (typeof obj == 'string') {
                                                return obj + '';
                                            }
                                            return JSON.stringify(obj, null, '    ')
                                        })
                                    }
                                },
                                {
                                    label: `format_json`,
                                    icon: 'el-icon-magic-stick',
                                    onClick: () => {
                                        convertText(function (s) {
                                            if (!s) {
                                                return s;
                                            }
                                            let obj = JSON.parse(s);
                                            return JSON.stringify(obj, null, '    ')
                                        })
                                    }
                                },
                            ]
                        },

                    ],
                    sql: [
                        {
                            label: 'SQL segments',
                            icon: 'el-icon-magic-stick',
                            children: [
                                {label: '', divided: true}, // 分割线
                                {
                                    label: `count(1)`,
                                    icon: 'el-icon-magic-stick',
                                    onClick: () => {
                                        convertText(function (s) {
                                            if (!s) {
                                                return s;
                                            }
                                            return 'select count(1) \nfrom ' + s + '\nwhere 1=1 \n';
                                        })
                                    }
                                },
                                {
                                    label: `select`,
                                    icon: 'el-icon-magic-stick',
                                    onClick: () => {
                                        convertText(function (s) {
                                            if (!s) {
                                                return s;
                                            }
                                            return 'select * \nfrom ' + s + '\nwhere 1=1 \n';
                                        })
                                    }
                                },
                                {
                                    label: `update`,
                                    icon: 'el-icon-magic-stick',
                                    onClick: () => {
                                        convertText(function (s) {
                                            if (!s) {
                                                return s;
                                            }
                                            return 'update ' + s + '\n set \n\nwhere  \n';
                                        })
                                    }
                                },
                                {
                                    label: `delete`,
                                    icon: 'el-icon-magic-stick',
                                    onClick: () => {
                                        convertText(function (s) {
                                            if (!s) {
                                                return s;
                                            }
                                            return 'delete from ' + s + '\nwhere  \n';
                                        })
                                    }
                                },
                            ]
                        },
                        {label: '', divided: true}, // 分割线
                        ...((typeof sqlFormatter != 'object') ? [] : [{
                            label: `format sql`,
                            icon: 'el-icon-magic-stick',
                            onClick: () => {
                                let _this = this;
                                convertText(function (s) {
                                    if (!s) {
                                        return s;
                                    }
                                    return _this.formatSql(s);
                                })
                            }
                        }]),
                    ],
                    'json-editor': [
                        {label: '', divided: true}, // 分割线
                        {
                            label: `Json Editor`,
                            icon: 'el-icon-magic-stick',
                            onClick: () => {
                                let text = editor.getValue();
                                this.showJsonEditor(text, (res) => {
                                    editor.setValue(res);
                                });

                            }
                        },
                    ]
                }
                let menus = [...presetMenusMap.text]
                categoryList = categoryList || []
                if (categoryList && categoryList.length > 0) {
                    for (let i = 0; i < categoryList.length; i++) {
                        let category = categoryList[i];
                        if (category == 'text') {
                            continue;
                        }
                        menus.push(...presetMenusMap[category])
                    }
                }
                this.$contextmenu({
                    items: menus,
                    event, // 必须传入 event 以确定位置
                    zIndex: 3000,
                    minWidth: 180
                });
            },
            initLangEditor(editorProp, editorRef, editorLang, afterCallback) {
                let bts = new Date().getTime()
                let initEditorTask = () => {
                    if (this.langEditors[editorProp]) {
                        try {
                            this.langEditors[editorProp].toTextArea()
                        } catch (e) {

                        }
                    }
                    this.langEditors[editorProp] = null;

                    let dom = this.$refs[editorRef];
                    if (!dom) {
                        let cts = new Date().getTime();
                        if (cts - bts < 3000) {
                            setTimeout(initEditorTask, 30);
                        }
                        return
                    }
                    this.langEditors[editorProp] = CodeMirror.fromTextArea(dom, {  // 标识到textarea
                        value: "",  // 文本域默认显示的文本
                        mode: editorLang || '',  // 模式
                        theme: "idea",  // CSS样式选择
                        indentUnit: 2,  // 缩进单位，默认2
                        smartIndent: true,  // 是否智能缩进
                        tabSize: 4,  // Tab缩进，默认4
                        readOnly: false,  // 是否只读，默认false
                        showCursorWhenSelecting: true,
                        hintOptions: {
                            completeSingle: false,
                        },
                        lineNumbers: true  // 是否显示行号
                        // .. 还有好多，翻译不完。需要的去看http://codemirror.net/doc/manual.html#config
                    });
                    this.langEditors[editorProp].on("keypress", () => {
                        //编译器内容更改事件
                        this.langEditors[editorProp].showHint();
                    });
                    if (afterCallback) {
                        afterCallback(this.langEditors[editorProp], dom)
                    }
                }

                initEditorTask()
            },
        }
    }
}