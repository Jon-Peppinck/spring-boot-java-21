package com.example.blog.security;

public enum RoleEnum {
    ADMIN(1),
    USER(2),
    SIGNED_OUT(3);

    private final int roleId;

    RoleEnum(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return name();
    }

    public static RoleEnum fromRoleId(int roleId) {
        for (RoleEnum role : RoleEnum.values()) {
            if (role.getRoleId() == roleId) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role ID: " + roleId);
    }
}
