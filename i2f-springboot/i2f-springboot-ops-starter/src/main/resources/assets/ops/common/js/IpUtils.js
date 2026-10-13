function isPrivateIPv4(ip) {
    if(!ip || ip===''){
        return false;
    }
    const parts = ip.split('.').map(Number);
    if (parts.length !== 4 || parts.some(n => Number.isNaN(n) || n < 0 || n > 255)) {
        return false;
    }

    const [a, b] = parts;

    // 10.0.0.0/8
    if (a === 10) {
        return true;
    }

    // 172.16.0.0/12
    if (a === 172 && b >= 16 && b <= 31) {
        return true;
    }

    // 192.168.0.0/16
    if (a === 192 && b === 168) {
        return true;
    }

    // 127.0.0.0/8
    if (a === 127) {
        return true;
    }

    return false;
}

function isIntranetHost(hostname) {
    if(!hostname || hostname===''){
        return false;
    }

    // localhost / 127.0.0.1
    if (hostname === 'localhost' || hostname === '127.0.0.1' || hostname === '[::1]') {
        return true;
    }

    // IPv6 形式
    if (hostname.startsWith('[') && hostname.endsWith(']')) {
        const ip = hostname.slice(1, -1);
        if (ip === '::1') {
            return true;
        }
        if (ip.startsWith('fe80:')) {
            return true;
        } // 链路本地
        if (ip.startsWith('fc') || ip.startsWith('fd')) {
            return true;
        } // ULA
        return false;
    }

    // 普通 IPv4
    if (/^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/.test(hostname)) {
        return isPrivateIPv4(hostname);
    }

    return false;
}

function isIntranetPage(){
    return isIntranetHost(window.location.hostname)
}