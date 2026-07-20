function createStoreDataMixin() {
    return {
        methods: {
            getDataKey() {
                return 'ops:open-ai-chat:data'
            },
            loadData() {
                let content = localStorage.getItem(this.getDataKey());
                if (!content) {
                    return
                }
                content = content.trim();
                if (content.length == 0) {
                    return;
                }
                try {
                    let obj = JSON.parse(content);
                    Object.keys(obj).forEach(k => {
                        this.$data[k] = obj[k];
                    });
                } catch (e) {

                }
            },
            storeData() {
                try {
                    let data = {...this.$data};
                    data.opsBroadcastChannel = null;
                    data.langEditors = {};
                    Object.keys(this.$data.langEditors).forEach(k => {
                        data.langEditors[k] = null;
                    })
                    let json = JSON.stringify(data);
                    let obj = JSON.parse(json);
                    obj.metas.cert = '';
                    obj.form.meta.apiKey = '';
                    let content = JSON.stringify(obj);
                    localStorage.setItem(this.getDataKey(), content);
                } catch (e) {
                    // console.log(e)
                }
            },
            initStoreData() {
                let reset = new URL(window.location.href).searchParams.get('reset');
                if (reset != 'true') {
                    this.loadData();
                }
                setInterval(() => {
                    this.storeData();
                }, 1000);
            },
            doResetData() {
                let newData = this.getDefaultData();
                Object.keys(newData).forEach(k => {
                    this.$data[k] = newData[k];
                })
            },
        }
    }
}