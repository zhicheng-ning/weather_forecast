package com.example.weatherforecast;

/***
 *@Authot: niko
 *@Date: Created in 15:52 2020/5/24
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 */
public class WeatherIndex {
    private String indexName;//指数名字
    private String indexValue;
    private String indexDetail;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(String indexValue) {
        this.indexValue = indexValue;
    }

    public String getIndexDetail() {
        return indexDetail;
    }

    public void setIndexDetail(String indexDetail) {
        this.indexDetail = indexDetail;
    }
}
