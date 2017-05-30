package com.codingdie.shares.analyser.controller;

import com.codingdie.shares.analyser.model.QueryParam;
import com.codingdie.shares.analyser.model.ShareData;
import com.codingdie.shares.analyser.util.DataGather;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created by xupeng on 2017/5/30.
 */
@Controller
public class MainController {
    @RequestMapping("/")

    String hello(Map<String,Object> map){
        return  "index";
    }
    @ResponseBody
    @PostMapping("/query")
    List<ShareData> query(@RequestBody QueryParam queryParam){
       return DataGather.getInstance().query(queryParam);
    }
}
