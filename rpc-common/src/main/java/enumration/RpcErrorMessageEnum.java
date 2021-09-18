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
    SERVICE_CAN_NOT_BE_FOUND("对应的服务接口没有任何实现类");

    private final String message;
}
