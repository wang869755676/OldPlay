package com.td.oldplay.bean;

import java.io.Serializable;

public class GroupBuy implements Serializable{

    public int buyNum;  // 购买时不能超过的数量
    public int conditions;
    public String content;
    public TimeInfo endTime;
    public String goodsId;
    public String id;
    public int number;
    public float price;
    public TimeInfo startTime;
    public int status; //0:团购中, 1 团购成功 2 团购失败

}