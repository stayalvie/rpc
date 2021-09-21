package xiaofei.register;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CuratorHelper;

import java.net.InetSocketAddress;

/**
 * @author xiaofei
 * @create 2021-09-20 16:54
 *
 *
 * 服务的注册中心
 *
 * 客户端 得到： ip : 端口
 *  服务端 ： 发布服务
 */
public class ZkServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(CuratorHelper.class);
    private final CuratorFramework zkClient;

    public ZkServiceRegistry() {
        zkClient = CuratorHelper.getZkClient();
        zkClient.start();
    }

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        //根节点下注册子节点：服务
        StringBuilder servicePath = new StringBuilder(CuratorHelper.ZK_REGISTER_ROOT_PATH).append("/").append(serviceName);
        //服务子节点下注册子节点：服务地址
        servicePath.append(inetSocketAddress.toString());
        CuratorHelper.createEphemeralNode(zkClient, servicePath.toString());
        logger.info("节点创建成功，节点为:{}", servicePath);
    }
}
