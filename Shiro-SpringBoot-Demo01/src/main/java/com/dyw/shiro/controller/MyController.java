package com.dyw.shiro.controller;

import com.dyw.shiro.DTO.LoginDTO;
import com.dyw.shiro.service.LoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyController {

    @Autowired
    private LoginService loginService;

    //一般首页不止一个,所以就这样写
    @GetMapping({"/", "/index"})
    public String toIndex(Model model) {
        model.addAttribute("msg", "hello,Shiro");
        return "index";
    }

    @RequestMapping("/user/add")
    public String toAdd(){
        return "user/add";
    }
    @RequestMapping("/user/update")
    public String toUpdate(){
        return "user/update";
    }
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "nihao";
    }

    @PostMapping("login")
    public String login(LoginDTO loginDTO){
        if (loginService.login(loginDTO)){
            Subject subject = SecurityUtils.getSubject();
            boolean insert = subject.isPermitted("insert");
            System.out.println(insert);
            return "index";
        }
        return "nihao";
    }

    @GetMapping("logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "nihao";
    }

    @RequiresRoles("admin")
    @GetMapping("/say")
    public void sqy(){
        System.out.println();
    }
}