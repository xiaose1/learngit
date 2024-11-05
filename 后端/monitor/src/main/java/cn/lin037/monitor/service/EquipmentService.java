package cn.lin037.monitor.service;

import cn.lin037.monitor.domain.Equipment;
import cn.lin037.monitor.domain.vo.EquipmentCountVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author 18333
* @description 针对表【equipment】的数据库操作Service
* @createDate 2024-05-31 17:01:07
*/
public interface EquipmentService extends IService<Equipment> {


    void switchBatchByIds(List<Integer> equipmentIds, Boolean isOpen, HttpServletRequest request);

    Page<Equipment> selectInPage(Integer pageNum, Integer equipmentNum, HttpServletRequest request);

    Page<Equipment> search(Integer pageNum, Integer equipmentNum, String searchStr, HttpServletRequest request);

    EquipmentCountVO getCount(HttpServletRequest request);
}
