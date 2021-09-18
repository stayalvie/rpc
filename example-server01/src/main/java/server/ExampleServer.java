package server;

import service.HelloService;
import service.impl.HelloServiceImpl;
import xiaofei.RpcServer;
import xiaofei.register.DefaultServiceRegister;
import xiaofei.register.ServiceRegistry;

/**
 * @author xiaofei
 * @create 2021-09-17 19:51
 */
public class ExampleServer {

    public static void main(String[] args) {
        ServiceRegistry registry = new DefaultServiceRegister();
        //注册service, 目前不支持一个接口两个实现类
        registry.register(new HelloServiceImpl());
        RpcServer rpcServer = new RpcServer(registry);
        rpcServer.start(9999);
    }

}
