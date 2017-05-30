package com.codingdie.shares.analyser.model;

import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Created by xupeng on 2017/5/29.
 */
public class OneDayData {
    public String date;
    public  double openingPrice;
    public  double highestPrice;
    public  double minimumPrice;
    public  double closingPrice;
    public  int volume;
    public  double turnover;
    public  double increase;
    public  double increasePrice;

    @Override
    public OneDayData clone()  {
        return new Gson().fromJson(new Gson().toJson(this),OneDayData.class);
    }
}
