package dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author xiaofei
 * @create 2021-09-17 17:39
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;

}
