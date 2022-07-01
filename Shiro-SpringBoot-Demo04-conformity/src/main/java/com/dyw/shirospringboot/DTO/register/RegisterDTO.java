package com.dyw.shirospringboot.DTO.register;

import lombok.Data;

/**
 * @author Devil
 * @since 2022-06-30-23:58
 */
@Data
public class RegisterDTO {
    private String username;

    private String password;

    private String mail;

    private String name;

    private Integer age;
}
