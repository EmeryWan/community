package cn.letout.community.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 私信实体类
 */
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = -6055301435110209507L;

    private int id;

    /**
     * 发送私信方
     */
    private int fromId;

    /**
     * 接收私信方
     */
    private int toId;

    /**
     * 11111_11113
     * fromId_toId / toId_fromId
     * 双方 id 的拼接，小的 id 在前
     *
     * 信息类别  comment follow like 评论 跟踪 点赞
     */
    private String conversationId;

    /**
     * 私信内容
     */
    private String content;

    /**
     * 0 未读 1 已读 2 删除
     */
    private int status;

    private Date createTime;

}
