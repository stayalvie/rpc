package xiaofei;

import service.Hello;
import service.HelloService;
import xiaofei.transport.ClientTransport;
import xiaofei.transport.RpcClientProxy;
import xiaofei.transport.netty.client.NettyRpcClient;
import xiaofei.transport.netty.client.RpcClientManager;

import java.net.InetSocketAddress;

/**
 * @author xiaofei
 * @create 2021-09-17 19:45
 */
public class RpcClientTest {

    public static void main(String[] args) {
        ClientTransport rpcClient = new RpcClientManager();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("111", "222"));
        System.out.println(hello);
        hello = helloService.hello(new Hello("111", "222"));
        System.out.println(hello);
        System.out.println(helloService.hello(new Hello("111", "222")));
        System.out.println(hello);
    }
}
