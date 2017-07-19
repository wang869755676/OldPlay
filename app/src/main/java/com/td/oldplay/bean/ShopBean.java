package com.td.oldplay.bean;

/**
 * Created by my on 2017/7/5.
 */

public class ShopBean {


    /**
     * addtime : {"date":13,"day":4,"hours":10,"minutes":7,"month":6,"seconds":18,"time":1499911638000,"timezoneOffset":-480,"year":117}
     * goodsId : 1
     * goodsImages : []
     * goodsName : 有机花菜
     * goodsTypeId : 1
     * isCommend : 1
     * isPreferential : 0
     * picUrl :
     * price : 50
     * profile : 无公害有机产品
     * score : 5
     * sellNum : 100
     * shopId : 1
     * state : 0
     */

    public String goodsId;
    public String goodsName;
    public int goodsTypeId;
    public int isCommend;
    public int isPreferential;  //0.不优惠,1.满减,2:团购
    public String picUrl;
    public int price;
    public String profile;
    public int score;
    public int sellNum;
    public int shopId;
    public int state;

}
