package cn.letout.community.util;

/**
 * Redis Key 生成工具
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";


    // 实体点赞前缀 （帖子，评论，用户等）
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    // 统计某个用户的赞数量
    private static final String PREFIX_USER_LIKE = "like:user";


    // 用户关注实体
    private static final String PREFIX_FOLLOWEE = "followee";
    // 某个实体被哪些用户关注了
    private static final String PREFIX_FOLLOWER = "follower";


    // 验证码
    private static final String PREFIX_KAPTCHA = "kaptcha";


    // 登录凭证
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";


    // 网站数据统计
    private static final String PREFIX_UV = "uv";
    private static final String PREFIX_DAU = "dau";


    private static final String PREFIX_POST = "post";


    // 某个实体收到的赞，如帖子，评论  like:entity:entityType:entityId
    // 将对应点赞的 userId 存到 set 中
    // set(userId) 对应 set，存入 userId
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + entityType + SPLIT + entityId;
    }

    // 某个用户收到的总赞数
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }


    // 某个用户关注的实体
    // followee:userId:entityType
    // zset(entityId,date) 有序集合 存的是关注的哪个实体 分数是当前时间
    // 为了后期统计方便，统计关注了哪些，进行排序列举
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个实体拥有的粉丝，实体可能是用户，或者是帖子
    // follower:entityType:entityId
    // zset(userId,date)  存入用户Id
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityType;
    }


    // 登录验证码
    // owner是指随机生成的 uuid 用户的临时凭证
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    // 登录的凭证
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // 用户
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    // 单日uv
    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    // 区间UV
    public static String getUVKey(String startDate, String endData) {
        return PREFIX_UV + SPLIT + startDate + SPLIT + endData;
    }

    // 单日DAU
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    // 区间DAU
    public static String getDAUKey(String startDate, String endDate) {
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    // 帖子分数
    public static String getPostScoreKey() {
        return PREFIX_POST + SPLIT + "score";
    }

}