package com.store.store.common.entity;

import com.store.store.common.enums.UserType;
import com.store.store.common.entity.auditable.BaseRepoEntityAuditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseRepoEntityAuditable<String, Long> {

    private String email;

    private String name;

    private LocalDateTime registeredDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;
}
