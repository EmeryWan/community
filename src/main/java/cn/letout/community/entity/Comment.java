package cn.letout.community.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论实体类
 */
@Data
public class Comment implements Serializable {

    private static final long serialVersionUID = -9150903232853475280L;

    private int id;

    /**
     * 评论发出的用户
     */
    private int userId;

    /**
     * 表明这个评论的类型(是属于帖子的评论，还是评论的评论，还是问题的评论)
     */
    private int entityType;

    /**
     * 评论的帖子是哪一个
     */
    private int entityId;

    /**
     * 评论指向的人
     */
    private int targetId;

    /**
     * 评论的内容
     */
    private String content;

    /**
     * 状态 0为正常的 1为删除的或者错误
     */
    private int status;

    private Date createTime;

}
