function createBroadcastMixin() {
    return {
        methods: {
            initBroadcast() {
                this.opsBroadcastChannel = new BroadcastChannel('ops_station_broadcast');

                setInterval(() => {
                    this.hookBroadcastSend(this.opsBroadcastChannel);
                }, 3000)


                this.opsBroadcastChannel.onmessage = (event) => {
                    this.hookBroadcastRecv(event);
                };

            },
            hookBroadcastSend(channel) {
                channel.postMessage({
                    type: 'cert',
                    data: this.metas.cert
                });
            },
            hookBroadcastRecv(event) {
                let data = event.data;
                if (data.type == 'cert') {
                    if (!this.metas.cert || this.metas.cert == '') {
                        this.metas.cert = data.data;
                    }
                }
            }
        }
    }
}