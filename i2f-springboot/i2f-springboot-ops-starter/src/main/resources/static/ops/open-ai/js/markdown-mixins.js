function createMarkdownMixin() {
    return {
        methods: {
            // 渲染 Markdown（流式输出期间增量内容也可安全解析）
            renderMarkdown(content) {
                if (!content) {
                    return '';
                }
                if (!this.$md) {
                    return content.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
                }
                try {
                    return this.$md.render(content);
                } catch (e) {
                    return content;
                }
            },
        }
    }
}