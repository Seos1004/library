package kr.co.library.api.common.util;

import kr.co.library.api.constant.JWTConstant;
import kr.co.library.api.model.util.jwt.JWTTokenCreateUserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active=local")
class JWTUtilsTest {

    @Autowired
    private JWTUtils jwtUtils;

    @Test
    void createAccessToken() {
        JWTTokenCreateUserModel param = new JWTTokenCreateUserModel();
        param.setUserId("save7412");
        param.setUserSeq(123L);
        param.setUserName("서승환zz");
        String token = jwtUtils.createAccessToken(param);
        System.out.println("token ? : " + token);
    }

    @Test
    void getClaimsInKey() {
        String token = "eyJyZWdEYXRlIjoxNzAyNTQxMTcwNzYzLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjpudWxsLCJleHAiOjE3MDI1NDE3NzAsInNlcSI6bnVsbH0.08xTVbVDHGoIr9OCaBklm10j_7lZA1hnXFNNQT-sKGw";
        String result = jwtUtils.getClaimsInKey(token , JWTConstant.GET_CLAIMS_IN_SEQ);
        System.out.println("seq ? : " + result);
        result = jwtUtils.getClaimsInKey(token , JWTConstant.GET_CLAIMS_IN_NAME);
        System.out.println("name ? : " + result);
        result = jwtUtils.getClaimsInKey(token , JWTConstant.GET_CLAIMS_IN_ID);
        System.out.println("id : " + result);
    }

    @Test
    void tokenValidation() {
        String token = "1eyJyZWdEYXRlIjoxNzAxODM5NTA1NDczLCJ0eXAiOiJBQ0NFU1NfVE9LRU4iLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi7ISc7Iq57ZmYenoiLCJleHAiOjE3MDE4Mzk1NjUsInNlcSI6MTIzfQ.Rn0-omUgtJkCNQ4zWKR376BffSMxTTZnYSo533mmICI";
        String result = jwtUtils.tokenValidation(token);
        System.out.println("Validation result ? : " +result);
    }


}
