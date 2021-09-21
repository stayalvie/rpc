package xiaofei.transport.netty.client;

import dto.RpcMessageChecker;
import dto.RpcRequest;
import dto.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xiaofei.provider.ServiceProvider;
import xiaofei.register.ServiceDiscovery;
import xiaofei.register.ServiceRegistry;
import xiaofei.register.ZkServiceDiscovery;
import xiaofei.register.ZkServiceRegistry;
import xiaofei.transport.ClientTransport;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xiaofei
 * @create 2021-09-19 18:27
 */
public class RpcClientManager implements ClientTransport {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientManager.class);
    private static ServiceDiscovery serviceDiscovery;

    static {
        serviceDiscovery = new ZkServiceDiscovery();
    }

    /*
    * send rpcRequest and return result
    * */
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        Object result = null;
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.discoveryService(rpcRequest.getInterfaceName());
            if (inetSocketAddress == null) {
                logger.info("此服务没有找到:{}", rpcRequest.getInterfaceName());
                return null;
            }
            Channel channel = ChannelProvider.get(inetSocketAddress);
            if (channel.isActive()) {
                channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        logger.info("client send message: {}", rpcRequest);
                    } else {
                        future.channel().close();
                        logger.error("Send failed:", future.cause());
                    }
                });

                channel.closeFuture().sync();
                // TODO 这个地方绝对是个优化的点
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
                RpcResponse rpcResponse = channel.attr(key).get();

                logger.info("client get rpcResponse from channel:{}", rpcResponse);
                //校验 RpcResponse 和 RpcRequest
                RpcMessageChecker.check(rpcResponse, rpcRequest);
                result = rpcResponse.getData();
            } else {
                System.exit(0);
            }

        } catch (InterruptedException e) {
            logger.error("occur exception when send rpc message from client:", e);
        }
        return result;
    }

}
