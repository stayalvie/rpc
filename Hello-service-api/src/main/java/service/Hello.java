package service;

import lombok.*;

import java.io.Serializable;

/**
 * @author xiaofei
 * @create 2021-09-17 19:40
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Hello implements Serializable {

    private String message;
    private String description;

}
