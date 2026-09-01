<template>
    <div>
        <ReadingProgress />
        <AppNav />
        <SideNav />
        <main class="main-content">
            <Masthead />
            <MetaTable />
            <StatGrid />
            <ChapterSection v-for="ch in spec.chapters" :key="ch.id" :chapter="ch" />
            <AppFooter />
        </main>
        <BackToTop />
    </div>
</template>

<header>
    <title>OpenAI 兼容 AI 对话框架 · 技术全景</title>
</header>

<script>
    export default {
        name: 'App',
        components: {
            ReadingProgress: './ReadingProgress.vue',
            AppNav: './AppNav.vue',
            SideNav: './SideNav.vue',
            Masthead: './Masthead.vue',
            MetaTable: './MetaTable.vue',
            StatGrid: './StatGrid.vue',
            ChapterSection: './ChapterSection.vue',
            AppFooter: './AppFooter.vue',
            BackToTop: './BackToTop.vue'
        },
        data: function () {
            return {
                spec: window.$spec,
                state: window.$specState,
                scrollTimer: null
            };
        },
        methods: {
            /* ---------- 滚动处理 ---------- */
            onScroll: function () {
                var self = this;
                var scrollY = window.scrollY;
                var docHeight = document.documentElement.scrollHeight - window.innerHeight;
                if (docHeight > 0) {
                    self.state.progress = Math.round((scrollY / docHeight) * 100);
                }
                self.state.showBackTop = scrollY > 400;
                self.initReveal();
                self.loadVisibleChapters();
                self.updateActiveNav();
            },

            /* ---------- 滚动揭示动画 ---------- */
            initReveal: function () {
                var reveals = document.querySelectorAll('.reveal');
                for (var i = 0; i < reveals.length; i++) {
                    var el = reveals[i];
                    if (el._revealed) continue;
                    el._revealed = true;
                    var rect = el.getBoundingClientRect();
                    if (rect.top < window.innerHeight - 60) {
                        el.classList.add('active');
                    }
                }
            },

            /* ---------- 懒加载可见章节 ---------- */
            loadVisibleChapters: function () {
                var self = this;
                self.spec.chapters.forEach(function (ch) {
                    var el = document.getElementById(ch.id);
                    if (!el) return;
                    var rect = el.getBoundingClientRect();
                    if (rect.top < window.innerHeight * 3 && rect.bottom > -window.innerHeight) {
                        self.loadChapter(ch);
                    }
                });
            },

            /* ---------- 加载单个章节 ---------- */
            loadChapter: function (ch) {
                var self = this;
                if (self.state.chapterHtml[ch.id] || self.state.chapterLoading[ch.id]) return;
                self.state.chapterLoading[ch.id] = true;
                fetch(ch.file)
                    .then(function (r) {
                        if (!r.ok) throw new Error('HTTP ' + r.status);
                        return r.text();
                    })
                    .then(function (text) {
                        self.state.chapterHtml[ch.id] = window.$md.render(text);
                        self.$nextTick(function () {
                            self.initReveal();
                            self.renderMermaidBlocks();
                        });
                    })
                    .catch(function (err) {
                        self.state.chapterError[ch.id] = err.message;
                    })
                    .finally(function () {
                        self.state.chapterLoading[ch.id] = false;
                    });
            },

            /* ---------- 渲染 mermaid 代码块 ---------- */
            renderMermaidBlocks: function () {
                var blocks = document.querySelectorAll('.mermaid-code-block');
                for (var i = 0; i < blocks.length; i++) {
                    var dom = blocks[i];
                    if (dom.innerHTML.trim() !== '') continue;
                    if (dom.chartCode && window.mermaid) {
                        renderMermaid(dom, dom.chartCode);
                    }
                }
            },

            /* ---------- 更新激活导航 ---------- */
            updateActiveNav: function () {
                var self = this;
                var bestId = 'ch01';
                var bestTop = Infinity;
                self.spec.chapters.forEach(function (ch) {
                    var el = document.getElementById(ch.id);
                    if (!el) return;
                    var rect = el.getBoundingClientRect();
                    var top = Math.abs(rect.top - 100);
                    if (rect.top < window.innerHeight / 2 && top < bestTop) {
                        bestTop = top;
                        bestId = ch.id;
                    }
                });
                self.state.activeNavId = bestId;
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
                        self.state.mastheadHtml = window.$md.render(text);
                        self.$nextTick(function () {
                            self.initReveal();
                        });
                    });
            }
        },
        mounted: function () {
            var self = this;
            self.loadMasthead();
            self.$nextTick(function () {
                self.loadVisibleChapters();
                self.initReveal();
            });
            window.addEventListener('scroll', function () {
                if (self.scrollTimer) return;
                self.scrollTimer = setTimeout(function () {
                    self.scrollTimer = null;
                    self.onScroll();
                }, 50);
            }, { passive: true });
            self.onScroll();
            document.addEventListener('keydown', function (e) {
                if (e.key === 'Escape' && self.state.navOpen) {
                    self.state.navOpen = false;
                }
            });
        }
    };
</script>