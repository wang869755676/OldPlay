package com.td.oldplay.bean;

import java.util.List;

/**
 * Created by my on 2017/7/18.
 */

public class ShopDetail {


    public ShopBean goods;
    public GroupBuy groupBuy;
    public MarkDownBean markDown;
    public List<ColorListBean> colorList;
    public List<GoodsImageListBean> goodsImageList;
    public List<String> sizeList;
    public String userId;






    public static class GoodsImageListBean {
        /**
         * goodsId : 1
         * imageId : 1
         * imageUrl : /image/i.png
         */

        public String goodsId;
        public String imageId;
        public String imageUrl;


    }
}
