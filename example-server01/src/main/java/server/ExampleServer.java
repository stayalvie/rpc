package server;

import service.HelloService;
import service.impl.HelloServiceImpl;
import xiaofei.register.ServiceRegistry;
import xiaofei.register.ZkServiceRegistry;
import xiaofei.transport.netty.server.NettyRpcServer;

import java.net.InetSocketAddress;

/**
 * @author xiaofei
 * @create 2021-09-17 19:51
 */
public class ExampleServer {

    public static void main(String[] args) {

        NettyRpcServer rpcServer = new NettyRpcServer("127.0.0.1", 9999);
        rpcServer.publishService(new HelloServiceImpl(), HelloService.class);
        rpcServer.start();
    }

}
