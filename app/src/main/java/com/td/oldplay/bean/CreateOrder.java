package com.td.oldplay.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by my on 2017/7/19.
 */

public class CreateOrder implements Serializable {
    public AddressBean address;
    public List<GoodBean> orderDetails;
    public int amount_paid;
    public int amount_payable;
    public String orderNum;
}
