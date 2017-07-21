package com.td.oldplay.bean;

import java.io.Serializable;

/**
 * Created by my on 2017/7/10.
 */

public class ForumBean implements Serializable{

    /**
     * boardId : 1
     * content :
     * label : 0
     * replyCount : 2
     * status : 0
     * title : 论世界的十大未解之谜
     * topicId : 1
     * userId : 1
     * userName : 小明
     */

    public String boardId;
    public String content;
    public int label;  //帖子标签,0:非精品,1:精品贴
    public int replyCount;
    public int status;
    public String formatTime;
    public String title;
    public String topicId;
    public String userId;
    public String userName;
    public UserBean user;


}
