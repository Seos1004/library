package kr.co.library.api.common.util;

import kr.co.library.api.constant.DateConstant;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active=local")
class DateUtilsTest {

    @Autowired
    private DateUtils dateUtils;

    @Test
    void timestampToDateTime() {
        String dateTime = dateUtils.timestampToDateTime(1702949510259L , DateConstant.BASE_DATE_PATTERN);
        System.out.println("dateTime ? : " + dateTime);
    }
}
