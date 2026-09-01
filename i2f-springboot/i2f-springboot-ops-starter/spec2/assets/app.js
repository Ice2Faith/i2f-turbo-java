window.app = new Vue({
    components: {},
    mixins: [
        createMarkdownMixin(),
    ],
    data() {
        return {
            sampleMarkdown: getDemoMarkdown()
        }
    },
    created() {

    },
    mounted() {

    },
    watch: {},
    methods: {}
})

let md = setupMarkdown();
window.app.$md = md;
window.app.$mount('#app');