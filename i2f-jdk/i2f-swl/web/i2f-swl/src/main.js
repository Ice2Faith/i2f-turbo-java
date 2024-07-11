import Vue from 'vue'
import App from './App.vue'

import SwlAxios from "@/i2f-turbo-web/i2f-swl/axios/SwlAxios";

Vue.prototype.$axios=SwlAxios

Vue.config.productionTip = false

new Vue({
    render: h => h(App),
}).$mount('#app')
