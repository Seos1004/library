package kr.co.library.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sample")
@Tag(name = "0. Sample" , description = "샘플 컨트롤러")
public class SampleController {

   @GetMapping
    public String sampleGet(){
        return "dkkkk";
    }

}


