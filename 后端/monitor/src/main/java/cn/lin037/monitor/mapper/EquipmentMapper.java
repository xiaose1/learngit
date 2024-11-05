package cn.lin037.monitor.mapper;

import cn.lin037.monitor.domain.Equipment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.Mapping;

/**
* @author 18333
* @description 针对表【equipment】的数据库操作Mapper
* @createDate 2024-05-31 17:01:07
* @Entity cn.lin037.monitor.domain.Equipment
*/
@Mapper
public interface EquipmentMapper extends BaseMapper<Equipment> {

}




