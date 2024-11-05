package cn.lin037.monitor.controller;

import cn.lin037.monitor.domain.vo.ResultVO;
import cn.lin037.monitor.enums.CodeEnum;
import cn.lin037.monitor.exception.CommonException;
import cn.lin037.monitor.utils.ResultUtil;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/public")
public class CommonController {

    @PostMapping("/uploadPicture")
    public ResultVO uploadPicture(MultipartFile file){

        if (file.isEmpty()) throw new CommonException(CodeEnum.ERROR_NULL, "上传图片为空");
        //可以自己加一点校验 例如上传的是不是图片或者上传的文件是不是png格式等等 这里省略
        //获取原来的文件名和后缀
        String originalFilename = file.getOriginalFilename();
        if (null == originalFilename) throw new CommonException(CodeEnum.ERROR_NULL, "上传图片为空");
        String ext = "."+ originalFilename.split("\\.")[1];
        //生成一个新的文件名（以防有重复的名字存在导致被覆盖）
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String newName = uuid + ext;
        //拼接图片上传的路径 url+图片名
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        String pre = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() + "\\src\\main\\resources\\static\\images\\";
        String path = pre + newName;
        try {
            file.transferTo(new File(path));
        } catch (IOException e) {
            throw new CommonException(CodeEnum.ERROR_UNKNOWN, e.getMessage());
        }

        return ResultUtil.success((Object)("/images/" + newName));
    }
}
