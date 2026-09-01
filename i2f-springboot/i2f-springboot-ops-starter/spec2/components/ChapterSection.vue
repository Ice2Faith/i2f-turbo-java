<template>
    <section :id="chapter.id" class="chapter">
        <div class="chapter-header reveal">
            <span class="ch-no">{{ chapter.no }}</span>
            <div class="ch-title-group">
                <span class="ch-en">{{ chapter.en }}</span>
                <h2 class="ch-title">{{ chapter.title }}</h2>
            </div>
        </div>
        <div class="chapter-body">
            <div v-if="loading" class="chapter-loading">
                <i class="el-icon-loading"></i> 加载章节内容...
            </div>
            <div v-else-if="error" class="chapter-error">
                <i class="el-icon-warning"></i> 加载失败：{{ error }}
            </div>
            <div v-else-if="html" v-html="html" class="chapter-html"></div>
            <div v-else class="chapter-placeholder"></div>
        </div>
    </section>
</template>

<script>
    export default {
        name: 'ChapterSection',
        props: {
            chapter: {
                type: Object,
                required: true
            }
        },
        computed: {
            html: function () {
                return window.$specState.chapterHtml[this.chapter.id];
            },
            loading: function () {
                return window.$specState.chapterLoading[this.chapter.id];
            },
            error: function () {
                return window.$specState.chapterError[this.chapter.id];
            }
        }
    };
</script>