package com.dyw.shirospringboot.service.register;

import com.dyw.shirospringboot.DTO.register.RegisterDTO;
import com.dyw.shirospringboot.response.Result;

/**
 * @author Devil
 * @since 2022-06-30-23:59
 */
public interface RegisterService {

    Result register(RegisterDTO registerDTO);
}
