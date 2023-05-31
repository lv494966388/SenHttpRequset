package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;

import com.example.demo.entity.Barrage;
import com.example.demo.entity.User;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import java.math.BigInteger;
import java.security.MessageDigest;

import java.util.*;

@Api(value = "模拟登陆发送弹幕")
@Controller
public class SenBarrageController {

    protected final static Logger log = LoggerFactory.getLogger(SenBarrageController.class);

    @ApiOperation(value="登陆", notes="登陆")
    @ResponseBody
    @RequestMapping(value = "/start" ,method = RequestMethod.GET)
    public String start(String name ,String password) {

        //颜色集合
        List<String> list=new ArrayList<String>();
        list.add("#ff780a");
        list.add("#0aebff");
        list.add("#fff000");
        //弹幕内容集合
        List<String> barragelist = new ArrayList<String>();
        barragelist.add("针不戳，针不戳，雷神加速针不戳！ ");
        barragelist.add("祝雷神生日快乐前程似锦 也祝我可以中个电竞椅 ");
        barragelist.add("6666666666666666 ");
        barragelist.add("雷妹是我的！ ");
        barragelist.add("加速用雷神，不用可暂停 ");
        barragelist.add("雷神，永远滴神");
        barragelist.add("雷神五周年生日快");
        barragelist.add("这是有多少人中啊");
        barragelist.add("我也想要中奖");
        barragelist.add("雷神我最爱，是我了吧");


        //开始登陆的时间
        String oldTime = "";
        //加密后的密码
        String md5String = getMD5String(password);
        //开始创建登陆对象
        User user = new User();
        user.setLang("zh_CN");
        user.setPassword(md5String);
        user.setSrc_channel("");
        user.setRegion_code("1");
        user.setUser_type("0");
        user.setUsername(name);
        //转换Json
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(user);
        //开始请求登陆
        String s = jsonObject.toString();

        //拿到Token
        String token = sendPostLogin(s);

        Barrage barrage = new Barrage();
        barrage.setAccount_token(token);
        barrage.setContent(barragelist.get(new Random().nextInt(10)));
        barrage.setContent_color(list.get(new Random().nextInt(3)));
        barrage.setBarrage_level("2");
        barrage.setLang("zh_CN");
        barrage.setRegion_code("1");
        barrage.setSrc_channel("guanwang");

        JSONObject o = (JSONObject) JSONObject.toJSON(barrage);
        //开始发送弹幕
        String s1 = sendDan(o.toString());
        System.out.println(s1);
        return "任务开始了";
    }

    /*获取token*/
    public String sendPostLogin(String param){
        String url="https://webapi.leigod.com/api/auth/login";
        String sendPost = sendPost2(url, param);
        log.info(sendPost);
        //登陆成功 获取json
        JSONObject jsonObj = JSONObject.parseObject(sendPost);
        JSONObject.parseObject(jsonObj.get("data").toString()).get("login_info").toString();
        String s = JSONObject.parseObject(JSONObject.parseObject(jsonObj.get("data").toString()).get("login_info").toString()).get("account_token").toString();
        return s;
    }

    public String sendDan(String param){
        final String param2 = param;
        final String url="https://webapi.leigod.com/api/barrage/send";
//        String sendPost = sendPost2(url, param);

        new Thread(()->{
            while (true){
                try {
                    Thread.sleep(5000+new Random().nextInt(3000));
                    String sendPost = sendPost2(url, param2);
                    log.info("请求结果 ： {} ",sendPost);
                } catch (InterruptedException e) {
                }
            }
        }).start();

//        System.out.println(sendPost);
       log.info(param);
        return "操作成功";
    }







    public static String sendPost2(String url, String data) {
        String response = null;
        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpresponse = null;
            try {
                httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost(url);
                StringEntity stringentity = new StringEntity(data,
                        ContentType.create("text/json", "UTF-8"));
                httppost.setEntity(stringentity);
                httpresponse = httpclient.execute(httppost);
                response = EntityUtils
                        .toString(httpresponse.getEntity(),"UTF-8");

            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (httpresponse != null) {
                    httpresponse.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /*密码MD5加密*/
    private static String getMD5String(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
