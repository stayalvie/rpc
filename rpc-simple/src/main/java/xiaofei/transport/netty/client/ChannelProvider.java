package xiaofei.transport.netty.client;

import enumration.RpcErrorMessageEnum;
import exception.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaofei
 * @create 2021-09-19 18:49
 */
public class ChannelProvider {

    private static final Logger logger = LoggerFactory.getLogger(ChannelProvider.class);

    private static Bootstrap bootstrap = NettyRpcClient.getBootStrap();
    private static Channel channel = null;
    /**
     * 最多重试次数
     */
    private static final int MAX_RETRY_COUNT = 5;

    /*
    *  TODO 当前客户端的一个端口只能连接一个端口， 如何复用这个端口让  让这个连接 3秒内没发数据断开， 通过requestId来标识结果
    *
    * */
    public static Channel get(InetSocketAddress inetSocketAddress) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            connect(bootstrap, inetSocketAddress, countDownLatch);
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("occur exception when get  channel:", e);
        }
        return channel;
    }


    private static void connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress, CountDownLatch countDownLatch) {
        connect(bootstrap, inetSocketAddress, MAX_RETRY_COUNT, countDownLatch);
    }

    /**
     * 带有重试机制的客户端连接方法
     * 目前客户端本端口只能发一次服务， 如果在利用这个端口发数据得等待
     */
    private static void connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress, int retry, CountDownLatch countDownLatch) {
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            logger.info("连接上: {}", inetSocketAddress);
            if (future.isSuccess()) {
                logger.info("客户端连接成功!");
                channel = future.channel();
                logger.info("本次：{}", channel);
                countDownLatch.countDown();
                return;
            }
            if (retry == 0) {
                logger.error("客户端连接失败:重试次数已用完，放弃连接！");
                countDownLatch.countDown();
                throw new RpcException(RpcErrorMessageEnum.CLIENT_CONNECT_SERVER_FAILURE);
            }
            // 第几次重连
            int order = (MAX_RETRY_COUNT - retry) + 1;
            // 本次重连的间隔
            int delay = 1 << order;
            logger.error("{}: 连接失败，第 {} 次重连……", new Date(), order);
            bootstrap.config().group().schedule(() -> connect(bootstrap, inetSocketAddress, retry - 1, countDownLatch), delay, TimeUnit
                    .SECONDS);
        });
    }


}
