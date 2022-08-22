package cn.letout.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件，用于消息队列
 */
public class Event {

    // 事件类型
    private String topic;

    // 事件发出者
    private int userId;

    // 事件类型 （如 点赞 回复 关注）
    private int entityType;

    // 实体id （如贴子的id）
    private int entityId;

    // 实体的作者（如帖子作者）
    private int entityUserId;

    // 把其他额外的数据，存入map中，具有扩展性 目的：为了后期动态扩展，有些字段没有办法做出预判
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}
