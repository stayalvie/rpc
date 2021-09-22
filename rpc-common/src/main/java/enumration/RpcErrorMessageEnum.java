package enumration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author xiaofei
 * @create 2021-09-17 17:40
 */
@AllArgsConstructor
@Getter
@ToString
public enum RpcErrorMessageEnum {

    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVICE_CAN_NOT_BE_NULL("注册的服务不能为空"),
    SERVICE_CAN_NOT_BE_FOUND("对应的服务接口没有任何实现类"),
    REQUEST_NOT_MATCH_RESPONSE("返回结果错误！请求和返回的相应不匹配"),
    CLIENT_CONNECT_SERVER_FAILURE("端口占用中，客户端连接失败"),
    SERVER_CONNECT_FAIL("服务端连接失败");
    private final String message;
}
