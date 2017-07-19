package com.td.oldplay.bean;

public class GroupBuy {

    public int buyNum;  // 购买时不能超过的数量
    public int conditions;
    public String content;
    public TimeInfo endTime;
    public String goodsId;
    public String id;
    public int number;
    public int price;
    public TimeInfo startTime;
    public int status; //优惠状态0:优惠还未开始,1:优惠中,2:优惠已结束

}