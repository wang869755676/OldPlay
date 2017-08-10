package com.td.oldplay.bean;

/**
 * Created by my on 2017/7/18.
 */

public class TeacherDetail {

    /**
     * addTime : {"date":17,"day":1,"hours":15,"minutes":21,"month":6,"seconds":33,"time":1500276093000,"timezoneOffset":-480,"year":117}
     * contentUrl :
     * coursesId : 1
     * coursesTypeId : 1
     * name : 疯狂的Java
     * price : 120
     * profile : 这是一个很好的课程
     * user : {"addTime":{"date":17,"day":1,"hours":15,"minutes":20,"month":6,"seconds":14,"time":1500276014000,"timezoneOffset":-480,"year":117},"avatar":"/lnrzb/images/1500088656435.wmv","classHour":0,"money":0,"nickName":"123124","password":"123","payPassword":"123","phone":"1234","profile":"毕业于清华大学","score":500,"uType":1,"userId":1}
     * userId : 1
     */

    public String contentUrl;
    public String coursesId;
    public int coursesTypeId;
    public String name;
    public float price;
    public String teacherDescribes;
    public String coursesDescribes;
    public UserBean user;
    public String userId;
}
