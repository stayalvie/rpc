package xiaofei.transport.netty;

import dto.RpcRequest;
import dto.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.threadPoolMannger.CustomThreadPoolConfig;
import utils.threadPoolMannger.ThreadPoolFactory;
import xiaofei.handler.RpcRequestHandler;
import xiaofei.register.ServiceRegistry;

import java.util.concurrent.ExecutorService;


/**
 * @author xiaofei
 * @create 2021-09-19 15:11
 */

@AllArgsConstructor
public class NettyRpcRequestHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(NettyRpcRequestHandler.class);

    private ServiceRegistry serviceRegistry;
    private final RpcRequestHandler requestHandler = new RpcRequestHandler();
    private static ExecutorService threadPool;

    static {
        CustomThreadPoolConfig customThreadPoolConfig = new CustomThreadPoolConfig();
        customThreadPoolConfig.setCorePoolSize(6);
        threadPool = ThreadPoolFactory.createCustomThreadPoolIfAbsent("netty-rpc-handler-response", customThreadPoolConfig);
    }

    /*
    *  处理响应不应阻塞主线程， 改为线程池执行
    * */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        threadPool.execute(() -> {
            logger.info(String.format("server handle message from client by thread: %s", Thread.currentThread().getName()));
            try {
                logger.info(String.format("server receive msg: %s", msg));
                RpcRequest rpcRequest = (RpcRequest) msg;
                Object result = requestHandler.handler(rpcRequest);

                logger.debug(String.format("server get result: %s", result.toString()));
                ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result, rpcRequest.getRequestId()));
                //本次请求完成当前连向客户端可断开  TODO： 这个端口应该是可以复用的不应直接关闭？
                future.addListener(ChannelFutureListener.CLOSE);
            } finally {
                //当前内核缓冲区的引用释放
                ReferenceCountUtil.release(msg);
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}
