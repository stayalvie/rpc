package xiaofei.register;

import java.net.InetSocketAddress;

/**
 * @author xiaofei
 * @create 2021-09-21 15:36
 *
 *
 * 将之前的一个接口分离开
 * 一个交给客户端
 * 一个交给服务端
 */
public interface ServiceDiscovery {


    public InetSocketAddress discoveryService(String serviceName);


}
