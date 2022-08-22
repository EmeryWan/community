package cn.letout.community.dao.alpha;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary  // 优先级最高，优先加载，当注入 bean 冲突时，以 @Primary 定义的为准
public class AlphaDaoMyBatisImpl implements AlphaDao {

    @Override
    public String select() {
        return "MyBatis";
    }

}
