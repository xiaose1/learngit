package cn.lin037.monitor.controller;

import cn.lin037.monitor.domain.Equipment;
import cn.lin037.monitor.domain.vo.ResultVO;
import cn.lin037.monitor.domain.vo.UsersVO;
import cn.lin037.monitor.enums.CodeEnum;
import cn.lin037.monitor.exception.CommonException;
import cn.lin037.monitor.service.EquipmentService;
import cn.lin037.monitor.utils.ResultUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static cn.lin037.monitor.utils.FinalValueUtil.USER_LOGIN_STATUS;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    /**
     * 新增用户设备接口
     */
    @PostMapping("/")
    public ResultVO insertEquipment(@RequestBody Equipment equipment, HttpServletRequest request){
        
        if (null == equipment || equipment.getEquipmentPlantName().isBlank()) throw new CommonException(CodeEnum.ERROR_NULL);
        if (equipment.getEquipmentName().isBlank()) equipment.setEquipmentName("未命名");

        UsersVO usersVO = (UsersVO) request.getSession().getAttribute(USER_LOGIN_STATUS);
        equipment.setEquipmentUserId(usersVO.getUserId());

        equipment.setEquipmentId(null);
        equipmentService.save(equipment);
        return ResultUtil.success("新增成功");
    }

    /**
     * 删除用户设备接口
     */
    @DeleteMapping("/{equipmentId}")
    public ResultVO deleteEquipment(@PathVariable Integer equipmentId, HttpServletRequest request){

        if (null == equipmentId) throw new CommonException(CodeEnum.ERROR_NULL);

        UsersVO usersVO = (UsersVO) request.getSession().getAttribute(USER_LOGIN_STATUS);
        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Equipment::getEquipmentId, equipmentId).eq(Equipment::getEquipmentUserId, usersVO.getUserId());
        equipmentService.remove(queryWrapper);
        return ResultUtil.success("删除成功");
    }

    /**
     * 修改用户设备接口
     */
    @PutMapping("/")
    public ResultVO updateEquipment(@RequestBody Equipment equipment, HttpServletRequest request){

        if (null == equipment || null == equipment.getEquipmentPlantName() || equipment.getEquipmentPlantName().isBlank()) throw new CommonException(CodeEnum.ERROR_NULL);
        if (equipment.getEquipmentStatus() < 0 || equipment.getEquipmentStatus() > 1) throw new CommonException(CodeEnum.ERROR_PARAM);
        if (equipment.getEquipmentName().isBlank()) equipment.setEquipmentName("未命名");

        UsersVO usersVO = (UsersVO) request.getSession().getAttribute(USER_LOGIN_STATUS);
        LambdaUpdateWrapper<Equipment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Equipment::getEquipmentId, equipment.getEquipmentId()).eq(Equipment::getEquipmentUserId, usersVO.getUserId());

        equipment.setEquipmentUpdateTime(new Date());
        equipmentService.update(equipment, updateWrapper);

        return ResultUtil.success("修改成功");
    }

    /**
     * 开启or关闭设备接口
     */
    @PutMapping("/{isOpen}")
    public ResultVO updateStatus(@RequestBody List<Integer> equipmentIds, @PathVariable Boolean isOpen, HttpServletRequest request){

        if (null == equipmentIds || equipmentIds.isEmpty()) throw new CommonException(CodeEnum.ERROR_NULL);
        if (equipmentIds.size() > 50) throw new CommonException(CodeEnum.ERROR_PARAM, "单次操作数据量不可超过50条");

        equipmentService.switchBatchByIds(equipmentIds, isOpen, request);
        if (isOpen) return ResultUtil.success("开启成功");
        else return ResultUtil.success("关闭成功");
    }

    @GetMapping("/count")
    public ResultVO getCount(HttpServletRequest request){

        return ResultUtil.success(equipmentService.getCount(request));
    }

    /**
     * 分页查询用户接口
     */
    @GetMapping("/{pageNum}/{equipmentNum}")
    public ResultVO getEquipments(@PathVariable Integer pageNum, @PathVariable Integer equipmentNum, HttpServletRequest request){

        if (pageNum <= 0 || equipmentNum <= 0) throw new CommonException(CodeEnum.ERROR_PARAM, "请求页码和数据量需为正数");
        if (equipmentNum > 50) throw new CommonException(CodeEnum.ERROR_PARAM, "单次请求数据量不可超过50条");

        Page<Equipment> equipmentList = equipmentService.selectInPage(pageNum, equipmentNum, request);
        return ResultUtil.success(equipmentList);
    }
    /**
     * 分页搜索查询用户接口
     */
    @GetMapping("/{pageNum}/{equipmentNum}/{searchStr}")
    public ResultVO getEquipments(@PathVariable Integer pageNum, @PathVariable Integer equipmentNum, @PathVariable String searchStr, HttpServletRequest request){

        if (pageNum <= 0 || equipmentNum <= 0) throw new CommonException(CodeEnum.ERROR_PARAM, "请求页码和数据量需为正数");
        if (equipmentNum > 50) throw new CommonException(CodeEnum.ERROR_PARAM, "单次请求数据量不可超过50条");
        if (null == searchStr || searchStr.length() < 2) throw new CommonException(CodeEnum.ERROR_PARAM, "搜索内容不可少于2个字符");

        Page<Equipment> equipmentList = equipmentService.search(pageNum, equipmentNum, searchStr, request);
        return ResultUtil.success(equipmentList);
    }

}
