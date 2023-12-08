package kr.co.library.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.library.api.common.response.api.ApiResponseHandler;
import kr.co.library.api.model.response.ApiResponseModel;
import kr.co.library.api.model.sign.request.SignInRequestModel;
import kr.co.library.api.model.sign.request.SignOutRequestModel;
import kr.co.library.api.model.sign.request.SignUpRequestModel;
import kr.co.library.api.model.sign.response.SignInResponseModel;
import kr.co.library.api.model.sign.response.SignUpResponseModel;
import kr.co.library.api.service.SignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "1. Sign" , description = "로그인/로그아웃 컨트롤러")
public class SignController {

    @Autowired
    private ApiResponseHandler apiResponseHandler;

    @Autowired
    private SignService signService;

    @PostMapping("/sign-up")
    public ApiResponseModel<SignUpResponseModel> signUp(@Valid @RequestBody SignUpRequestModel signUpRequestModel){
        String responseKey = signService.signUp(signUpRequestModel);
        return apiResponseHandler.apiResponse(responseKey);
    }

    @PostMapping("/sign-in")
    public ApiResponseModel<SignInResponseModel> signIn(@Valid @RequestBody SignInRequestModel signInRequestModel){
        SignInResponseModel signInResponseModel = new SignInResponseModel();
        signInResponseModel = signService.signIn(signInRequestModel);
        return apiResponseHandler.apiResponse("ok" , signInResponseModel);
    }

    @PostMapping("/sign-out")
    public ApiResponseModel<Void> signOut(@Valid @RequestBody SignOutRequestModel signOutRequestModel){
        signService.signOut(signOutRequestModel);
        return apiResponseHandler.apiResponse("ok");
    }

}


