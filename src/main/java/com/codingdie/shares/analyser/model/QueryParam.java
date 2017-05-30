package com.codingdie.shares.analyser.model;

import java.util.List;

/**
 * Created by xupeng on 2017/5/30.
 */
public class QueryParam {
    private  String beginDate;
    private  String endDate;
    private List<String> shareCodes;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<String> getShareCodes() {
        return shareCodes;
    }

    public void setShareCodes(List<String> shareCodes) {
        this.shareCodes = shareCodes;
    }
}
