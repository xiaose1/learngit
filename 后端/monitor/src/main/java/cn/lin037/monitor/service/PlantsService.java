package cn.lin037.monitor.service;

import cn.lin037.monitor.domain.Plants;
import cn.lin037.monitor.domain.vo.PlantSearchedVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 18333
* @description 针对表【plants】的数据库操作Service
* @createDate 2024-05-31 17:54:42
*/
public interface PlantsService extends IService<Plants> {

    Long getCount();
    Page<Plants> search(Integer pageNum, Integer plantNum, String searchStr);

    List<PlantSearchedVO> search(String searchStr);
}
