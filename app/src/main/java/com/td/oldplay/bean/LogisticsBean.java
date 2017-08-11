package com.td.oldplay.bean;

import java.util.List;

/**
 * Created by my on 2017/7/14.
 */

public class LogisticsBean {

    /**
     * com : zhongtong
     * ischeck : 1
     * comurl : http://www.zto.cn/SiteInfo/index.aspx
     * condition : F00
     * data : [{"context":"[杭州文三西路] [杭州市] [杭州文三西路]的派件已签收 感谢使用中通快递,期待再次为您服务!","location":"","time":"2017-07-19 11:46:25"},{"context":"[杭州文三西路] [杭州市] [杭州文三西路]的张学涛正在第1次派件 电话:15157189755 请保持电话畅通、耐心等待","location":"","time":"2017-07-19 07:45:40"},{"context":"[杭州文三西路] [杭州市] 快件到达 [杭州文三西路]","location":"","time":"2017-07-19 07:21:09"},{"context":"[杭州中转部] [嘉兴市] 快件到达 [杭州中转部]","location":"","time":"2017-07-19 00:43:30"},{"context":"[杭州中转部] [嘉兴市] 快件离开 [杭州中转部]已发往[杭州文三西路]","location":"","time":"2017-07-19 00:30:48"},{"context":"[金华中转部] [金华市] 快件离开 [金华中转部]已发往[杭州中转部]","location":"","time":"2017-07-18 21:17:37"},{"context":"[金华中转部] [金华市] 快件到达 [金华中转部]","location":"","time":"2017-07-18 20:54:05"},{"context":"[义乌中转部] [金华市] 快件离开 [义乌中转部]已发往[金华中转部]","location":"","time":"2017-07-18 19:39:45"},{"context":"[义乌中转部] [金华市] 快件到达 [义乌中转部]","location":"","time":"2017-07-18 19:38:21"},{"context":"[义乌营销部] [金华市] 快件离开 [义乌营销部]已发往[杭州]","location":"","time":"2017-07-18 17:43:14"},{"context":"[义乌营销部] [金华市] [义乌营销部]的义乌市场营销部已收件 电话:17769759013","location":"","time":"2017-07-18 17:42:26"}]
     * comcontact : 95311
     * nu : 446372004622
     * state : 3
     * message : ok
     * status : 1
     */

    public String com;
    public String ischeck;
    public String comurl;
    public String condition;
    public String comcontact;
    public String nu;
    public String state;
    public String message;
    public String status;
    public List<DataBean> data;


    public static class DataBean {
        /**
         * context : [杭州文三西路] [杭州市] [杭州文三西路]的派件已签收 感谢使用中通快递,期待再次为您服务!
         * location :
         * time : 2017-07-19 11:46:25
         */

        public String context;
        public String location;
        public String time;

        public DataBean(String context) {
            this.context = context;
        }

        public DataBean() {
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
