package i2f.springboot.ops.common;

import i2f.springboot.ops.util.NetworkUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2025/11/14 8:43
 */
@Data
@NoArgsConstructor
@Component
public class HostIdHelper {
    @Autowired
    protected Environment environment;

    protected AtomicReference<String> hostIpHolder = new AtomicReference<>();
    protected AtomicLong hostIpUpdateTs = new AtomicLong(0L);

    protected AtomicInteger postHolder=new AtomicInteger(0);

    public int getAppPort(){
        int port = postHolder.get();
        if(port>0){
            return port;
        }
        String property = environment.getProperty("server.port", "8080");
        try {
            port=Integer.parseInt(property);
        }catch (Exception e){

        }
        postHolder.set(port);
        return port;
    }

    public String getHostIp(){
        long cts=System.currentTimeMillis();
        if(cts-hostIpUpdateTs.get()<5*60*1000){
            String ret = hostIpHolder.get();
            if(ret!=null){
                return ret;
            }
        }
        List<Map.Entry<InetAddress, NetworkInterface>> list = NetworkUtil.getUsefulAddresses();
        int port = getAppPort();
        int count=0;
        String hostIp =port+"@";
        for (Map.Entry<InetAddress, NetworkInterface> entry : list) {
            hostIp+="["+entry.getKey().getHostAddress() + "#" + entry.getValue().getName()+"]";
            count++;
            if(count==3){
                break;
            }
        }
        hostIpHolder.set(hostIp);
        hostIpUpdateTs.set(cts);
        return hostIpHolder.get();
    }


}
