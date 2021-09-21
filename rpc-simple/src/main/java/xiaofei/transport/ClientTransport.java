package xiaofei.transport;

import dto.RpcRequest;

/**
 * @author xiaofei
 * @create 2021-09-18 17:30
 */
public interface ClientTransport {

    public Object sendRpcRequest(RpcRequest rpcRequest);

}
