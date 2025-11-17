package i2f.springboot.ops.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/11/13 19:30
 * @desc
 */
public class NetworkUtil {
    public static List<Map .Entry<InetAddress,NetworkInterface>> getUsefulAddresses(){
        List<Map.Entry<InetAddress,NetworkInterface>> ret=new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()){
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                try {
                    if(!networkInterface.isUp()){
                        continue;
                    }
                    if(networkInterface.isLoopback()){
                        continue;
                    }
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while(inetAddresses.hasMoreElements()){
                        InetAddress inetAddress = inetAddresses.nextElement();
                        try {
                            if(inetAddress.isMulticastAddress()){
                                continue;
                            }
                            if(inetAddress.isLoopbackAddress()){
                                continue;
                            }
                            ret.add(new AbstractMap.SimpleEntry<>(inetAddress,networkInterface));
                        } catch (Exception e) {

                        }
                    }
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {

        }
        ret.sort((v1,v2)->{
            NetworkInterface n1 = v1.getValue();
            NetworkInterface n2 = v2.getValue();
            boolean n1Virtual=isVirtualOrSoftwareInterface(n1);
            boolean n2Virtual=isVirtualOrSoftwareInterface(n2);
            if(n1Virtual == n2Virtual){
                InetAddress k1 = v1.getKey();
                InetAddress k2 = v2.getKey();
                if(k1 instanceof Inet4Address && k2 instanceof Inet4Address){
                    return 0;
                }else if(k1 instanceof Inet4Address){
                    return -1;
                }else{
                    return 1;
                }
            }else if(n1Virtual){
                return 1;
            }else {
                return -1;
            }
        });
        return ret;
    }

    public static boolean isVirtualOrSoftwareInterface(NetworkInterface ni) {
        try {
            if(ni.isVirtual()){
                return true;
            }

            String name = ni.getName().toLowerCase();
            if( name.contains("virtual") ||
                    name.contains("docker") ||
                    name.contains("loop") ||
                    name.contains("dummy")||
                    name.contains("vmware") ||
                    name.contains("vmnet") ||
                    name.contains("veth")){
                return true;
            }

            String displayName = ni.getDisplayName().toLowerCase();
            if( displayName.contains("virtual") ||
                    displayName.contains("docker") ||
                    displayName.contains("loop") ||
                    displayName.contains("dummy")||
                    displayName.contains("vmware") ||
                    displayName.contains("vmnet")||
                    displayName.contains("veth")){
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }
}
