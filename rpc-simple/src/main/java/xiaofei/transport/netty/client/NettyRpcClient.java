package xiaofei.transport.netty.client;

import dto.RpcRequest;
import dto.RpcResponse;
import enumration.RpcErrorMessageEnum;
import exception.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xiaofei.serializer.impl.KryoSerializer;
import xiaofei.transport.codc.kryo.NettyKryoDecoder;
import xiaofei.transport.codc.kryo.NettyKryoEncoder;
import xiaofei.transport.netty.NettyRpcResponseHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

/**
 * @author xiaofei
 * @create 2021-09-19 15:38
 */
public class NettyRpcClient{
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcClient.class);
    private static final Bootstrap b;
    private static EventLoopGroup eventLoopGroup;

    // 初始化相关资源比如 EventLoopGroup、Bootstrap
    static {
        eventLoopGroup = new NioEventLoopGroup();
        b = new Bootstrap();
        KryoSerializer kryoSerializer = new KryoSerializer();
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        /*自定义序列化编解码器*/
                        // RpcResponse -> ByteBuf
                        ch.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcResponse.class));
                        // ByteBuf -> RpcRequest
                        ch.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcRequest.class));
                        ch.pipeline().addLast(new NettyRpcResponseHandler());
                    }
                });
    }

    public static void close() {
        logger.info("call close method");
        eventLoopGroup.shutdownGracefully();
    }

    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        ChannelFuture future = null;
        try {
            future = b.connect(inetSocketAddress).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (future == null) {
            new RpcException(RpcErrorMessageEnum.SERVER_CONNECT_FAIL);
        }
        return future.channel();
    }
}