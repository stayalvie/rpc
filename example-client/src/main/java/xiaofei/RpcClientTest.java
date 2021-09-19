package xiaofei;

import service.Hello;
import service.HelloService;
import xiaofei.transport.RpcClientProxy;
import xiaofei.transport.netty.NettyRpcClient;

/**
 * @author xiaofei
 * @create 2021-09-17 19:45
 */
public class RpcClientTest {

    public static void main(String[] args) {

        RpcClientProxy rpcClientProxy = new RpcClientProxy(new NettyRpcClient("127.0.0.1", 9999));
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("111", "222"));
        System.out.println(hello);
    }
}
