package kr.co.library.api.common.util;

import io.jsonwebtoken.*;
import jakarta.xml.bind.DatatypeConverter;
import kr.co.library.api.constant.JWTConstant;
import kr.co.library.api.model.util.jwt.JWTTokenCreateUserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWTUtils {
    @Autowired
    private DateUtils dateUtils;
    @Value("${jwt.secret_key}")
    private String JWT_ACCESS_TOKEN_SECRET;

    @Value("${jwt.access.validity-time}")
    private Integer ACCESS_TOKEN_VALIDITY_TIME;

    @Value("${jwt.refresh.validity-time}")
    private Integer REFRESH_TOKEN_VALIDITY_TIME;

    public String createAccessToken(JWTTokenCreateUserModel user) {
        return createJWTToken(user, ACCESS_TOKEN_VALIDITY_TIME);
    }

    public String createRefreshToken(JWTTokenCreateUserModel user) {
        return createJWTToken(user, REFRESH_TOKEN_VALIDITY_TIME);
    }

    public String getClaimsInKey(String token , String key){
        String result = "";
        switch (key){
            case JWTConstant.GET_CLAIMS_IN_NAME:
            case JWTConstant.GET_CLAIMS_IN_SEQ:
                result = parseClaims(token).get(key).toString();
                break;
        }
        return result;
    }

    public String tokenValidation(String token) {
        log.info("[JWTUtils.tokenValidation] START");
        String ValidationResult = "";
        String userName = "";
        try {
            userName = getClaimsInKey(token, JWTConstant.GET_CLAIMS_IN_NAME);;
            Jwts.parser().setSigningKey(createSigningKey(JWT_ACCESS_TOKEN_SECRET)).parseClaimsJws(token);
            ValidationResult = JWTConstant.TOKEN_VALIDATION_SUCCESS;
        } catch (ExpiredJwtException e) {
            log.info("[JWTUtils.tokenValidation] {} 만료된 토큰입니다." , userName);
            ValidationResult =  JWTConstant.TOKEN_VALIDATION_EXPIRATION;
        } catch (JwtException exception) {
            log.info("[JWTUtils.tokenValidation] {} 위/변조 토큰입니다." , userName);
            ValidationResult =  JWTConstant.TOKEN_VALIDATION_FAKE;
        }  catch (Exception e) {
            log.info("[JWTUtils.tokenValidation] {} 잘못된 토큰입니다." , userName);
            ValidationResult =  JWTConstant.TOKEN_VALIDATION_FAIL;
        }
        log.info("[JWTUtils.tokenValidation] userName : {} Result : {}" , userName , ValidationResult);
        log.info("[JWTUtils.tokenValidation] END");
        return ValidationResult;
    }

    private String createJWTToken(JWTTokenCreateUserModel user , Integer validityTime){
        log.info("[JWTUtils.createJWTToken] START");
        Date now = new Date();
        String token = Jwts.builder()
                .setSubject(user.getUserId())
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .setExpiration(new Date(now.getTime() + validityTime))
                .signWith(SignatureAlgorithm.HS256, createSigningKey(JWT_ACCESS_TOKEN_SECRET)).compact();
        log.info("[JWTUtils.createToken] END");
        return token;
    }

    private Map<String, Object> createHeader() {
        log.info("[JWTUtils.createHeader] START");
        Map<String, Object> header = new HashMap<>();
        header.put(JWTConstant.CREATE_HEADER_TOKEN_TYPE_KEY, JWTConstant.CREATE_HEADER_TOKEN_TYPE_VALUE);
        header.put(JWTConstant.CREATE_HEADER_ALGORITHM_KEY, JWTConstant.CREATE_HEADER_ALGORITHM_VALUE);
        header.put(JWTConstant.CREATE_HEADER_REG_DATE_KEY, dateUtils.getInstantNowToDate());
        log.info("[JWTUtils.createHeader] END");
        return header;
    }

    private Map<String, Object> createClaims(JWTTokenCreateUserModel user) {
        log.info("[JWTUtils.createClaims] START {}" , user.getUserName());
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWTConstant.CREATE_CLAIMS_USER_SEQ_KEY, user.getUserSeq());
        claims.put(JWTConstant.CREATE_CLAIMS_USER_NAME_KEY, user.getUserName());
        log.info("[JWTUtils.createClaims] END" , user.getUserName());
        return claims;
    }
    private Key createSigningKey(String key) {
        log.info("[JWTUtils.createSigningKey] START");
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
        Key result = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
        log.info("[JWTUtils.createSigningKey] END");
        return result;
    }

    private Claims parseClaims(String token) {
        log.info("[JWTUtils.parseClaims] START");
        Claims result = null;
        try {
            result = Jwts.parser()
                    .setSigningKey(createSigningKey(JWT_ACCESS_TOKEN_SECRET))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("[JWTUtils.parseClaims] 만료된 토큰");
            // 만료된 토큰이 더라도 일단 파싱을 함
            result = e.getClaims();
        }catch (Exception e){
            log.error("[JWTUtils.parseClaims] 토큰 파서 오류 {}" , e);
            return null;
        }
        log.info("[JWTUtils.parseClaims] END");
        return result;

    }
}
