package i2f.natives.windows.consts;

/**
 * @author Ice2Faith
 * @date 2024/5/14 16:13
 * @desc
 */
public interface WinGetComputerNameExFormat {
    // 本地计算机的 NetBIOS 名称。 如果本地计算机是群集中的节点， lpBuffer 将接收群集虚拟服务器的 NetBIOS 名称。
    int ComputerNameNetBIOS = 0;
    // 本地计算机的 DNS 主机名。 如果本地计算机是群集中的节点， lpBuffer 将接收群集虚拟服务器的 DNS 主机名。
    int ComputerNameDnsHostname = 1;
    // 分配给本地计算机的 DNS 域的名称。 如果本地计算机是群集中的节点， lpBuffer 将接收群集虚拟服务器的 DNS 域名。
    int ComputerNameDnsDomain = 2;
    // 唯一标识本地计算机的完全限定 DNS 名称。 此名称是 DNS 主机名和 DNS 域名的组合，格式为 HostName.DomainName。 如果本地计算机是群集中的节点， lpBuffer 将接收群集虚拟服务器的完全限定 DNS 名称。
    int ComputerNameDnsFullyQualified = 3;
    // 本地计算机的 NetBIOS 名称。 如果本地计算机是群集中的节点， 则 lpBuffer 接收本地计算机的 NetBIOS 名称，而不是群集虚拟服务器的名称。
    int ComputerNamePhysicalNetBIOS = 4;
    // 本地计算机的 DNS 主机名。 如果本地计算机是群集中的节点， lpBuffer 将接收本地计算机的 DNS 主机名，而不是群集虚拟服务器的名称。
    int ComputerNamePhysicalDnsHostname = 5;
    // 分配给本地计算机的 DNS 域的名称。 如果本地计算机是群集中的节点， lpBuffer 将接收本地计算机的 DNS 域名，而不是群集虚拟服务器的名称。
    int ComputerNamePhysicalDnsDomain = 6;
    // 唯一标识计算机的完全限定 DNS 名称。 如果本地计算机是群集中的节点， 则 lpBuffer 接收本地计算机的完全限定 DNS 名称，而不是群集虚拟服务器的名称。
    // 此完全限定的 DNS 名称是 DNS 主机名和 DNS 域名的组合，格式为 HostName.DomainName。
    int ComputerNamePhysicalDnsFullyQualified = 7;
    int ComputerNameMax = 8;
}
