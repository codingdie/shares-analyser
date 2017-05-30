package com.codingdie.shares.analyser;

import com.codingdie.shares.analyser.model.OneDayData;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.security.provider.SHA;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupeng on 2017/5/29.
 */
@SpringBootApplication
public class SharesAnalyseApplication {
    public static void main(String[] args) throws  Exception {
        SpringApplication.run(SharesAnalyseApplication.class);
    }
}
