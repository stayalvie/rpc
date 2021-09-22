package xiaofei.transport.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.threadPoolMannger.ThreadPoolFactory;
import xiaofei.handler.RpcRequestHandler;
import xiaofei.register.ServiceRegistry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author xiaofei
 * @createTime 2021年09月17日 18:01:00
 */
public class SocketRpcServer {
    private static ExecutorService threadPool;
    private RpcRequestHandler rpcRequestHandler = new RpcRequestHandler();
    private static final Logger logger = LoggerFactory.getLogger(SocketRpcServer.class);
    private final ServiceRegistry serviceRegistry;

    static {
        threadPool = ThreadPoolFactory.createCustomThreadPoolIfAbsent("socket-server");
    }

    public SocketRpcServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }



    /**
     * 服务端开启服务
     */
    public void start(int port) {
        try (ServerSocket server = new ServerSocket(port);) {
            logger.info("server starts...");
            Socket socket;
            while ((socket = server.accept()) != null) {
                logger.info("client connected");
                threadPool.execute(new RpcMessageHandlerRunnable(socket, serviceRegistry, rpcRequestHandler));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("occur IOException:", e);
        }
    }

}
