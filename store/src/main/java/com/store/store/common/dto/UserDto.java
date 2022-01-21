package com.store.store.common.dto;

import com.store.store.common.enums.UserType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private Long id;

    private String firstName;

    private UserType userType;

    private LocalDateTime registeredDate;
}
