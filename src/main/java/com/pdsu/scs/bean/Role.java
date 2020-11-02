package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * @author 半梦
 */
public class Role implements Serializable {

    public static final Integer ROLE_USER = 1;

    public static final Integer ROLE_TEACHER = 2;

    public static final Integer ROLE_ADMIN = 3;

    public static final String SYSTEM_USER = "学生";

    public static final String SYSTEM_TEACHER = "教师";

    public static final String SYSTEM_ADMIN = "管理员";

    public Role getUser() {
        return new Role(ROLE_USER, SYSTEM_USER);
    }
    public Role getTeacher() {
        return new Role(ROLE_TEACHER, SYSTEM_TEACHER);
    }

    public Role getAdmin() {
        return new Role(ROLE_ADMIN, SYSTEM_ADMIN);
    }

    private Integer id;

    private String role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }

    public Role(String role) {
        this.role = role;
    }

    public Role() {
    }

    public Role(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                '}';
    }
}