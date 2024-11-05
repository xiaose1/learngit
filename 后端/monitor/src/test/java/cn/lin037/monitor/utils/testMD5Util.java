package cn.lin037.monitor.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class testMD5Util {

    @Test
    void testMD5() {

        String transformed = MD5Util.transformMD5("123456");
        System.out.println(transformed);

        boolean b = MD5Util.compareMD5(null, null);
        System.out.println(b);
    }
}
