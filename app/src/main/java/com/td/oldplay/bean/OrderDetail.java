package com.td.oldplay.bean;

import java.util.List;

/**
 * Created by my on 2017/7/24.
 */

public class OrderDetail {

    /**
     * addTime : {"date":23,"day":0,"hours":16,"minutes":10,"month":6,"seconds":7,"time":1500797407000,"timezoneOffset":-480,"year":117}
     * address : {"addTime":null,"address":"阿拉斯加","addressId":1,"consignee":"12345678","isDefault":1,"mobile":"12345678","userId":1}
     * addressId : 1
     * amount_paid : 50
     * amount_payable : 50
     * formatTime : 2017.07.23 16:10:07
     * isApplyScore : 0
     * logistic :
     * logisticnum :
     * orderDetails : [{"color":"红色","goodsId":3,"goodsImageUrl":"","goodsName":"青椒","groupBuy":null,"id":28,"markDown":null,"number":2,"orderId":17,"orderNum":"B201720416003998","price":25,"size":"A"}]
     * orderId : 17
     * orderNum : B201720416003998
     * overTime : null
     * payNum : 201720416003792
     * payTime : null
     * payment_way : 0
     * scoreOffset : null
     * sendTime : null
     * status : 0
     * userId : 1
     */

   public AddressBean address;
   public String addressId;
   public float amount_paid;  // 实际支付
   public float amount_payable; // 总额
   public String formatTime;
   public int isApplyScore;  //  "isApplyScore": 0, //是否使用积 0:不使用,1:使用
   public String logistic;
   public String logisticnum;
   public String orderId;
   public String orderNum;
   public String payNum;
   public int payment_way;  //payment_way	int	支付方式 0:余额支付,1:支付宝,2:微信
   public ScoreOffset scoreOffset;
   public int status;
   public int userId;
   public List<GoodBean> orderDetails;
}
