package xiaofei.handler;

import dto.RpcRequest;
import dto.RpcResponse;
import enumration.RpcResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xiaofei.provider.ServiceProvider;
import xiaofei.provider.impl.ServiceProviderImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @author xiaofei
 * @create 2021-09-18 15:28
 */

public class RpcRequestHandler {

    private static Logger logger = LoggerFactory.getLogger(RpcRequest.class);
    private static final ServiceProvider SERVICE_PROVIDER;

    static {
        SERVICE_PROVIDER = new ServiceProviderImpl();
    }

    /*-
    * 处理调用方法
    * */
    public Object handler(RpcRequest rpcRequest) {
        Object result = null;

        try {
            Object service = SERVICE_PROVIDER.getServiceProvider(rpcRequest.getInterfaceName());
            result = invokeTargetMethod(rpcRequest, service);
            logger.info("service:{} successful invoke method:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("occur exception: {}", e);
        }

        return result;
    }

    /*
    * 实际的调用
    * */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        if (null == method) {
            return RpcResponse.fail(RpcResponseCode.NOT_FOUND_METHOD, rpcRequest.getRequestId());
        }
        return method.invoke(service, rpcRequest.getParameters());
    }


}
