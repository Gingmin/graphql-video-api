package com.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * React SPA 라우팅 지원.
 * - 운영: resources/static/index.html로 포워딩
 * - API(/graphql, /graphiql)는 제외
 * - 정적 파일(확장자 포함)은 제외
 */
@Controller
public class SpaForwardingController {

    // 파일 확장자(.)가 있는 요청은 정적 리소스로 간주하고 포워딩하지 않음
    @GetMapping({"/{path:^(?!graphql$|graphiql$)[^\\.]+$}", "/**/{path:^(?!graphql$|graphiql$)[^\\.]+$}"})
    public String forward() {
        return "forward:/index.html";
    }
}

