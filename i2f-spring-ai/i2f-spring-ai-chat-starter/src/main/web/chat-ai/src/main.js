import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'

// https://code-farmer-i.github.io/vue-markdown-editor/zh/#%E4%BB%8B%E7%BB%8D
// 编辑markdown
// import VMdEditor from '@kangc/v-md-editor';
// import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';

// highlightjs
import hljs from 'highlight.js';

// VMdEditor.use(githubTheme, {
//
//     Hljs: hljs,
// });

// 预览markdown
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css'

VMdPreview.use(githubTheme, {

    Hljs: hljs,
});

// 高亮行
import createHighlightLinesPlugin from '@kangc/v-md-editor/lib/plugins/highlight-lines/index';
import '@kangc/v-md-editor/lib/plugins/highlight-lines/highlight-lines.css';

// VMdEditor.use(createHighlightLinesPlugin());

VMdPreview.use(createHighlightLinesPlugin());

// 显示行号
import createLineNumbertPlugin from '@kangc/v-md-editor/lib/plugins/line-number/index';

// VMdEditor.use(createLineNumbertPlugin());
VMdPreview.use(createLineNumbertPlugin());

// 显示代码复制按钮
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';

// VMdEditor.use(createCopyCodePlugin());
VMdPreview.use(createCopyCodePlugin());

// emoji 支持
import createEmojiPlugin from '@kangc/v-md-editor/lib/plugins/emoji/index';
import '@kangc/v-md-editor/lib/plugins/emoji/emoji.css';

// VMdEditor.use(createEmojiPlugin());
VMdPreview.use(createEmojiPlugin());


createApp(App)
    // .use(VMdEditor)
    .use(VMdPreview)
    .mount('#app')
