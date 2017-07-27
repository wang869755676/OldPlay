package com.td.oldplay.pay;

import java.io.Serializable;

import org.json.JSONObject;

import com.google.gson.Gson;

public class PayOrderId implements Serializable {
	/**    "out_trade_no": "V2016070917242605705910098315",
        "user_id": "1539",
        "student_id": "1539",
        "type": "flower",
        "start_time": "2016-07-09 17:24:26",
        "quantity": "500",
        "expiry_start_date": "2016-07-09",
        "expiry_end_date": "2016-12-08",
        "totalfee": "240" */
	private static final long serialVersionUID = 1L;
	
	public String out_trade_no;
	public String userid;
	public String student_id;
	public String type;
	public String start_time;
	public String quantity;
	public String totalfee;
	public String expiry_start_date;
	public String expiry_end_date;

	

	
	public static PayOrderId fromJson(JSONObject json) {
		if(json != null){
            return new Gson().fromJson(json.toString(), PayOrderId.class);
        }
		return null;
	}

}
