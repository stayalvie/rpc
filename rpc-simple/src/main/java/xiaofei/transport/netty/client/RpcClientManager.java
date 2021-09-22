package xiaofei.transport.netty.client;

import dto.RpcMessageChecker;
import dto.RpcRequest;
import dto.RpcResponse;
import factory.SingletonFactory;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xiaofei
 * @create 2021-09-19 18:27
 */
public class RpcClientManager implements ClientTransport {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientManager.class);
    private final ServiceDiscovery serviceDiscovery;
    private final UnprocessedRequests unprocessedRequests;

    public RpcClientManager() {
        serviceDiscovery = new ZkServiceDiscovery();
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    /*
    * send rpcRequest and return result
    * */
    @Override
    public CompletableFuture<RpcResponse> sendRpcRequest(RpcRequest rpcRequest) {
        // 构建返回值
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        InetSocketAddress inetSocketAddress = serviceDiscovery.discoveryService(rpcRequest.getInterfaceName());
        Channel channel = ChannelProvider.get(inetSocketAddress);
        if (channel != null && channel.isActive()) {
            // 放入未处理的请求
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    logger.info("client send message: {}", rpcRequest);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    logger.error("Send failed:", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }

        return resultFuture;
    }

}
