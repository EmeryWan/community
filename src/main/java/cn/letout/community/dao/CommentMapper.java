package cn.letout.community.dao;

import cn.letout.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    // 获取评论列表
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    // 获取评论数
    int selectCountByEntity(int entityType, int entityId);

    // 添加评论
    int insertComment(Comment comment);

    Comment selectCommentById(int id);
}
