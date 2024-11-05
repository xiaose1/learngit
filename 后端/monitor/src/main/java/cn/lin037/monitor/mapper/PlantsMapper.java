package cn.lin037.monitor.mapper;

import cn.lin037.monitor.domain.Plants;
import cn.lin037.monitor.domain.vo.PlantSearchedVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 18333
* @description 针对表【plants】的数据库操作Mapper
* @createDate 2024-05-31 17:54:42
* @Entity generator.domain.Plants
*/
@Mapper
public interface PlantsMapper extends BaseMapper<Plants> {

    @Select("SELECT plants.plant_id, plants.plant_name FROM plants where plants.plant_name like #{searchStr}")
    List<PlantSearchedVO> searchPlans(String searchStr);
}




