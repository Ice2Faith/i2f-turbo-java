package i2f.net;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/13 19:30
 * @desc
 */
public class NetworkUtil {
    // 常见虚拟网卡关键字（强虚拟）
    private static final String[] VIRTUAL_NAMES = {
            "docker", "cni", "cali", "flannel", "weave", "podman", "kube-ipvs", "cilium",
            "virbr", "vnet", "vboxnet", "vmnet", "vmx", "vmware", "vethernet", "xenbr", "vif",
            "dummy",
            "macvlan", "ipvlan"
    };

    // 弱虚拟网卡（可能误报或 VPN 类）
    private static final String[] VIRTUAL_WEAK_NAMES = {
            "br-", "vwnet",
            "tun", "tap", "utun", "wg",
            "zt", "tailscale", "ts", "openvpn", "ovpn", "ipsec",
            "bond", "team",
            "vxlan", "geneve", "gre", "gretap", "ipip", "sit", "erspan"
    };

    /**
     * 判断是否为强虚拟网卡
     */
    public static boolean isVirtualInterface(String name) {
        String lower = name.toLowerCase();
        for (String kw : VIRTUAL_NAMES) {
            if (lower.contains(kw)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为弱虚拟网卡
     */
    public static boolean isVirtualWeakInterface(String name) {
        String lower = name.toLowerCase();
        for (String kw : VIRTUAL_WEAK_NAMES) {
            if (lower.contains(kw)) {
                return true;
            }
        }
        return false;
    }

    /**
     * IP 条目，包含网卡名、IP 地址及虚拟标识
     */
    @Data
    @NoArgsConstructor
    public static class IpEntry {
        protected NetworkInterface networkInterface;
        protected InetAddress inetAddress;

        protected String interfaceName;
        protected String ip;
        protected boolean ipv6;
        protected boolean virtual;
        protected boolean weakVirtual;

        public IpEntry(NetworkInterface networkInterface, InetAddress inetAddress) {
            this.networkInterface = networkInterface;
            this.inetAddress = inetAddress;
            this.resolveProperties();
        }

        public void resolveProperties() {
            if (networkInterface != null) {
                interfaceName = networkInterface.getDisplayName();
            }
            if (inetAddress != null) {
                ip = inetAddress.getHostAddress();
                ipv6 = inetAddress instanceof Inet6Address;
            }
            if (interfaceName != null) {
                virtual = isVirtualInterface(interfaceName);
                weakVirtual = isVirtualWeakInterface(interfaceName);
            }
        }
    }

    /**
     * 获取系统当前正在使用的出口 IP（最可靠）
     */
    public static String getPreferredIp() {
        // 尝试通过 UDP 连接 8.8.8.8:80 获取本地地址
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 80);
            socket.setSoTimeout(2000);
            InetAddress local = socket.getLocalAddress();
            if (local instanceof Inet4Address) {
                return local.getHostAddress();
            }
        } catch (Exception e) {
            // 忽略异常，继续降级方案
        }

        // 降级方案：取 getAllIpList 排序后的第一个 IP
        List<IpEntry> entries = getUsefulAddresses();
        if (!entries.isEmpty()) {
            return entries.get(0).getIp();
        }
        return null;
    }

    /**
     * 获取第一个有用的网卡地址
     */
    public static IpEntry getFirstUsefulAddress() {
        List<IpEntry> list = getUsefulAddresses();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 获取所有 up 状态、非 loopback 的 IPv4 地址（包含虚拟网卡），
     * 返回按 [非虚拟 → 弱虚拟 → 强虚拟] 排序的列表，同组内按网卡名排序。
     */
    public static List<IpEntry> getUsefulAddresses() {
        List<IpEntry> ret = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                try {
                    if (!networkInterface.isUp()) {
                        continue;
                    }
                    if (networkInterface.isLoopback()) {
                        continue;
                    }
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        try {
                            if (inetAddress.isMulticastAddress()) {
                                continue;
                            }
                            if (inetAddress.isLoopbackAddress()) {
                                continue;
                            }
                            ret.add(new IpEntry(networkInterface, inetAddress));
                        } catch (Exception e) {

                        }
                    }
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {

        }

        // 排序规则：非虚拟网卡在前，非弱虚拟网卡在前，网卡名称正序，ipv4在前
        ret.sort(Comparator
                .comparing(IpEntry::isVirtual)
                .thenComparing(IpEntry::isWeakVirtual)
                .thenComparing(IpEntry::getInterfaceName)
                .thenComparing(IpEntry::isIpv6)
        );
        return ret;
    }
}
