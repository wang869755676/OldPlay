package com.td.oldplay.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by my on 2017/7/11.
 */

public class OrderBean implements Serializable {
    public List<GoodBean> goodBeanList;

    /**
     * addTime : {"date":18,"day":2,"hours":17,"minutes":51,"month":6,"seconds":19,"time":1500371479679,"timezoneOffset":-480,"year":117}
     * address : {"addTime":{"date":11,"day":2,"hours":0,"minutes":0,"month":6,"seconds":0,"time":1499702400000,"timezoneOffset":-480,"year":117},"address":"隔壁","addressId":3,"consignee":"1234567890","isDefault":1,"mobile":"1234567890","userId":1}
     * addressId : 3
     * amount_paid : 231
     * amount_payable : 231
     * logisticnum :
     * orderDetails : [{"color":"红色","goodsId":1,"goodsName":"有机花菜","groupBuy":null,"id":0,"markDown":{"conditions":1,"content":"满20减19","endTime":null,"goodsId":1,"id":3,"number":0,"price":20,"rebate":0,"startTime":null,"status":0,"subtract":19,"type":1},"number":5,"orderId":0,"price":50,"size":"A"}]
     * orderId : 8
     * orderNum : 201719917001
     * overTime : null
     * payTime : null
     * payment_way : 0
     * sendTime : null
     * status : 0
     * userId : 1
     */

    public AddressBean address;
    public String addressId;
    public float amount_paid;
    public float amount_payable;
    public String logisticnum;
    public String orderId;
    public String orderNum;
    public TimeInfo overTime;
    public TimeInfo payTime;
    public int payment_way;
    public TimeInfo sendTime;
    public int status;
    public int userId;
    public List<GoodBean> orderDetails;
}
