package com.td.oldplay.bean;

import java.io.Serializable;

/**
 * Created by my on 2017/7/10.
 */

public class AddressBean  implements Serializable{


    /**
     * addTime : null
     * address : 杭州
     * addressId : 1
     * consignee : 小明
     * isDefault : 1
     * mobile : 12345678
     * userId : 1
     */


    public String address;
    public String addressId;
    public String consignee;
    public int isDefault;  //是否默认0:不默认,1:默认
    public String mobile;
    public int userId;


}
