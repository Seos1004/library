package kr.co.library.api.service;

import kr.co.library.api.common.response.exception.custom.ServiceFail;
import kr.co.library.api.common.util.JWTUtils;
import kr.co.library.api.common.util.RedisUtils;
import kr.co.library.api.component.user.UserComponent;
import kr.co.library.api.constant.JWTConstant;
import kr.co.library.api.constant.response.YmlKey000CommonConstant;
import kr.co.library.api.constant.response.YmlKey001SignConstant;
import kr.co.library.api.constant.response.YmlKey003TokenConstant;
import kr.co.library.api.model.token.request.TokenReissueRequestModel;
import kr.co.library.api.model.token.response.TokenReissueResponseModel;
import kr.co.library.api.model.util.jwt.JWTTokenCreateUserModel;
import kr.co.library.api.model.util.jwt.JWTTokenModel;
import kr.co.library.api.model.util.redis.CreateKeyModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TokenService {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserComponent userComponent;

    @Value("${jwt.refresh.validity-time}")
    private Integer REFRESH_TOKEN_VALIDITY_TIME;

    public TokenReissueResponseModel tokenReissue(TokenReissueRequestModel tokenReissueRequestModel) {
        log.info("[TokenService.tokenReissue] START");
        String userId = tokenReissueRequestModel.getUserId();
        String tokenUserId = "";
        String token = "";
        String tokenParsingResult = "";
        String accessToken = "";
        String refreshToken = "";

        TokenReissueResponseModel result = new TokenReissueResponseModel();
        result.setYmlKey(YmlKey003TokenConstant.TOKEN_REISSUE_SUCCESS);
        boolean userExist = false;
        try {
            userExist = userComponent.userExist(userId);
        }catch (Exception e){
            log.error("[TokenService.tokenReissue] 사용자 존재 확인 오류");
            throw new ServiceFail(YmlKey003TokenConstant.TOKEN_REISSUE_USER_EXIST_FAIL, e);
        }
        if(!userExist){
            log.info("[TokenService.tokenReissue] 사용자 미존재");
            throw new ServiceFail(YmlKey003TokenConstant.TOKEN_REISSUE_USER_NOT_EXIST);
        }
        try {
            token = jwtUtils.tokenPrefixParser(tokenReissueRequestModel.getRefreshToken());
        }catch (Exception e){
            log.error("[TokenService.tokenReissue] HEADER 파싱 실패");
            throw new ServiceFail(YmlKey003TokenConstant.TOKEN_REISSUE_PARSING_FAIL , e);
        }
        tokenParsingResult = jwtUtils.tokenValidation(token);
        switch (tokenParsingResult){
            case JWTConstant.TOKEN_VALIDATION_SUCCESS:
                tokenUserId = jwtUtils.getClaimsInKey(token, JWTConstant.GET_CLAIMS_IN_ID);
                if(userId.equals(tokenUserId)){
                    JWTTokenModel redisToken = redisUtils.getHashType(userId);
                    if(null == redisToken){
                        log.info("[TokenService.tokenReissue] redis에 key가 존재하지 않습니다.");
                        throw new ServiceFail(YmlKey003TokenConstant.TOKEN_REISSUE_REDIS_KEY_NOT_EXIST);
                    }
                    if(null == redisToken.getAccessToken()){
                        log.info("[TokenService.tokenReissue] key에 access 토큰이 존재하지 않습니다.");
                        throw new ServiceFail(YmlKey003TokenConstant.TOKEN_REISSUE_REDIS_ACCESS_TOKEN_NOT_EXIST);
                    }
                    if(null == redisToken.getRefreshToken()){
                        log.info("[TokenService.tokenReissue] key에 refresh 토큰이 존재하지 않습니다.");
                        throw new ServiceFail(YmlKey003TokenConstant.TOKEN_REISSUE_REDIS_REFRESH_TOKEN_NOT_EXIST);
                    }
                    if(!token.equals(redisToken.getRefreshToken())){
                        log.info("[TokenService.tokenReissue] 토큰이 일치하지 않습니다.");
                        throw new ServiceFail(YmlKey003TokenConstant.TOKEN_REISSUE_REDIS_ACCESS_TOKEN_NOT_EQUAL);
                    }
                    try {
                        JWTTokenCreateUserModel jwtTokenCreateUserModel = new JWTTokenCreateUserModel();
                        jwtTokenCreateUserModel.setUserId(userId);
                        accessToken = jwtUtils.createAccessToken(jwtTokenCreateUserModel);
                        refreshToken = jwtUtils.createRefreshToken(jwtTokenCreateUserModel);

                        List<CreateKeyModel> keys = List.of(
                                new CreateKeyModel(JWTConstant.ACCESS_TOKEN_KEY, accessToken),
                                new CreateKeyModel(JWTConstant.REFRESH_TOKEN_KEY, refreshToken)
                        );

                        redisUtils.createHashTypeKey(userId , keys ,REFRESH_TOKEN_VALIDITY_TIME);
                        result.setAccessToken(accessToken);
                        result.setRefreshToken(refreshToken);
                    }catch (Exception e){
                        log.error("[TokenService.tokenReissue] 토큰 생성중 오류");
                        throw new ServiceFail(YmlKey003TokenConstant.TOKEN_REISSUE_TOKEN_CREATE_FAIL, e);

                    }
                }else{
                    log.info("[TokenService.tokenReissue] 존재하지 않는 사용자입니다. => userId : {} | tokenUserId : {}" , userId , tokenUserId);
                    throw new ServiceFail(YmlKey003TokenConstant.TOKEN_REISSUE_USER_NOT_EQUALS);
                }
                break;
            case JWTConstant.TOKEN_VALIDATION_EXPIRATION:
                log.error("[TokenService.tokenReissue] 만료된 토큰입니다.");
                throw new ServiceFail(YmlKey003TokenConstant.TOKEN_REISSUE_TOKEN_VALIDATION_EXPIRATION);
            case JWTConstant.TOKEN_VALIDATION_FAKE:
                log.error("[TokenService.tokenReissue] 위/변조된 토큰입니다.");
                throw new ServiceFail(YmlKey003TokenConstant.TOKEN_REISSUE_TOKEN_VALIDATION_FAKE);
            case JWTConstant.TOKEN_VALIDATION_FAIL:
                log.error("[TokenService.tokenReissue] 토큰 파싱에 실패하였습니다.");
                throw new ServiceFail(YmlKey003TokenConstant.TOKEN_REISSUE_TOKEN_VALIDATION_PARSING_FAIL);
            default:
                log.error("[TokenService.tokenReissue] 규격 외 결과");
                throw new ServiceFail(YmlKey003TokenConstant.TOKEN_REISSUE_TOKEN_VALIDATION_OUT_OF_SPECIFICATION);
        }
        log.info("[TokenService.tokenReissue] END");
        return result;
    }
}
