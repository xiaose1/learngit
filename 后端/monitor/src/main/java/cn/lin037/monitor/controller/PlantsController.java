package cn.lin037.monitor.controller;

import cn.lin037.monitor.domain.Plants;
import cn.lin037.monitor.domain.vo.ResultVO;
import cn.lin037.monitor.enums.CodeEnum;
import cn.lin037.monitor.exception.CommonException;
import cn.lin037.monitor.service.PlantsService;
import cn.lin037.monitor.utils.ResultUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/plants")
public class PlantsController {

    private final PlantsService plantsService;

    @Autowired
    public PlantsController(PlantsService plantsService) {
        this.plantsService = plantsService;
    }

    @PostMapping("/")
    public ResultVO insertPlants(@RequestBody Plants plant){

        plantsService.save(plant);
        return ResultUtil.success("新增成功");
    }

    @DeleteMapping("/")
    public ResultVO insertPlants(@RequestParam("plantId") List<Integer> plantIds){

        plantsService.removeBatchByIds(plantIds);
        return ResultUtil.success("删除成功");
    }

    @PutMapping("/")
    public ResultVO updatePlants(@RequestBody Plants plant){

        plant.setPlantUpdateTime(new Date());
        plantsService.updateById(plant);
        return ResultUtil.success("更新成功");
    }

    @GetMapping("/count")
    public ResultVO getCount(){

        return ResultUtil.success(plantsService.getCount());
    }

    @GetMapping("/{pageNum}/{plantNum}")
    public ResultVO getPlants(@PathVariable Integer pageNum, @PathVariable Integer plantNum){
        if (pageNum <= 0 || plantNum <= 0) throw new CommonException(CodeEnum.ERROR_PARAM, "请求页码和数据量需为正数");
        if (plantNum > 50) throw new CommonException(CodeEnum.ERROR_PARAM, "单次请求数据量不可超过50条");

        return ResultUtil.success(plantsService.page(new Page<>(pageNum, plantNum)));
    }

    @GetMapping("/{pageNum}/{plantNum}/{searchStr}")
    public ResultVO searchPlants(@PathVariable Integer pageNum, @PathVariable Integer plantNum, @PathVariable String searchStr){
        if (pageNum <= 0 || plantNum <= 0) throw new CommonException(CodeEnum.ERROR_PARAM, "请求页码和数据量需为正数");
        if (plantNum > 50) throw new CommonException(CodeEnum.ERROR_PARAM, "单次请求数据量不可超过50条");
        if (null == searchStr || searchStr.length() < 2) throw new CommonException(CodeEnum.ERROR_PARAM, "搜索内容不可少于2个字符");

        return ResultUtil.success(plantsService.search(pageNum, plantNum, searchStr));
    }

    @GetMapping("/search/{searchStr}")
    public ResultVO searchPlants(@PathVariable String searchStr){

        return ResultUtil.success(plantsService.search(searchStr));
    }

    @GetMapping("/get/{plantId}")
    public ResultVO searchPlants(@PathVariable Integer plantId){

        return ResultUtil.success(plantsService.getById(plantId));
    }
}
