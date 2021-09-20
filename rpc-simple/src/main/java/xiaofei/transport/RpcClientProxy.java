package xiaofei.transport;


import dto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.UUIDGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        logger.info("Call invoke method and invoked method: {}", method.getName());
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .requestId(UUIDGenerator.UUID_Generator())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .build();
        return rpcClient.sendRpcRequest(rpcRequest);
    }
}
