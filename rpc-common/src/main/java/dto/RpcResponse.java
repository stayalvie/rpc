package dto;

import enumration.RpcResponseCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaofei
 * @create 2021-09-17 17:39
 */
@Data
public class RpcResponse<T> implements Serializable {

    private static final long serialVersionUID = 715745410605631233L;
    /**
     * 响应码
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    public static <T> RpcResponse<T> success(T data) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(RpcResponseCode.SUCCESS.getCode());
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    public static <T> RpcResponse<T> fail (RpcResponseCode rpcResponseCode) {
      RpcResponse<T>  response = new RpcResponse<>();
      response.setMessage(rpcResponseCode.getMessage());
      response.setCode(rpcResponseCode.getCode());
      return response;
    }
}
