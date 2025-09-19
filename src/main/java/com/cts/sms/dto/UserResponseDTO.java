
package com.cts.sms.dto;

import com.cts.sms.model.Role;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
