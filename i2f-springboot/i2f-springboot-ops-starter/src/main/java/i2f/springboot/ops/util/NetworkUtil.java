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
    public static void main(String[] args){
        List<Map.Entry<InetAddress, NetworkInterface>> list = getUsefulAddresses();
        for (Map.Entry<InetAddress, NetworkInterface> entry : list) {
            System.out.println("===========");
            System.out.println(entry.getKey().getHostAddress());
            System.out.println(entry.getValue().getName());
        }
    }
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
            if(n1.isVirtual() == n2.isVirtual()){
                InetAddress k1 = v1.getKey();
                InetAddress k2 = v2.getKey();
                if(k1 instanceof Inet4Address && k2 instanceof Inet4Address){
                    return 0;
                }else if(k1 instanceof Inet4Address){
                    return -1;
                }else{
                    return 1;
                }
            }else if(n1.isVirtual()){
                return 1;
            }else {
                return -1;
            }
        });
        return ret;
    }
}
