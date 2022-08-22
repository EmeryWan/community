package cn.letout.community.controller;

import cn.letout.community.entity.Event;
import cn.letout.community.entity.User;
import cn.letout.community.event.EventProducer;
import cn.letout.community.service.LikeService;
import cn.letout.community.util.CommunityConstant;
import cn.letout.community.util.CommunityUtil;
import cn.letout.community.util.HostHolder;
import cn.letout.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 点赞相关 Controller
 */
@Controller
public class LikeController implements CommunityConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    // ---

    /**
     * 点赞
     *
     * @param entityType   CommunityConstant 帖子1 评论2 用户3
     * @param entityId     帖子 评论 用户 id
     * @param entityUserId 被赞的人 id
     * @param postId       帖子的 id
     */
    @PostMapping(path = "/like")
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId, int postId) {
        User user = hostHolder.getUser();
        // 点赞 （操作 redis）
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        // 点赞状态
        int likeStatus = likeService.findEntityLikeStatus(entityType, entityId, user.getId());

        // 返回结果，该 帖子 评论 用户 的点赞数量，当前用户是否点赞
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        // 触发点赞事件
        if (likeStatus == 1) {  // 仅点赞需要通知
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId", postId);
            eventProducer.fireEvent(event);

            if (entityType == ENTITY_TYPE_POST) {
                // 计算帖子分数
                String redisKey = RedisKeyUtil.getPostScoreKey();
                redisTemplate.opsForSet().add(redisKey, postId);
            }
        }

        return CommunityUtil.getJSONString(0, null, map);
    }

}
