package cn.letout.community.service;

import cn.letout.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * 点赞Service
 */
@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 对实体点赞, 对相应的用户加赞
     * @param userId 点赞人
     * @param entityType 点赞类型 给 评论点赞: 1  帖子: 2  用户: 3
     * @param entityId 被点赞的 评论 帖子 用户 的 id
     * @param entityUserId 被赞人id（用户统计该用户收到了多少赞）
     */
    public void like(int userId, int entityType, int entityId, int entityUserId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);  // 判断是否点过赞
                // 多个更新操作，需要事务
                operations.multi();
                if (isMember) {  // 取消赞
                    redisTemplate.opsForSet().remove(entityLikeKey, userId);
                    redisTemplate.opsForValue().decrement(userLikeKey);  // 统计该用户被赞的数量
                } else { // 点赞
                    redisTemplate.opsForSet().add(entityLikeKey, userId);
                    redisTemplate.opsForValue().increment(userLikeKey);
                }
                return operations.exec();
            }
        });

    }


    /**
     * 查询某实体（帖子，评论等）点赞数量
     **/
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }


    /**
     * 查询某人对某实体的点赞状态
     * 如：当前用户是否对该帖子/评论是否点赞
     **/
    public int findEntityLikeStatus(int entityType, int entityId, int userId) {
        // like:entity:entityType:entityId
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        // 此处返回int，是为了进行扩展。比如扩展 踩:2 等等情况
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }


    /**
     * 查询某个用户获得赞，用于在个人主页查看收获了多少赞
     **/
    public int findUserLikeCount(int userId) {
        // like:user:userId
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count.intValue();
    }

}
