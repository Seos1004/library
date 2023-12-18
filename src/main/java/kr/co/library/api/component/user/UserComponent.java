package kr.co.library.api.component.user;

import kr.co.library.api.common.dao.CommonDAO;
import kr.co.library.api.common.response.exception.custom.ServiceFail;
import kr.co.library.api.common.util.JWTUtils;
import kr.co.library.api.common.util.RedisUtils;
import kr.co.library.api.constant.response.YmlKey002UserComponentConstant;
import kr.co.library.api.model.component.user.CreateUserModel;
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
            log.error("[UserComponent.userExist] 사용자 조회중 오류 발생" , e);
            throw new ServiceFail(YmlKey002UserComponentConstant.USER_EXIST_FAIL , e);
        }
        log.info("[UserComponent.userExist] END");
        return result;
    }

    public String getPassword(String userId) {
        log.info("[UserComponent.getPassword] START");
        String result = "";
        try {
            result = commonDao.select("userComponent.getPassword" , userId);
        }catch (Exception e){
            log.error("[UserComponent.getPassword] 비밀번호 조회중 오류 발생" , e);
            throw new ServiceFail(YmlKey002UserComponentConstant.GET_USER_PASSWORD_FAIL , e);
        }
        log.info("[UserComponent.getPassword] END");
        return result;
    }

    public boolean createUser(CreateUserModel createUserModel) {
        log.info("[UserComponent.createUser] START");
        boolean result = true;
        try {
            commonDao.insert("userComponent.createUser" , createUserModel);
        }catch (Exception e){
            log.error("[UserComponent.createUser] 유저 생성중 오류 발생" , e);
            throw new ServiceFail(YmlKey002UserComponentConstant.CREATE_USER_FAIL , e);
        }
        log.info("[UserComponent.createUser] END");
        return result;
    }

}
