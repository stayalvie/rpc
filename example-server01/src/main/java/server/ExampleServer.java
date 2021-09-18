package server;

import service.HelloService;
import service.impl.HelloServiceImpl;
import xiaofei.RpcServer;

/**
 * @author xiaofei
 * @create 2021-09-17 19:51
 */
public class ExampleServer {

    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(new HelloServiceImpl(), 9999);
    }

}
