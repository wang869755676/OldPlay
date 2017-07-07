package com.td.oldplay.bean;

import java.util.List;

/**
 * Created by my on 2017/7/6.
 */

public class HomeCourseBean {
    private List<CourseBean> recommends;
    private List<CourseBean> hos;

    public List<CourseBean> getRecommends() {
        return recommends;
    }

    public void setRecommends(List<CourseBean> recommends) {
        this.recommends = recommends;
    }

    public List<CourseBean> getHos() {
        return hos;
    }

    public void setHos(List<CourseBean> hos) {
        this.hos = hos;
    }
}
