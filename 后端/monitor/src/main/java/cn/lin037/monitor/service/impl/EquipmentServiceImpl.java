package cn.lin037.monitor.service.impl;

import cn.lin037.monitor.domain.vo.EquipmentCountVO;
import cn.lin037.monitor.domain.vo.UsersVO;
import cn.lin037.monitor.enums.CodeEnum;
import cn.lin037.monitor.exception.CommonException;
import cn.lin037.monitor.service.EquipmentService;
import cn.lin037.monitor.utils.FinalValueUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lin037.monitor.domain.Equipment;
import cn.lin037.monitor.mapper.EquipmentMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
* @author 18333
* @description 针对表【equipment】的数据库操作Service实现
* @createDate 2024-05-31 17:01:07
*/
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment>
    implements EquipmentService {

    @Override
    @Transactional
    public void switchBatchByIds(List<Integer> equipmentIds, Boolean isOpen, HttpServletRequest request) {

        UsersVO usersVO = (UsersVO) request.getSession().getAttribute(FinalValueUtil.USER_LOGIN_STATUS);
        if (null == usersVO) throw new CommonException(CodeEnum.ERROR_NOT_LOGIN);
        Integer userId = usersVO.getUserId();

        if (null != equipmentIds){

            for (int equipmentId :
                    equipmentIds) {
                LambdaUpdateWrapper<Equipment> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(Equipment::getEquipmentId, equipmentId);
                updateWrapper.eq(Equipment::getEquipmentUserId, userId);
                if (isOpen) updateWrapper.set(Equipment::getEquipmentStatus, 0);
                else updateWrapper.set(Equipment::getEquipmentStatus, 1);
                updateWrapper.set(Equipment::getEquipmentUpdateTime, new Date());
                baseMapper.update(updateWrapper);
            }
        }
    }

    @Override
    public Page<Equipment> selectInPage(Integer pageNum, Integer equipmentNum, HttpServletRequest request) {

        UsersVO usersVO = (UsersVO) request.getSession().getAttribute(FinalValueUtil.USER_LOGIN_STATUS);
        if (null == usersVO) throw new CommonException(CodeEnum.ERROR_NOT_LOGIN);

        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Equipment::getEquipmentUserId, usersVO.getUserId());

        return baseMapper.selectPage(new Page<>(pageNum, equipmentNum), queryWrapper);
    }

    @Override
    public Page<Equipment> search(Integer pageNum, Integer equipmentNum, String searchStr, HttpServletRequest request) {

        UsersVO usersVO = (UsersVO) request.getSession().getAttribute(FinalValueUtil.USER_LOGIN_STATUS);
        if (null == usersVO) throw new CommonException(CodeEnum.ERROR_NOT_LOGIN);

        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Equipment::getEquipmentUserId, usersVO.getUserId());

        if ("开启".equals(searchStr)) queryWrapper.like(Equipment::getEquipmentStatus, "0").or();
        else if ("关闭".equals(searchStr)) queryWrapper.like(Equipment::getEquipmentStatus, "1").or();
        queryWrapper.like(Equipment::getEquipmentIp, searchStr)
                .or().like(Equipment::getEquipmentName, searchStr)
                .or().like(Equipment::getEquipmentPlantName, searchStr);

        return baseMapper.selectPage(new Page<>(pageNum, equipmentNum), queryWrapper);
    }

    @Override
    public EquipmentCountVO getCount(HttpServletRequest request) {

        UsersVO usersVO = (UsersVO) request.getSession().getAttribute(FinalValueUtil.USER_LOGIN_STATUS);
        if (null == usersVO) throw new CommonException(CodeEnum.ERROR_NOT_LOGIN);

        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Equipment::getEquipmentUserId, usersVO.getUserId());
        queryWrapper.select(Equipment::getEquipmentStatus);
        List<Equipment> equipment = baseMapper.selectList(queryWrapper);

        if (null == equipment) throw new CommonException(CodeEnum.ERROR_PARAM, "该用户无设备");
        EquipmentCountVO equipmentCountVO = new EquipmentCountVO();
        if (equipment.isEmpty()){
            equipmentCountVO.setEquipmentAllCount(0);
            equipmentCountVO.setEquipmentOpenCount(0);
        }else {
            int openCount = (int) equipment.stream().filter(equipment1 -> equipment1.getEquipmentStatus() == 1).count();
            equipmentCountVO.setEquipmentOpenCount(openCount);
            equipmentCountVO.setEquipmentAllCount(equipment.size());
        }

        return equipmentCountVO;
    }
}




