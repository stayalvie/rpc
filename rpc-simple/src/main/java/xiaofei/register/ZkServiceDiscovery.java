package xiaofei.register;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CuratorHelper;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author xiaofei
 * @create 2021-09-21 15:38
 *
 *
 * 客户端使用
 *
 */
public class ZkServiceDiscovery implements ServiceDiscovery{

    private static final Logger logger = LoggerFactory.getLogger(CuratorHelper.class);
    private final CuratorFramework zkClient;

    public ZkServiceDiscovery() {
        zkClient = CuratorHelper.getZkClient();
        zkClient.start();
    }


    /*
     * 多个服务注册提供服务，
     * TODO: 如何实现负载均衡， 策略
     * */
    @Override
    public InetSocketAddress discoveryService(String serviceName) {
        logger.info("查找的服务为：{}", serviceName);
        List<String> services = CuratorHelper.getChildrenNodes(zkClient, serviceName);
        if (services == null || services.isEmpty()) {
            logger.info("没有注册此服务：{}", serviceName);
            return null;
        }
        String serviceAddress = services.get(0);
        logger.info("成功找到服务地址:{}", serviceAddress);
        return new InetSocketAddress(serviceAddress.split(":")[0], Integer.parseInt(serviceAddress.split(":")[1]));
    }
}
