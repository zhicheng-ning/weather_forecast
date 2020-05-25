package com.example.weatherforecast;

/***
 *@Authot: niko
 *@Date: Created in 21:43 2020/5/24
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 * 表示一个县级对象
 */
public class CountryCode {
    private String province;//所属省份
    private String city;//市
    private String country;//县
    private String code;//县级代码

    /*public CountryCode(String province, String city, String country, String code) {
        this.province = province;
        this.city = city;
        this.country = country;
        this.code = code;
    }*/

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
