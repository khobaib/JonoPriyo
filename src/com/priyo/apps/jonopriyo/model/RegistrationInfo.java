package com.priyo.apps.jonopriyo.model;

public class RegistrationInfo {
    private String name;
    private String email;
    private String password;
    private String dob;
    private Long professionId;
    private Long educationId;
    private String sex;
    private Long countryId;
    private Long cityId;
    private Long areaId;
    private String address;
    private String phone;
    
    public RegistrationInfo() {
    }

    public RegistrationInfo(String name, String email, String password, String dob, Long professionId,
            Long educationId, String sex, Long countryId, Long cityId, Long areaId, String address, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.professionId = professionId;
        this.educationId = educationId;
        this.sex = sex;
        this.countryId = countryId;
        this.cityId = cityId;
        this.areaId = areaId;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Long getProfessionId() {
        return professionId;
    }

    public void setProfessionId(Long professionId) {
        this.professionId = professionId;
    }

    public Long getEducationId() {
        return educationId;
    }

    public void setEducationId(Long educationId) {
        this.educationId = educationId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    
    
    
}
