package xiaofei.transport.netty.client;

import enumration.RpcErrorMessageEnum;
import exception.RpcException;
import factory.SingletonFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import javafx.beans.binding.MapExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaofei
 * @create 2021-09-19 18:49
 */
public class ChannelProvider {

    private static final Logger logger = LoggerFactory.getLogger(ChannelProvider.class);
    private static NettyRpcClient nettyClient;
    private static Map<String, Channel> channels = new ConcurrentHashMap<>();

    static {
        nettyClient = SingletonFactory.getInstance(NettyRpcClient.class);
    }

    /**
     * 判断有没有Channel了
     */
    public static Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        // 已经有可用连接就直接取
        if (channels.containsKey(key)) {
            Channel channel = channels.get(key);
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channels.remove(key);
            }
        }
        // 否则，重新连接获取 Channel
        Channel channel = nettyClient.doConnect(inetSocketAddress);
        channels.put(key, channel);
        return channel;
    }


}
