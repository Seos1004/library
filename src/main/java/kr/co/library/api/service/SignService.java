package kr.co.library.api.service;

import kr.co.library.api.common.dao.CommonDAO;
import kr.co.library.api.common.response.exception.custom.ServiceFail;
import kr.co.library.api.common.util.JWTUtils;
import kr.co.library.api.common.util.RedisUtils;
import kr.co.library.api.component.user.UserComponent;
import kr.co.library.api.constant.JWTConstant;
import kr.co.library.api.constant.response.YmlKey001SignConstant;
import kr.co.library.api.model.component.user.CreateUserModel;
import kr.co.library.api.model.sign.request.SignInRequestModel;
import kr.co.library.api.model.sign.request.SignOutRequestModel;
import kr.co.library.api.model.sign.request.SignUpRequestModel;
import kr.co.library.api.model.sign.response.SignInResponseModel;
import kr.co.library.api.model.util.jwt.JWTTokenCreateUserModel;
import kr.co.library.api.model.util.redis.CreateKeyModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SignService {

    @Autowired
    private CommonDAO commonDao;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserComponent userComponent;

    @Value("${jwt.refresh.validity-time}")
    private Integer REFRESH_TOKEN_VALIDITY_TIME;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public SignInResponseModel signIn(SignInRequestModel signInRequestModel) {
        log.info("[SignService.signIn] START {}" , signInRequestModel.getUserId());
        boolean userExist = false;
        try {
            userExist = userComponent.userExist(signInRequestModel.getUserId());
        }catch (Exception e){
            log.error("[SignService.signIn] 사용자 존재 확인 오류");
            throw new ServiceFail(YmlKey001SignConstant.SING_IN_USER_EXIST_FAIL , e);
        }
        if(!userExist){
            log.info("[SignService.signIn] 사용자 미존재");
            throw new ServiceFail(YmlKey001SignConstant.SING_IN_USER_NOT_EXIST);
        }
        SignInResponseModel signInResponseModel = new SignInResponseModel();
        String inputPassword = signInRequestModel.getPassword();
        String dbPassword = "";
        boolean passwordCheck = false;
        try {
            dbPassword = userComponent.getPassword(signInRequestModel.getUserId());
        }catch (Exception e){
            log.error("[SignService.signIn] 사용자 패스워드 조회 오류");
            throw new ServiceFail(YmlKey001SignConstant.SING_IN_GET_PASSWORD_FAIL , e);
        }
        try {
            passwordCheck = passwordEncoder.matches(inputPassword , dbPassword);
        }catch (Exception e){
            log.error("[SignService.signIn] 패스워드 검사중 오류");
            throw new ServiceFail(YmlKey001SignConstant.SING_IN_PASSWORD_CHECK_FAIL , e);
        }
        log.info("[SignService.signIn] passwordCheck ? : {}" , passwordCheck);
        if(passwordCheck) {
            try {
                JWTTokenCreateUserModel jwtTokenCreateUserModel = new JWTTokenCreateUserModel();
                jwtTokenCreateUserModel.setUserId(signInRequestModel.getUserId());
                String accessToken = jwtUtils.createAccessToken(jwtTokenCreateUserModel);
                String refreshToken = jwtUtils.createRefreshToken(jwtTokenCreateUserModel);

                List<CreateKeyModel> keys = new ArrayList<CreateKeyModel>();

                CreateKeyModel key = new CreateKeyModel();
                key.setKey(JWTConstant.ACCESS_TOKEN_KEY);
                key.setValue(accessToken);
                keys.add(key);

                key = new CreateKeyModel();
                key.setKey(JWTConstant.REFRESH_TOKEN_KEY);
                key.setValue(refreshToken);
                keys.add(key);

                redisUtils.createHashTypeKey(signInRequestModel.getUserId() , keys ,REFRESH_TOKEN_VALIDITY_TIME);
                signInResponseModel.setToken(accessToken);
            }catch (Exception e){
                log.error("[SignService.signIn] 토큰 생성중 오류");
                throw new ServiceFail(YmlKey001SignConstant.SING_IN_TOKEN_CREATE_FAIL , e);

            }
        }
        log.info("[SignService.signIn] END {}" , signInRequestModel.getUserId());
        return signInResponseModel;
    }

    public String signUp(SignUpRequestModel signUpRequestModel) {
        log.info("[SignService.signUp] START {}" , signUpRequestModel.getUserId());
        String responseKey = YmlKey001SignConstant.SIGN_UP_SUCCESS;
        boolean userExist = false;
        try {
            userExist = userComponent.userExist(signUpRequestModel.getUserId());
        }catch (Exception e){
            log.error("[SignService.signUp] 사용자 존재 확인 오류");
            throw new ServiceFail(YmlKey001SignConstant.SING_UP_USER_EXIST_FAIL , e);
        }
        if(userExist){
            log.info("[SignService.signUp] 사용자 존재");
            throw new ServiceFail(YmlKey001SignConstant.SING_UP_USER_EXIST);
        }
        try {
            CreateUserModel createUser = new CreateUserModel();
            createUser.setUserId(signUpRequestModel.getUserId());
            createUser.setUserName(signUpRequestModel.getUserName());
            createUser.setPassword(passwordEncoder.encode(signUpRequestModel.getPassword()));
            userComponent.createUser(createUser);
        }catch (Exception e){
            log.error("[SignService.signUp] 사용자 생성중 오류");
            throw new ServiceFail(YmlKey001SignConstant.SING_UP_CREATE_USER_FAIL , e);
        }
        log.info("[SignService.signUp] END");
        return responseKey;
    }

    public void signOut(SignOutRequestModel signOutRequestModel) {
        log.info("[SignService.signOut] START");
        try {
            redisUtils.removeHashTypeKey(signOutRequestModel.getUserId());
        }catch (Exception e){
            log.error("[SignService.signOut] 토큰정보 삭제중 오류 발생");
            throw new ServiceFail(YmlKey001SignConstant.SING_OUT_REMOVE_TOKEN_FAIL , e);
        }
        log.info("[SignService.signOut] END");
    }
}
