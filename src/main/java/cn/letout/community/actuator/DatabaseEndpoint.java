package cn.letout.community.actuator;

import cn.letout.community.util.CommunityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 自定义 actuator Endpoint 监控
 * 监控数据库信息
 */
@Slf4j
@Component
@Endpoint(id = "database")
public class DatabaseEndpoint {

    @Autowired
    private DataSource dataSource;

    @ReadOperation
    public String checkConnection(){
        try (Connection conn = dataSource.getConnection()) {
            return CommunityUtil.getJSONString(0, "获取连接成功!");
        } catch (SQLException e) {
            log.error("获取连接失败:" + e.getMessage());
            return CommunityUtil.getJSONString(1, "获取连接失败!");
        }
    }

}
