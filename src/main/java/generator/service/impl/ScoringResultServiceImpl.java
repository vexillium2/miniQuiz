package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.ScoringResult;
import generator.service.ScoringResultService;
import generator.mapper.ScoringResultMapper;
import org.springframework.stereotype.Service;

/**
* @author Jacob
* @description 针对表【scoring_result(评分结果)】的数据库操作Service实现
* @createDate 2024-12-19 19:26:53
*/
@Service
public class ScoringResultServiceImpl extends ServiceImpl<ScoringResultMapper, ScoringResult>
    implements ScoringResultService{

}




