package com.codingdie.shares.analyser.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupeng on 2017/5/30.
 */
public class ShareData {
    public   String name;
    public   String stockCode;
    public List<OneDayData> dayDatas=new ArrayList<>();
}
