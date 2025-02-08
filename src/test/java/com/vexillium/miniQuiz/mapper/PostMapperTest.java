package com.vexillium.miniQuiz.mapper;

import com.vexillium.miniQuiz.model.entity.Post;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 帖子数据库操作测试
 *
 * @author <a href="https://github.com/vexillium2">vexillium</a>
 * @from <a href="https://vexillium.icu"></a>
 */
@SpringBootTest
class PostMapperTest {

    @Resource
    private PostMapper postMapper;

    @Test
    void listPostWithDelete() {
        List<Post> postList = postMapper.listPostWithDelete(new Date());
        Assertions.assertNotNull(postList);
    }
}