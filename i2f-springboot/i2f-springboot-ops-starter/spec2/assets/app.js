/**
 * i2f-springboot-ops-starter · OpenAI 子系统 · 技术全景
 * Vue 渲染引擎：fetch 加载 .md → markdown-it 渲染 → GSAP 动画
 */
(function () {
    var spec = getSpecData();
    var md = setupMarkdown();

    /* ==================== 全局注册 ==================== */
    function copyToClipboard(text) {
        var ta = document.createElement('textarea');
        ta.value = text;
        ta.style.position = 'fixed';
        ta.style.opacity = '0';
        document.body.appendChild(ta);
        ta.select();
        try {
            document.execCommand('copy');
            window.app.$message.success('已复制到剪贴板');
        } catch (e) {
            window.app.$message.error('复制失败');
        }
        document.body.removeChild(ta);
    }

    /* ==================== Vue 应用 ==================== */
    window.app = new Vue({
        el: '#app',
        data: {
            spec: spec,
            mastheadHtml: '',
            progress: 0,
            showBackTop: false,
            activeNavId: 'ch01',
            scrollTimer: null,
            counterDone: {},
            /* 各章节的渲染后 HTML */
            chapterHtml: {},
            /* 加载状态 */
            chapterLoading: {},
            chapterError: {},
            /* 导航 */
            navOpen: false
        },
        computed: {
            allChapters: function () {
                return this.spec.chapters;
            }
        },
        methods: {
            /* ---------- 渲染 Markdown ---------- */
            renderMd: function (raw) {
                if (!raw) return '';
                return md.render(raw);
            },
            /* ---------- 加载 masthead ---------- */
            loadMasthead: function () {
                var self = this;
                fetch('content/masthead.md')
                    .then(function (r) {
                        if (!r.ok) throw new Error('HTTP ' + r.status);
                        return r.text();
                    })
                    .then(function (text) {
                        self.mastheadHtml = self.renderMd(text);
                        self.$nextTick(function () {
                            self.initReveal();
                            self.animateCounters();
                        });
                    });
            },
            /* ---------- 加载章节 ---------- */
            loadChapter: function (ch) {
                var self = this;
                if (self.chapterHtml[ch.id] || self.chapterLoading[ch.id]) return;
                Vue.set(self.chapterLoading, ch.id, true);
                fetch(ch.file)
                    .then(function (r) {
                        if (!r.ok) throw new Error('HTTP ' + r.status);
                        return r.text();
                    })
                    .then(function (text) {
                        Vue.set(self.chapterHtml, ch.id, self.renderMd(text));
                        self.$nextTick(function () {
                            self.initReveal();
                            self.renderMermaidBlocks();
                        });
                    })
                    .catch(function (err) {
                        Vue.set(self.chapterError, ch.id, err.message);
                    })
                    .finally(function () {
                        Vue.set(self.chapterLoading, ch.id, false);
                    });
            },
            /* ---------- 懒加载可见章节 ---------- */
            loadVisibleChapters: function () {
                var self = this;
                self.allChapters.forEach(function (ch) {
                    var el = document.getElementById(ch.id);
                    if (!el) return;
                    var rect = el.getBoundingClientRect();
                    /* 预加载：章节在视口 + 2 屏高度范围内 */
                    if (rect.top < window.innerHeight * 3 && rect.bottom > -window.innerHeight) {
                        self.loadChapter(ch);
                    }
                });
            },
            /* ---------- 滚动揭示动画 ---------- */
            initReveal: function () {
                var reveals = document.querySelectorAll('.reveal');
                reveals.forEach(function (el) {
                    if (el._revealed) return;
                    el._revealed = true;
                    var rect = el.getBoundingClientRect();
                    var windowHeight = window.innerHeight;
                    if (rect.top < windowHeight - 60) {
                        el.classList.add('active');
                    }
                });
            },
            /* ---------- 数字统计动画 ---------- */
            animateCounters: function () {
                var self = this;
                /* 只在 stats 元素可见时执行一次 */
                var statsEl = document.querySelector('.stat-section');
                if (!statsEl || self.counterDone._stats) return;
                var rect = statsEl.getBoundingClientRect();
                if (rect.top < window.innerHeight && rect.bottom > 0) {
                    self.counterDone._stats = true;
                    var counters = statsEl.querySelectorAll('.stat-count');
                    counters.forEach(function (el) {
                        var target = parseInt(el.getAttribute('data-target'));
                        var duration = 1200;
                        var start = null;
                        function step(ts) {
                            if (!start) start = ts;
                            var progress = Math.min((ts - start) / duration, 1);
                            var ease = 1 - Math.pow(1 - progress, 3);
                            el.textContent = Math.floor(ease * target);
                            if (progress < 1) {
                                requestAnimationFrame(step);
                            } else {
                                el.textContent = target;
                            }
                        }
                        requestAnimationFrame(step);
                    });
                }
            },
            /* ---------- 渲染 mermaid 代码块 ---------- */
            renderMermaidBlocks: function () {
                /* markdown-it 的 highlight 已处理 mermaid 代码块，此处做兜底 */
                var blocks = document.querySelectorAll('.mermaid-code-block');
                blocks.forEach(function (dom) {
                    if (dom.innerHTML.trim() !== '') return;
                    if (dom.chartCode && window.mermaid) {
                        renderMermaid(dom, dom.chartCode);
                    }
                });
            },
            /* ---------- 滚动处理 ---------- */
            onScroll: function () {
                var self = this;
                var scrollY = window.scrollY;
                var docHeight = document.documentElement.scrollHeight - window.innerHeight;
                /* 进度条 */
                if (docHeight > 0) {
                    self.progress = Math.round((scrollY / docHeight) * 100);
                }
                /* 返回顶部按钮 */
                self.showBackTop = scrollY > 400;
                /* 滚动揭示 */
                self.initReveal();
                self.animateCounters();
                /* 懒加载 */
                self.loadVisibleChapters();
                /* 更新激活导航 */
                self.updateActiveNav();
            },
            /* ---------- 滚动到章节 ---------- */
            scrollToChapter: function (id) {
                var el = document.getElementById(id);
                if (!el) return;
                this.navOpen = false;
                var top = el.getBoundingClientRect().top + window.scrollY - 80;
                window.scrollTo({ top: top, behavior: 'smooth' });
            },
            /* ---------- 返回顶部 ---------- */
            scrollToTop: function () {
                window.scrollTo({ top: 0, behavior: 'smooth' });
            },
            /* ---------- 更新激活导航 ---------- */
            updateActiveNav: function () {
                var self = this;
                var bestId = 'ch01';
                var bestTop = Infinity;
                self.allChapters.forEach(function (ch) {
                    var el = document.getElementById(ch.id);
                    if (!el) return;
                    var rect = el.getBoundingClientRect();
                    var top = Math.abs(rect.top - 100);
                    if (rect.top < window.innerHeight / 2 && top < bestTop) {
                        bestTop = top;
                        bestId = ch.id;
                    }
                });
                self.activeNavId = bestId;
            },
            /* ---------- 切换导航面板 ---------- */
            toggleNav: function () {
                this.navOpen = !this.navOpen;
            },
            /* ---------- 复制代码块 ---------- */
            onCopyCode: function (event) {
                /* 委托给全局函数 */
                onCopyMarkdownCodeBlock(event, '');
            }
        },
        mounted: function () {
            var self = this;
            /* 加载 masthead */
            self.loadMasthead();
            /* 初始加载可见章节 */
            self.$nextTick(function () {
                self.loadVisibleChapters();
                self.initReveal();
                self.animateCounters();
            });
            /* 滚动监听 */
            window.addEventListener('scroll', function () {
                if (self.scrollTimer) return;
                self.scrollTimer = setTimeout(function () {
                    self.scrollTimer = null;
                    self.onScroll();
                }, 50);
            }, { passive: true });
            /* 初始执行一次滚动计算 */
            self.onScroll();
            /* 键盘导航 */
            document.addEventListener('keydown', function (e) {
                if (e.key === 'Escape' && self.navOpen) {
                    self.navOpen = false;
                }
            });
        }
    });

    /* ==================== 全局函数暴露 ==================== */
    window.copy2clipboard = function (text) {
        copyToClipboard(text);
    };
})();