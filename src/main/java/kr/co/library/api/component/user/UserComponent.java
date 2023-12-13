package kr.co.library.api.component.user;

import kr.co.library.api.common.dao.CommonDAO;
import kr.co.library.api.common.response.exception.custom.ServiceFail;
import kr.co.library.api.common.util.JWTUtils;
import kr.co.library.api.common.util.RedisUtils;
import kr.co.library.api.model.sign.request.SignInRequestModel;
import kr.co.library.api.model.sign.request.SignOutRequestModel;
import kr.co.library.api.model.sign.request.SignUpRequestModel;
import kr.co.library.api.model.sign.response.SignInResponseModel;
import kr.co.library.api.model.util.jwt.JWTTokenCreateUserModel;
import kr.co.library.api.model.util.redis.CreateKeyModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class UserComponent {

    @Autowired
    private CommonDAO commonDao;

    public boolean userExist(String userId) {
        log.info("[UserComponent.userExist] START");
        boolean result = true;
        try {
            result = commonDao.selectCount("userComponent.userExist" , userId) > 0 ? true : false;
        }catch (Exception e){
            log.error("[UserComponent.userExist] 사용자 조회중 오류 발생");
            throw new ServiceFail("not_specified_message" , e);
        }
        log.info("[UserComponent.userExist] END");
        return result;
    }
}
