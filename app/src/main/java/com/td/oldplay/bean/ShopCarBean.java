package com.td.oldplay.bean;

/**
 * Created by my on 2017/7/12.
 */

public class ShopCarBean {

    public boolean isCheck;
    public int num;

    public String cartId;
    public ColorListBean color;
    public String colorId;
    public ShopBean goods;
    public String goodsId;
    public int number;
    public String size;
    public float total;
    public String userId;

    public ColorListBean getColor() {
        return color==null?new ColorListBean():color;
    }

    public void setColor(ColorListBean color) {
        this.color = color;
    }

    public ShopBean getGoods() {
        return goods==null? new ShopBean():goods;
    }

    public void setGoods(ShopBean goods) {
        this.goods = goods;
    }
}
