package xiaofei.register;

import java.net.InetSocketAddress;

/**
 * @author xiaofei
 * @create 2021-09-18 15:42
 */
public interface ServiceRegistry {

    public void register(String service, InetSocketAddress inetSocketAddress);
}
