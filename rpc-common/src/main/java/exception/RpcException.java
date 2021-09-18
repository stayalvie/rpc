package exception;

import enumration.RpcErrorMessageEnum;

/**
 * @author xiaofei
 * @create 2021-09-17 17:48
 *
 * 异常类， 用来判断方法是否执行有错误
 */

public class RpcException extends RuntimeException{

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }

}
