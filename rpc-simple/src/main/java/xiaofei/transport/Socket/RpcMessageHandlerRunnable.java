package xiaofei.transport.Socket;

import dto.RpcRequest;
import dto.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xiaofei.handler.RpcRequestHandler;
import xiaofei.register.ServiceRegistry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author xiaofei
 * @createTime 2020年05月10日 09:18:00
 */
public class RpcMessageHandlerRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RpcMessageHandlerRunnable.class);
    private Socket socket;
    private ServiceRegistry serviceRegistry;
    private RpcRequestHandler requestHandler;

    public RpcMessageHandlerRunnable(Socket socket, ServiceRegistry serviceRegistry, RpcRequestHandler requestHandler) {
        this.socket = socket;
        this.serviceRegistry = serviceRegistry;
        this.requestHandler = requestHandler;
    }

    @Override
    public void run() {
        // 注意使用 try-with-resources ,因为这样更加优雅
        // 并且,try-with-resources 语句在编写必须关闭资源的代码时会更容易，也不会出错
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object result = requestHandler.handler(rpcRequest);
            objectOutputStream.writeObject(RpcResponse.success(result, rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("occur exception:", e);
        }
    }


}
