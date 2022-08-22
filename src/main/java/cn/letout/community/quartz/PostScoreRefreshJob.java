package cn.letout.community.quartz;

import cn.letout.community.entity.DiscussPost;
import cn.letout.community.service.DiscussPostService;
import cn.letout.community.service.ElasticsearchService;
import cn.letout.community.service.LikeService;
import cn.letout.community.util.CommunityConstant;
import cn.letout.community.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 热帖排行 Job
 * 计算帖子分数
 */
@Slf4j
public class PostScoreRefreshJob implements Job, CommunityConstant {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    // 论坛纪元
    // 论坛开始的时间，计算分数时，越早的帖子，分数会更低
    private static final Date epoch;

    // 静态代码块：用 static 声明，jvm加载类时执行，仅执行一次
    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-08-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("初始化论坛纪元失败!", e);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String redisKey = RedisKeyUtil.getPostScoreKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);

        if (operations == null) {  // Set，在其中，说明帖子信息有更新；如果没有更新，跳过任务，不计算分数
            log.info("[任务取消] 没有要刷新的帖子!");
            return;
        }

        log.info("[任务开始] 正在刷新帖子分数:", operations.size());
        while (operations.size() > 0) {
            refresh((Integer) operations.pop());
        }
        log.info("[任务结束] 帖子分数刷新完毕!");
    }

    private void refresh(int postId) {
        DiscussPost post = discussPostService.findDiscussPostById(postId);
        if (post == null) {
            log.info("该帖子不存在:id=" + postId);
        }

        // 是否精华
        boolean wonderful = post.getStatus() == 1;
        // 评论数量
        int commentCount = post.getCommentCount();
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, postId);

        // 计算权重
        double w = (wonderful ? 75 : 0) + commentCount * 10 + likeCount * 2;
        // 分数=帖子权重+距离天数
        // w可能小于1，因为log存在，所以送入log的最小值应该为0
        // getTime()单位为ms
        double score = Math.log10(Math.max(1, w)) +
                (post.getCreateTime().getTime() - epoch.getTime()) / (3600 * 60 * 24);

        // 更新帖子分数
        discussPostService.updateScore(postId, score);
        // 更新elasticsearch
        post.setScore(score);
        elasticsearchService.saveDiscussPost(post);
    }

}
