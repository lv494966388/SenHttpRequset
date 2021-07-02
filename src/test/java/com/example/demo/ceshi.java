package com.example.demo;

import com.example.demo.controller.SenBarrageController;
import org.junit.jupiter.api.Test;

public class ceshi {
    @Test
    public static void main(String[] args) {
        SenBarrageController senBarrageController = new SenBarrageController();
        /*String json = senBarrageController.start("15255892312", "lv931219");
        System.out.println(json);*/
        String s = senBarrageController.sendPostLogin("123");
        System.out.println(s);
    }
}
