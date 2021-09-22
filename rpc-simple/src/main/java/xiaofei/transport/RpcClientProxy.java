package xiaofei.transport;


import dto.RpcRequest;
import dto.RpcResponse;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.UUIDGenerator;
import xiaofei.transport.Socket.SocketRpcClient;
import xiaofei.transport.netty.client.RpcClientManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author xiaofei
 * @createTime 2021年09月17日 18:01:00
 */
public class RpcClientProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    private ClientTransport rpcClient;

    public RpcClientProxy(ClientTransport rpcClient) {
        this.rpcClient = rpcClient;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        logger.info("Call invoke method and invoked method: {}", method.getName());
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .build();
        Object result = null;
        if (rpcClient instanceof RpcClientManager) {
            CompletableFuture<RpcResponse> completableFuture = (CompletableFuture<RpcResponse>) rpcClient.sendRpcRequest(rpcRequest);
            result = completableFuture.get().getData();
        }
        if (rpcClient instanceof SocketRpcClient) {
            RpcResponse rpcResponse = (RpcResponse) rpcClient.sendRpcRequest(rpcRequest);
            result = rpcResponse.getData();
        }
        return result;
    }
}
