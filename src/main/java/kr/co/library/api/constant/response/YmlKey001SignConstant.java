package kr.co.library.api.constant.response;

public final class YmlKey001SignConstant {

    //SUCCESS
    public static final String SIGN_UP_SUCCESS = "sign_up_success";
    public static final String SIGN_IN_SUCCESS = "sign_in_success";
    public static final String SIGN_OUT_SUCCESS = "sign_out_success";

    //EXCEPTION
    //로그인
    public static final String SING_IN_USER_EXIST_FAIL = "sing_in_user_exist_fail";
    public static final String SING_IN_USER_NOT_EXIST = "sing_in_user_not_exist";
    public static final String SING_IN_GET_PASSWORD_FAIL = "sing_in_get_password_fail";
    public static final String SING_IN_PASSWORD_CHECK_FAIL = "sing_in_password_check_fail";
    public static final String SING_IN_TOKEN_CREATE_FAIL = "sing_in_token_create_fail";
    public static final String SING_IN_PASSWORD_INCONSISTENCY = "sing_in_password_inconsistency";

    //가입(사용자 생성)
    public static final String SING_UP_USER_EXIST_FAIL = "sing_up_user_exist_fail";
    public static final String SING_UP_USER_EXIST = "sing_up_user_exist";
    public static final String SING_UP_CREATE_USER_FAIL = "sing_up_create_user_fail";

    //로그아웃
    public static final String SING_OUT_REMOVE_TOKEN_FAIL = "sing_out_remove_token_fail";

}




