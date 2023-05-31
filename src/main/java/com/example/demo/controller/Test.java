package com.example.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Api(value = "测试类")
@Controller
public class Test {


    @ApiOperation(value="测试", notes="测试接口")
    @RequestMapping("test")
    @ResponseBody
    public String test(){
        return "123";
    }

    public static void main(String[] args) {




    }

}
