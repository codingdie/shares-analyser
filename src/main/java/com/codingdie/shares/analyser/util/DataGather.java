package com.codingdie.shares.analyser.util;

import com.codingdie.shares.analyser.model.OneDayData;
import com.codingdie.shares.analyser.model.QueryParam;
import com.codingdie.shares.analyser.model.ShareData;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xupeng on 2017/5/30.
 */
public class DataGather {
    private static DataGather dataGather;
    private OkHttpClient client= new OkHttpClient.Builder().build();

    public ShareData  query(String code,String beginDateStr,String endDateStr){
          ShareData shareData=null;
        try {
            LocalDate beginDate=LocalDate.parse(beginDateStr);
            LocalDate endDate=LocalDate.parse(endDateStr);

            String s = client.newCall(new Request.Builder().url("http://www.aigaogao.com/tools/history.html?s="+code).build()).execute().body().string();
            List<OneDayData> oneDayDatas=new ArrayList<>();
            Document document= Jsoup.parse(s);
            document.select("#ctl16_contentdiv tr").iterator().forEachRemaining(i->{
                String[] array= i.text().split(" ");
                if(array[0].contains("/")){

                    LocalDate date = LocalDate.parse(array[0], DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    if(date.isAfter(endDate)||date.isBefore(beginDate)){
                        return;
                    }
                    OneDayData oneDayData=new OneDayData();
                    oneDayData.date= date.format(DateTimeFormatter.ISO_LOCAL_DATE);
                    oneDayData.openingPrice=Double.valueOf(array[1]);
                    oneDayData.highestPrice=Double.valueOf(array[2]);
                    oneDayData.minimumPrice=Double.valueOf(array[3]);
                    oneDayData.closingPrice=Double.valueOf(array[4]);
                    oneDayData.volume=Integer.valueOf(array[5].replace(",",""));
                    oneDayData.turnover=Double.valueOf(array[6].replace(",",""));
                    oneDayData.increasePrice=Double.valueOf(array[7].replace("/","0"));
                    oneDayData.increase=Double.valueOf(array[8].replace(" %","").replace("/","0"));
                    oneDayDatas.add(oneDayData);
                }
            });

            shareData=new ShareData();
            shareData.stockCode=code;
            shareData.name=document.select("title").text().split("\\(")[0];
            shareData.dayDatas=fillUp(oneDayDatas.stream().sorted((o1,o2)->{
                return  LocalDate.parse(o1.date).isBefore(LocalDate.parse(o2.date))?-1:1;
            }).collect(Collectors.toList()),beginDate,endDate);
            System.out.println(shareData.dayDatas.size());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return shareData;

    }
    public List<OneDayData> fillUp(List<OneDayData> oneDayDatas,final LocalDate begin,final  LocalDate end){
        final LocalDate tmp1=begin.plusDays(0);
        List<OneDayData> result=new ArrayList<>();
        OneDayData lastData=oneDayDatas.get(0).clone();
        lastData.date=begin.format(DateTimeFormatter.ISO_LOCAL_DATE);

        for( LocalDate i=begin.plusDays(0);i.isBefore(end)||i.equals(end);i=i.plusDays(1)){

            final  LocalDate tmp=i.plusDays(0);
            if(!oneDayDatas.stream().anyMatch(item -> {
                return   LocalDate.parse(item.date).equals(tmp);
            })){
                lastData=lastData.clone();
                lastData.date=i.format(DateTimeFormatter.ISO_LOCAL_DATE);
                result.add(lastData);
            }else{

                lastData=oneDayDatas.stream().filter(item->{
                    return   LocalDate.parse(item.date).equals(tmp);
                }).findFirst().get().clone();

                result.add(lastData);
            }

        }
        return  result;
    }
    public  List<ShareData> query(QueryParam queryParam){
       return queryParam.getShareCodes().stream().map(i->{
            return query(i,queryParam.getBeginDate(),queryParam.getEndDate());
        }).filter(i->{
            return i.dayDatas.size()>0;
       }).sorted((o1,o2)->{
           return  -o1.dayDatas.size()+o2.dayDatas.size();
       }).collect(Collectors.toList());
    }
    public static synchronized DataGather getInstance() {
        if (dataGather == null) {
            dataGather = new DataGather();
        }
        return dataGather;
    }
}
