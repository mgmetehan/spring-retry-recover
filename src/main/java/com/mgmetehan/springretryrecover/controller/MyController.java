package com.mgmetehan.springretryrecover.controller;

import com.mgmetehan.springretryrecover.service.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;

    @GetMapping("/callExternalService")
    public String callExternalService() {
        try {
            myService.callExternalService("");
            return "Success";
        } catch (Exception e) {
            return "Failed";
        }
    }

    @GetMapping("/callExternalService2")
    public String callExternalService2() {
        try {
            myService.callExternalService2("");
            return "Success";
        } catch (Exception e) {
            return "Failed";
        }
    }
}
