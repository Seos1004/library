package kr.co.library.api.service;

import kr.co.library.api.common.dao.CommonDAO;
import kr.co.library.api.common.response.exception.custom.ServiceFail;
import kr.co.library.api.common.util.JWTUtils;
import kr.co.library.api.common.util.RedisUtils;
import kr.co.library.api.component.user.UserComponent;
import kr.co.library.api.constant.JWTConstant;
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
        log.info("[SampleDetailService.signIn] START");
        boolean userExist = false;
        try {
            userExist = userComponent.userExist(signInRequestModel.getUserId());
        }catch (Exception e){
            log.error("사용자 존재 확인 오류");
            throw new ServiceFail("not_specified_message" , e);
        }
        if(!userExist){
            log.info("사용자 미존재");
            throw new ServiceFail("not_specified_message");
        }
        SignInResponseModel signInResponseModel = new SignInResponseModel();
        String inputPassword = signInRequestModel.getPassword();
        String dbPassword = "";
        boolean passwordCheck = false;
        try {
            dbPassword = commonDao.select("userComponent.getPassword" , signInRequestModel.getUserId());
        }catch (Exception e){
            log.error("사용자 패스워드 조회 오류");
            throw new ServiceFail("" , e);
        }
        try {
            passwordCheck = passwordEncoder.matches(inputPassword , dbPassword);
        }catch (Exception e){
            log.error("패스워드 검사중 오류 {}");
            throw new ServiceFail("" , e);
        }
        log.info("passwordCheck ? : {}" , passwordCheck);
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
                //아래 리프레시 토큰 만료시간으로 지정해야함
                redisUtils.createHashTypeKey(signInRequestModel.getUserId() , keys ,REFRESH_TOKEN_VALIDITY_TIME);
                signInResponseModel.setToken(accessToken);
            }catch (Exception e){
                log.error("토큰 생성중 오류");
            }
        }
        log.info("[SampleDetailService.signIn] END");
        return signInResponseModel;
    }

    public String signUp(SignUpRequestModel signUpRequestModel) {
        log.info("[SampleDetailService.signUp] START");
        String responseKey = "created";
        boolean userExist = false;
        try {
            userExist = userComponent.userExist(signUpRequestModel.getUserId());
        }catch (Exception e){
            log.error("사용자 존재 확인 오류");
            throw new ServiceFail("" , e);
        }
        if(userExist){
            log.info("사용자 존재");
            throw new ServiceFail("");
        }
        try {
            signUpRequestModel.setPassword(passwordEncoder.encode(signUpRequestModel.getPassword()));
            log.info("signUpRequestModel.getPassword() ? : {}" , signUpRequestModel.getPassword());
            //TODO 이거  UserCOmponent로 뺴고 model객체 만들어서 해야함
            commonDao.insert("userComponent.createUser" , signUpRequestModel);
        }catch (Exception e){
            log.error("사용자 생성중 오류");
            throw new ServiceFail("" , e);
        }
        log.info("[SampleDetailService.signUp] END");
        return responseKey;
    }

    public void signOut(SignOutRequestModel signOutRequestModel) {
        log.info("[SampleDetailService.getSampleDetails] START");
        try {
            redisUtils.removeHashTypeKey(signOutRequestModel.getUserId());
        }catch (Exception e){
            log.error("토큰정보 삭제중 오류 발생");
            throw new ServiceFail("not_specified_message" , e);
        }
        log.info("[SampleDetailService.getSampleDetails] END");
    }
}
