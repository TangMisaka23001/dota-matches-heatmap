package com.misaka.service.controller;


import com.google.common.collect.ImmutableMap;
import com.misaka.service.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class IndexController {

    @RequestMapping(value="/**", method= RequestMethod.GET)
    public String index(){
        return "index";
    }

    @GetMapping("getUserInfo")
    public Result<Map<String, String>> getUserInfo() {
        return Result.success(
                ImmutableMap.of(
                        "userId","123",
                        "username", "username"
                )
        );
    }
}
