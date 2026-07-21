package i2f.springboot.ai.mcp.server.netty.impl;

import i2f.mutator.BaseMutator;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/7/13 11:44
 * @desc
 */
@Data
@NoArgsConstructor
public class NettyHttpSimpleMcpServer implements BaseMutator<NettyHttpSimpleMcpServer> {
    private int port = 23745;
    private int bossThread = 4;
    private int workerThread = 0;
    private int maxContentLength = 65536;
    private HttpSimpleMcpInBoundHandler handler;

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(bossThread);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerThread);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            // 1. HTTP 请求/响应编解码器
                            p.addLast(new HttpServerCodec());
                            // 2. HTTP 消息聚合器（将 HttpRequest + HttpContent 聚合成 FullHttpRequest）
                            p.addLast(new HttpObjectAggregator(maxContentLength));
                            // 3. 自定义的 JSON 业务处理器
                            p.addLast(handler);
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            System.out.println("Netty HTTP Simple Mcp Server started on port: " + port);
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
