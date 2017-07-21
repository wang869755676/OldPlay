package com.td.oldplay.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by my on 2017/7/20.
 */

public class ForumDetial implements Serializable {

    /**
     * imageUrlList : ["/image/1.png","/image/1.png"]
     * speechUrlList : ["/image/1.png","/image/1.png"]
     * topic : {"addTime":{"date":14,"day":5,"hours":18,"minutes":15,"month":6,"seconds":15,"time":1500027315000,"timezoneOffset":-480,"year":117},"boardId":1,"content":"","label":0,"replyCount":2,"status":0,"title":"论世界的十大未解之谜","topicId":1,"userId":1,"userName":""}
     * videoUrlList : ["/image/1.png","/image/1.png"]
     */

    public ForumBean topic;
    public List<String> imageUrlList;
    public List<String> speechUrlList;
    public List<String> videoUrlList;


}
