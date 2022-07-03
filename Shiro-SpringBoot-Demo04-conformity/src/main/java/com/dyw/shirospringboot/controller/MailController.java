package com.dyw.shirospringboot.controller;

import com.dyw.shirospringboot.response.R;
import com.dyw.shirospringboot.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Devil
 * @since 2022-07-02-19:09
 */
@RestController
@RequestMapping("/mail")
public class MailController {
    @GetMapping
    public Result sendMail() {
        return R.success("这是一个邮件");
    }
}
