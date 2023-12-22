package kr.co.library.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.library.api.common.response.api.ApiResponseHandler;
import kr.co.library.api.model.response.ApiResponseModel;
import kr.co.library.api.model.sign.response.SignUpResponseModel;
import kr.co.library.api.model.token.request.TokenReissueRequestModel;
import kr.co.library.api.model.token.response.TokenReissueResponseModel;
import kr.co.library.api.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/token")
@Tag(name = "2. Token" , description = "토큰 컨트롤러")
public class TokenController {
    @Autowired
    private ApiResponseHandler apiResponseHandler;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/reissue")
    public ApiResponseModel<TokenReissueResponseModel> tokenReissue(@Valid @RequestBody TokenReissueRequestModel tokenReissueRequestModel){
        TokenReissueResponseModel result = tokenService.tokenReissue(tokenReissueRequestModel);
        return apiResponseHandler.apiResponse(result.getYmlKey() , result);
    }
}


