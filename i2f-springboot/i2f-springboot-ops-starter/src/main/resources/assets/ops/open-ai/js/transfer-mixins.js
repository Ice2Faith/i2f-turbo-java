function createTransferMixin() {
    return {
        methods: {
            getTransfer() {
                let cert = this.metas.cert;
                if (!cert || cert.length == 0) {
                    cert = '';
                }
                cert = cert.trim();
                if (!cert || cert.length == 0) {
                    throw new Error('请先填写连接证书')
                }
                let ex = null;
                for (let i = 0; i < 3; i++) {
                    try {
                        let keyPair = OpsSecureHelper.deserializeKeyPair(cert)
                        return new OpsSecureTransfer(keyPair)
                    } catch (e) {
                        ex = e;
                    }
                }
                if (!ex) {
                    ex = new Error('init transfer error')
                }
                throw ex;
            },
            transferSend(req) {
                let ex = null;
                for (let i = 0; i < 5; i++) {
                    try {
                        let transfer = this.getTransfer();
                        return transfer.send(req);
                    } catch (e) {
                        ex = e;
                    }
                }
                if (ex != null) {
                    throw ex;
                }
            },
            transferRecv(req) {
                let ex = null;
                for (let i = 0; i < 5; i++) {
                    try {
                        let transfer = this.getTransfer();
                        return transfer.recv(req);
                    } catch (e) {
                        ex = e;
                    }
                }
                if (ex != null) {
                    throw ex;
                }
            },
        }
    }
}