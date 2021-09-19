package server;

import service.impl.HelloServiceImpl;
import xiaofei.transport.Socket.SocketRpcServer;
import xiaofei.register.DefaultServiceRegister;
import xiaofei.register.ServiceRegistry;
import xiaofei.transport.netty.NettyRpcRequestHandler;
import xiaofei.transport.netty.NettyRpcServer;

/**
 * @author xiaofei
 * @create 2021-09-17 19:51
 */
public class ExampleServer {

    public static void main(String[] args) {
        ServiceRegistry registry = new DefaultServiceRegister();
        //注册service, 目前不支持一个接口两个实现类
        registry.register(new HelloServiceImpl());
        NettyRpcServer rpcServer = new NettyRpcServer(9999, registry);
        rpcServer.start();
    }

}
