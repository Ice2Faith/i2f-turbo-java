package i2f.extension.netty.tcp.rpc;

import i2f.extension.netty.tcp.handler.ISocketChannelHandler;
import i2f.extension.netty.tcp.rpc.handler.RpcServerSocketChannelHandler;
import i2f.extension.netty.tcp.tcp.NettyServer;
import i2f.serialize.str.json.IJsonSerializer;

import java.util.function.Function;

/**
 * 提供了RPC的服务端
 * 服务端在RPC中主要是暴露服务
 * 根据客户端的请求调用指定的服务
 * 响应给客户端
 * 实质上是一个运行在TCP之上的RPC处理
 * 因此，继承自NettyServer
 * 但是是针对RPC场景的特殊处理，因此私有化父类构造方法
 * 通过instance方法获取对应的NettyRpcServer实例
 * 使用方法如下：
 * <p>
 * // 初始化服务实例bean提供器
 * InstanceBeanSupplier beanSupplier = new InstanceBeanSupplier();
 * beanSupplier.registerBean(new RpcServiceImpl());
 * <p>
 * // 构造RPC服务端
 * NettyRpcServer server=NettyRpcServer.instance(9999, beanSupplier);
 * <p>
 * // 配置服务端参数
 * server.setEnableHeartBeat(true);
 * server.setLogLevel(LogLevel.DEBUG);
 * server.setEnablePkgFlag(false);
 * <p>
 * // 启动服务端
 * server.start();
 */
public class NettyRpcServer extends NettyServer {
    private IJsonSerializer jsonSerializer;

    public NettyRpcServer(ISocketChannelHandler handler, IJsonSerializer jsonSerializer) {
        super(handler);
        this.jsonSerializer = jsonSerializer;
    }

    public NettyRpcServer(int port, ISocketChannelHandler handler, IJsonSerializer jsonSerializer) {
        super(port, handler);
        this.jsonSerializer = jsonSerializer;
    }

    public static NettyRpcServer instance(Function<Class<?>, Object> beanSupplier, IJsonSerializer jsonSerializer) {
        return new NettyRpcServer(new RpcServerSocketChannelHandler(beanSupplier, jsonSerializer), jsonSerializer);
    }

    public static NettyRpcServer instance(int port, Function<Class<?>, Object> beanSupplier, IJsonSerializer jsonSerializer) {
        return new NettyRpcServer(port, new RpcServerSocketChannelHandler(beanSupplier, jsonSerializer), jsonSerializer);
    }

    public static NettyRpcServer instance(int port, RpcServerSocketChannelHandler handler, IJsonSerializer jsonSerializer) {
        return new NettyRpcServer(port, handler, jsonSerializer);
    }
}
