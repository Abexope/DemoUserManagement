package com.Abe.domain;

/**
 * 条件查询内容 Bean 封装类
 */
public class ConditionQueryBean {

    private String name;        // 姓名
    private String address;     // 地址
    private String email;       // 邮箱

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ConditionQueryBean{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    
}
