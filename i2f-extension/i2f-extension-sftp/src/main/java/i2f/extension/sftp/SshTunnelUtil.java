package i2f.extension.sftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.*;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 建立SSH隧道
 * 原理就是在运行本程序的机器上，使用一个端口正向代理目标主机的端口的流量
 * 这个代理是基于TCP/IP层面的，因此可以用于代理数据库连接等这种TCP协议的连接
 * 当然，用来代理HTTP也是可以的
 * 现在，就有三个角色的主机参与
 * local 本地主机，也就是你在哪里运行了本程序，哪里就是local本机
 * ssh 连接的隧道代理机，也就是你SSH的主机
 * remote 这个就是SSH那台主机连接的远程主机，多以remote的地址是相对于ssh主机来说的
 * 流量代理路径
 * localHost:localPort -> sshHost:sshPort -> remoteHost:remotePort
 * ------------------------------------
 * 使用案例
 * SshTunnelUtil tunnel = new SshTunnelUtil("10.12.x.1", "web", "123xxxx")
 *                     .createTunnel(3306, "10.4.x.7", 3306)
 *                     .createTunnel(16379, "10.8.x.3", 6379)
 *                     .setup();
 * 在这个例子中
 * 我们以 10.12.x.1 机器作为隧道跳板机
 * 然后，在 10.12.x.1 机器上，能够访问到 10.4.x.7:3306 这个Mysql服务
 * 所以，我们建立一个通道 localhost:3306 -> 10.4.x.7:3306
 * 这样，我们访问 localhost:3306 就是在访问 10.4.x.7:3306
 * 同理
 * 我们建立了另一个通道 localhost:16379 -> 10.8.x.3:6379
 * 这样，我们访问 localhost:16379 就是在访问 10.8.x.3:6379
 * ------------------------------------
 * 如果你在程序中使用来对数据库等链接做隧道的话
 * 你应该要在建立数据库连接之前建立隧道
 * 也就是说，应该在main中进行隧道的建立
 * 特别是在springboot中，则一定要确保隧道在自动配置之前
 * 可以考虑在入口类的main中建立隧道，再startup/run程序
 *
 * @author Ice2Faith
 * @date 2025/8/25 15:43
 */
@Data
@NoArgsConstructor
public class SshTunnelUtil {
    @Getter(AccessLevel.NONE)
    protected transient Session session;

    protected String sshHost;
    protected int sshPort=22;
    protected String sshUser;
    protected String sshPassword;
    protected String knownHostFilePath;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected transient final ConcurrentHashMap<Integer, Map.Entry<String,Integer>> tunnels=new ConcurrentHashMap<>();
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected transient final AtomicBoolean setup =new AtomicBoolean(false);

    protected final AtomicInteger keepaliveSeconds=new AtomicInteger(15);

    public SshTunnelUtil(String sshHost, String sshUser, String sshPassword) {
        this.sshHost = sshHost;
        this.sshUser = sshUser;
        this.sshPassword = sshPassword;
    }

    public SshTunnelUtil(String sshHost, int sshPort, String sshUser, String sshPassword) {
        this.sshHost = sshHost;
        this.sshPort = sshPort;
        this.sshUser = sshUser;
        this.sshPassword = sshPassword;
    }

    public SshTunnelUtil setup(){
        if(setup.getAndSet(true)){
            return this;
        }
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    keepaliveTunnels();
                } catch (Exception e) {

                }
                try {
                    TimeUnit.SECONDS.sleep(keepaliveSeconds.get());
                } catch (InterruptedException e) {

                }
            }
        });
        thread.setDaemon(true);
        thread.setName("ssh-tunnel");
        thread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                closeTunnel();
            } catch (Exception e) {

            }
        }));
        return this;
    }

    public boolean isInvalidSession(){
        return this.session == null || !this.session.isConnected();
    }

    public Session getSession() throws JSchException  {
        if(isInvalidSession()){
            synchronized (this) {
                if(isInvalidSession()) {
                    JSch jSch = new JSch();
                    if (knownHostFilePath != null && !knownHostFilePath.isEmpty()) {
                        jSch.setKnownHosts(knownHostFilePath);
                    }

                    session = jSch.getSession(sshUser, sshHost, sshPort > 0 ? sshPort : 22);
                    session.setPassword(sshPassword);
                    session.setConfig("StrictHostKeyChecking", "no");

                    session.connect();
                }
            }
        }
        return session;
    }

    public synchronized SshTunnelUtil createTunnel(int localPort,String remoteHost,int remotePort) throws Exception {
        Session session = getSession();
        if(tunnels.containsKey(localPort)){
            try {
                session.delPortForwardingL(localPort);
            } catch (JSchException e) {

            }
        }
        tunnels.put(localPort,new AbstractMap.SimpleEntry<>(remoteHost,remotePort));
        session.setPortForwardingL(localPort, remoteHost, remotePort);
        return this;
    }

    public synchronized void closeTunnel() throws Exception {
        if(isInvalidSession()){
            return;
        }
        for (Map.Entry<Integer, Map.Entry<String, Integer>> entry : tunnels.entrySet()) {
            Integer localPort = entry.getKey();
            try {
                session.delPortForwardingL(localPort);
            } catch (JSchException e) {

            }
        }
        session.disconnect();
    }

    public SshTunnelUtil keepaliveTunnels() throws Exception {
        if(tunnels.isEmpty()){
            return this;
        }
        if(!isInvalidSession()){
            return this;
        }
        for (Map.Entry<Integer, Map.Entry<String, Integer>> entry : tunnels.entrySet()) {
            createTunnel(entry.getKey(),entry.getValue().getKey(),entry.getValue().getValue());
        }
        return this;
    }
}
