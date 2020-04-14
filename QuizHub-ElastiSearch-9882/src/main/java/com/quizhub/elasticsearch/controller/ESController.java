package com.quizhub.elasticsearch.controller;

import com.alibaba.fastjson.JSONObject;
import com.quizhub.elasticsearch.utils.ESUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.junit.Test;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-04-13
 */
@RestController
@Api(tags = "ES调试接口")
@RequestMapping("/es/")
public class ESController {

    @PostMapping("/indeies")
    @ApiOperation(value = "创建索引")
    public void createIndex(String index) {
        ESUtils.createIndex(index);
    }

    @PostMapping("/indeies/{index}/{type}/{id}")
    @ApiOperation(value = "添加数据")
    public void insertData(@PathVariable("index") String index,
                           @PathVariable("type") String type,
                           String name, String text
    ) {
        for (int i = 0; i < 100; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("name", name);

            map.put("text", text);

            ESUtils.addData(map, index, type);
        }
    }

    @GetMapping("/indeies/{index}/{type}/{id}")
    @ApiOperation(value = "分词查询")
    public List<String> searchListData(@PathVariable("index") String index,
                                       @PathVariable("type") String type, String text) {


        List<Map<String, Object>> list = ESUtils.searchAllData(index, type, null, "text=" + text);
        List<String> strlist = new ArrayList<>();
        for (Map<String, Object> item : list) {

            strlist.add(item.get("text").toString());
        }

        return strlist;
    }


}
