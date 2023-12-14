package kr.co.library.api.common.util;

import kr.co.library.api.constant.JWTConstant;
import kr.co.library.api.model.util.jwt.JWTTokenCreateUserModel;
import kr.co.library.api.model.util.redis.CreateKeyModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(properties = "spring.profiles.active=local")
class RedisUtilsTest {

    @Autowired
    private RedisUtils redisUtils;

    @Test
    void createHashTypeKey() {
        List<CreateKeyModel> createKeys = new ArrayList<CreateKeyModel>();
        for(int i = 1; i < 5; i++){
            CreateKeyModel param = new CreateKeyModel();
            param.setKey("key" + i);
            param.setValue("val" + (i+1));
            createKeys.add(param);
        }
        CreateKeyModel param = new CreateKeyModel();
        param.setKey("key4");
        param.setValue("tnwjd");
        createKeys.add(param);
        redisUtils.createHashTypeKey("sshTest" , createKeys , 1200000);
    }
    @Test
    void getHashType() {
        redisUtils.getHashType("sshTest");
    }
    @Test
    void getHashTypeDetail() {
        redisUtils.getHashTypeDetail("sshTest" , "key1");
        redisUtils.getHashTypeDetail("sshTest" , "key22");
        redisUtils.getHashTypeDetail("sshTest" , "key3");
    }


    @Test
    void removeHashTypeKey() {
        redisUtils.removeHashTypeKey("sshTest");
    }

    @Test
    void removeHashTypeRow() {
        redisUtils.removeHashTypeRow("sshTest" , "key4");
    }
}
