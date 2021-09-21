package utils;

import java.util.UUID;

/**
 * @author xiaofei
 * @create 2021-09-19 17:28
 */
public class UUIDGenerator {


    public static String UUID_Generator() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

}
