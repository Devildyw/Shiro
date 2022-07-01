package com.dyw.shirospringboot.service.register.impl;

import com.dyw.shirospringboot.DTO.register.RegisterDTO;
import com.dyw.shirospringboot.entity.User;
import com.dyw.shirospringboot.mapper.UserMapper;
import com.dyw.shirospringboot.response.R;
import com.dyw.shirospringboot.response.Result;
import com.dyw.shirospringboot.service.register.RegisterService;
import com.dyw.shirospringboot.utils.DigestsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Devil
 * @since 2022-07-01-0:00
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    @Resource
    private UserMapper userMapper;
    @Override
    public Result register(RegisterDTO registerDTO) {
        try {
            String username = registerDTO.getUsername();
            String password = registerDTO.getPassword();
            String name = registerDTO.getName();
            String mail = registerDTO.getMail();
            Integer age = registerDTO.getAge();

            //检验请求参数
            if (StringUtils.isEmpty(username)) {
                return R.fail(1001, "用户名不能为空");
            }

            //判断用户是否已经注册
            if (userMapper.findUserByUserName(username) != null) {
                return R.fail(1002, "该用户名已被注册");
            }

            if (StringUtils.isEmpty(password)) {
                return R.fail(1001, "邮箱不能为空");
            }

            if (StringUtils.isEmpty(name)) {
                return R.fail(1001, "姓名不能为空");
            }

            if (age > 0 && StringUtils.isEmpty(Integer.toString(age))) {
                return R.fail(1001, "年龄不能为负数或年龄行为空");
            }

            //密码进行密文加密
            Map<String, String> map = DigestsUtil.encryptPassword(password);

            User user = new User(username, map.get("password"), name, mail, age, map.get("salt"));

            userMapper.insert(user);

            return R.success(null);
        } catch (Exception e) {
            throw new RuntimeException("未知错误,请联系管理员");
        }
    }
}
