package kr.co.library.api.constant;

public final class JWTConstant {
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String REQUEST_TOKEN_HEADER_KEY = "Authorization";

    public static final String CREATE_CLAIMS_USER_SEQ_KEY = "seq";
    public static final String CREATE_CLAIMS_USER_NAME_KEY = "name";
    public static final String CREATE_CLAIMS_ID_NAME_KEY = "id";

    public static final String CREATE_HEADER_TOKEN_TYPE_KEY = "typ";
    public static final String CREATE_HEADER_TOKEN_TYPE_VALUE = "JWT";

    public static final String CREATE_HEADER_ALGORITHM_KEY = "alg";
    public static final String CREATE_HEADER_ALGORITHM_VALUE = "HS256";
    public static final String CREATE_HEADER_REG_DATE_KEY = "regDate";

    public static final String GET_CLAIMS_IN_NAME = "name";
    public static final String GET_CLAIMS_IN_SEQ = "seq";
    public static final String GET_CLAIMS_IN_ID = "id";


    public static final String TOKEN_VALIDATION_SUCCESS = "SUCCESS";
    public static final String TOKEN_VALIDATION_EXPIRATION = "EXPIRATION";
    public static final String TOKEN_VALIDATION_FAKE = "FAKE";
    public static final String TOKEN_VALIDATION_FAIL = "FAIL";

    public static final String ACCESS_TOKEN_KEY = "accessToken";
    public static final String REFRESH_TOKEN_KEY = "refreshToken";

}




