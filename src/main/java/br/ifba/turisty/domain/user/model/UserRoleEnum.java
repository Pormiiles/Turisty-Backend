package br.ifba.turisty.domain.user.model;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    ADMIN("admin"),
    USER("user");

    private final String role;

    UserRoleEnum(String role){
        this.role = role;
    }

}
