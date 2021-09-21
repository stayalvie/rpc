package xiaofei.provider;

/**
 * @author xiaofei
 * @create 2021-09-20 17:07
 *
 * 保存和提供服务实例对象。服务端使用。
 *
 */
public interface ServiceProvider {

    /**
     * 保存服务提供者
     */
    <T> void addServiceProvider(T service);

    /**
     * 获取服务提供者
     */
    Object getServiceProvider(String serviceName);


}
