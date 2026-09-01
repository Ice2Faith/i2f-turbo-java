<template>
    <section class="stat-section reveal">
        <div class="stat-grid">
            <div class="stat-item" v-for="(s, i) in spec.stats" :key="i" :style="{ '--sc': s.color }">
                <span class="stat-count" :data-target="s.count" ref="counters">0</span>
                <span class="stat-label">{{ s.label }}</span>
            </div>
        </div>
    </section>
</template>

<script>
    export default {
        name: 'StatGrid',
        data: function () {
            return {
                _animated: false
            };
        },
        computed: {
            spec: function () {
                return window.$spec;
            }
        },
        methods: {
            animateCounters: function () {
                var self = this;
                if (self._animated) return;
                var el = self.$el;
                if (!el) return;
                var rect = el.getBoundingClientRect();
                if (rect.top < window.innerHeight && rect.bottom > 0) {
                    self._animated = true;
                    var counters = el.querySelectorAll('.stat-count');
                    for (var i = 0; i < counters.length; i++) {
                        (function (counterEl) {
                            var target = parseInt(counterEl.getAttribute('data-target'));
                            var duration = 1200;
                            var start = null;
                            function step(ts) {
                                if (!start) start = ts;
                                var progress = Math.min((ts - start) / duration, 1);
                                var ease = 1 - Math.pow(1 - progress, 3);
                                counterEl.textContent = Math.floor(ease * target);
                                if (progress < 1) {
                                    requestAnimationFrame(step);
                                } else {
                                    counterEl.textContent = target;
                                }
                            }
                            requestAnimationFrame(step);
                        })(counters[i]);
                    }
                }
            },
            checkAndAnimate: function () {
                this.animateCounters();
            }
        },
        mounted: function () {
            var self = this;
            self.$nextTick(function () {
                self.animateCounters();
            });
            window.addEventListener('scroll', function () {
                self.animateCounters();
            }, { passive: true });
        }
    };
</script>