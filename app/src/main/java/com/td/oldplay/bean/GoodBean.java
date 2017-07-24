package com.td.oldplay.bean;

import java.io.Serializable;

/**
 * Created by my on 2017/7/11.
 */

public class GoodBean implements Serializable{


    /**
     * color : 红色
     * goodsId : 1
     * goodsName : 有机花菜
     * groupBuy : null
     * id : 0
     * markDown : {"conditions":1,"content":"满20减19","endTime":null,"goodsId":1,"id":3,"number":0,"price":20,"rebate":0,"startTime":null,"status":0,"subtract":19,"type":1}
     * number : 5
     * orderId : 0
     * price : 50
     * size : A
     */

    public String color;
    public int goodsId;
    public String goodsName;
    public GroupBuy groupBuy;
    public int id;
    public MarkDownBean markDown;
    public int number;
    public int orderId;
    public float price;
    public String size;
    public String goodsImageUrl;


}
