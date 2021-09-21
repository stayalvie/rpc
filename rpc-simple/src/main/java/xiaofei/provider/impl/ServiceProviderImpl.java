package xiaofei.provider.impl;

import enumration.RpcErrorMessageEnum;
import exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xiaofei.provider.ServiceProvider;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaofei
 * @create 2021-09-20 17:08
 *
 * 本服务器的服务提供中心
 */
public class ServiceProviderImpl implements ServiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);

    /*
     * 服务注册的中心 目前是单机所以用map TODO 目前不支持一个接口多个实现, map的key不能
     *
     * key  : 接口的名称
     * value: 实际的接口实现
     * */
    private static Map<String, Object> serviceFactory = new ConcurrentHashMap<>();

    /*
     * 线程安全的set类， 底层就是ConcurrentHashMap
     * */
    private static Set<String> registeredName = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void addServiceProvider(T service) {
        String serviceName = service.getClass().getName();
        if (registeredName.contains(serviceName)) {
            return;
        }
        registeredName.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        //将这个service的所有实现的接口都添加到注册中心的。
        for (Class<?> i : interfaces) {
            serviceFactory.put(i.getCanonicalName(), service);
        }
        logger.info("Add service: {} and interfaces:{}", serviceName, service.getClass().getInterfaces());
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceFactory.get(serviceName);
        if (null == service) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }
}
