/* ============================================================
   i2f-springboot-ops-starter · OpenAI 子系统技术全景
   交互逻辑：滚动揭示 / 导航高亮 / 计数动画 / 代码复制 / 进度条
   零依赖原生 JS，可离线运行
   ============================================================ */
(function () {
    'use strict';

    /* ---------- 1. 滚动揭示动画 ---------- */
    var revealEls = document.querySelectorAll('.reveal, .reveal-l');
    if ('IntersectionObserver' in window) {
        var revealObserver = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (entry.isIntersecting) {
                    entry.target.classList.add('in');
                    revealObserver.unobserve(entry.target);
                }
            });
        }, {threshold: 0.08, rootMargin: '0px 0px -40px 0px'});
        revealEls.forEach(function (el) {
            revealObserver.observe(el);
        });
    } else {
        // 降级：直接显示
        revealEls.forEach(function (el) {
            el.classList.add('in');
        });
    }

    /* ---------- 2. 侧边导航滚动高亮 ---------- */
    var tocLinks = document.querySelectorAll('.side-nav a.toc');
    var chapters = [];
    tocLinks.forEach(function (link) {
        var id = link.getAttribute('href').slice(1);
        var sec = document.getElementById(id);
        if (sec) {
            chapters.push({id: id, el: sec, link: link});
        }
    });

    function updateActiveToc() {
        var scrollPos = window.scrollY + 120;
        var current = null;
        for (var i = 0; i < chapters.length; i++) {
            if (chapters[i].el.offsetTop <= scrollPos) {
                current = chapters[i];
            } else {
                break;
            }
        }
        tocLinks.forEach(function (l) {
            l.classList.remove('active');
        });
        if (current) {
            current.link.classList.add('active');
        }
    }

    /* ---------- 3. 阅读进度条 + 返回顶部 ---------- */
    var progressBar = document.getElementById('readProgress');
    var backTop = document.getElementById('backTop');

    function updateProgress() {
        var docHeight = document.documentElement.scrollHeight - window.innerHeight;
        var ratio = docHeight > 0 ? (window.scrollY / docHeight) * 100 : 0;
        if (progressBar) {
            progressBar.style.width = ratio + '%';
        }
        if (backTop) {
            if (window.scrollY > 480) {
                backTop.classList.add('show');
            } else {
                backTop.classList.remove('show');
            }
        }
    }

    if (backTop) {
        backTop.addEventListener('click', function () {
            window.scrollTo({top: 0, behavior: 'smooth'});
        });
    }

    /* 节流滚动事件 */
    var scrollTicking = false;
    window.addEventListener('scroll', function () {
        if (!scrollTicking) {
            scrollTicking = true;
            window.requestAnimationFrame(function () {
                updateActiveToc();
                updateProgress();
                scrollTicking = false;
            });
        }
    }, {passive: true});

    updateActiveToc();
    updateProgress();

    /* ---------- 4. 统计数字计数动画 ---------- */
    var counters = document.querySelectorAll('.stats-strip .num[data-count]');

    function animateCounter(el) {
        var target = parseInt(el.getAttribute('data-count'), 10) || 0;
        var duration = 1200;
        var startTime = null;

        function tick(ts) {
            if (!startTime) {
                startTime = ts;
            }
            var progress = Math.min((ts - startTime) / duration, 1);
            // easeOutCubic
            var eased = 1 - Math.pow(1 - progress, 3);
            el.textContent = Math.round(eased * target);
            if (progress < 1) {
                window.requestAnimationFrame(tick);
            } else {
                el.textContent = target;
            }
        }

        window.requestAnimationFrame(tick);
    }

    if ('IntersectionObserver' in window && counters.length) {
        var counterObserver = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (entry.isIntersecting) {
                    animateCounter(entry.target);
                    counterObserver.unobserve(entry.target);
                }
            });
        }, {threshold: 0.4});
        counters.forEach(function (el) {
            counterObserver.observe(el);
        });
    } else {
        counters.forEach(function (el) {
            el.textContent = el.getAttribute('data-count');
        });
    }

    /* ---------- 5. 代码块复制按钮 ---------- */
    var copyBtns = document.querySelectorAll('.code-block .copy-btn');
    copyBtns.forEach(function (btn) {
        btn.addEventListener('click', function () {
            var block = btn.closest('.code-block');
            var pre = block ? block.querySelector('pre') : null;
            if (!pre) {
                return;
            }
            var text = pre.innerText;
            var done = function () {
                var old = btn.textContent;
                btn.textContent = 'COPIED ✓';
                btn.style.borderColor = '#2b8a3e';
                btn.style.color = '#b2f2bb';
                setTimeout(function () {
                    btn.textContent = old;
                    btn.style.borderColor = '';
                    btn.style.color = '';
                }, 1500);
            };
            if (navigator.clipboard && navigator.clipboard.writeText) {
                navigator.clipboard.writeText(text).then(done, function () {
                    fallbackCopy(text);
                    done();
                });
            } else {
                fallbackCopy(text);
                done();
            }
        });
    });

    function fallbackCopy(text) {
        var ta = document.createElement('textarea');
        ta.value = text;
        ta.style.position = 'fixed';
        ta.style.opacity = '0';
        document.body.appendChild(ta);
        ta.select();
        try {
            document.execCommand('copy');
        } catch (e) { /* ignore */
        }
        document.body.removeChild(ta);
    }

    /* ---------- 6. 顶部状态条时钟（细节点缀） ---------- */
    var stripRight = document.querySelector('.boot-strip .right');
    if (stripRight) {
        var base = stripRight.textContent;
        setInterval(function () {
            var d = new Date();
            var hh = ('0' + d.getHours()).slice(-2);
            var mm = ('0' + d.getMinutes()).slice(-2);
            stripRight.textContent = base + ' · ' + hh + ':' + mm;
        }, 30000);
    }
})();
