package com.td.oldplay.bean;

import java.io.Serializable;

/**
 * Created by my on 2017/7/14.
 */

public class UserBean  implements Serializable{

    /**
     * avatar : /images/1499762427663.jpg
     * classHour : 0
     * money : 0
     * nickName : 葬爱
     * password : 123
     * payPassword :
     * phone : 1234
     * profile : 123
     * score : 500
     * uType : 1
     * userId : 1
     */

    public String avatar;
    public int classHour;
    public float money;
    public String nickName;
    public String password;
    public String payPassword;
    public String phone;
    public String profile;
    public String score;
    public int uType;   // 用户类型,0:表示普通用户,1:表示主播
    public String userId;


}
