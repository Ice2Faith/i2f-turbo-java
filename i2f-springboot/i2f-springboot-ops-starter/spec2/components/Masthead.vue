<template>
    <div>
        <section class="masthead" v-if="mastheadHtml" v-html="mastheadHtml"></section>
        <section class="masthead masthead-loading" v-else>
            <div class="loading-spinner"><i class="el-icon-loading"></i> 加载中...</div>
        </section>
    </div>
</template>

<script>
    export default {
        name: 'Masthead',
        computed: {
            mastheadHtml: function () {
                return window.$specState.mastheadHtml;
            }
        }
    };
</script>