package cn.lin037.monitor.service.impl;

import cn.lin037.monitor.domain.Plants;
import cn.lin037.monitor.domain.vo.PlantSearchedVO;
import cn.lin037.monitor.mapper.PlantsMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lin037.monitor.service.PlantsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 18333
* @description 针对表【plants】的数据库操作Service实现
* @createDate 2024-05-31 17:54:42
*/
@Service
public class PlantsServiceImpl extends ServiceImpl<PlantsMapper, Plants>
    implements PlantsService{

    @Override
    public Long getCount() {

        LambdaQueryWrapper<Plants> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Plants::getPlantId);
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public Page<Plants> search(Integer pageNum, Integer plantNum, String searchStr) {

        LambdaQueryWrapper<Plants> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Plants::getPlantId, searchStr)
                        .or().like(Plants::getPlantHumidity, searchStr)
                        .or().like(Plants::getPlantName, searchStr)
                        .or().like(Plants::getPlantInformation, searchStr)
                        .or().like(Plants::getPlantTemperature, searchStr);
        return baseMapper.selectPage(new Page<>(pageNum, plantNum), queryWrapper);
    }

    @Override
    public List<PlantSearchedVO> search(String searchStr) {

        return baseMapper.searchPlans(searchStr);
    }
}




