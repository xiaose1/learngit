package cn.lin037.monitor.controller;

import cn.lin037.monitor.domain.Users;
import cn.lin037.monitor.domain.vo.ResultVO;
import cn.lin037.monitor.domain.vo.UsersVO;
import cn.lin037.monitor.enums.CodeEnum;
import cn.lin037.monitor.exception.CommonException;
import cn.lin037.monitor.service.UsersService;
import cn.lin037.monitor.utils.FinalValueUtil;
import cn.lin037.monitor.utils.MD5Util;
import cn.lin037.monitor.utils.ResultUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public ResultVO login(@RequestBody Users users, HttpServletRequest request){

        if (null == users
                || null == users.getUserUsername()
                || null == users.getUserPassword()
                || users.getUserUsername().isBlank()
                || users.getUserPassword().isBlank()) {
            throw new CommonException(CodeEnum.ERROR_NULL, "账号和密码不可为空");
        }

        UsersVO usersVO = usersService.userLogin(users, request);
        return ResultUtil.success(usersVO);
    }

    /**
     * 用户注销接口
     */
    @DeleteMapping("/logout")
    public ResultVO logout(HttpServletRequest request){

        usersService.logout(request);
        return ResultUtil.success("注销成功");
    }

    /**
     * 用户查询个人信息接口
     */
    @GetMapping("/selfMsg")
    public ResultVO getSelfMsg(HttpServletRequest request){

        UsersVO selfMsg = usersService.getSelfMsg(request);

        return ResultUtil.success(selfMsg);
    }

    /**
     * 用户更改个人信息接口
     */
    @PutMapping("/updateSelf")
    public ResultVO updateSelf(@RequestBody Users users, HttpServletRequest request){

        if (null == users) throw new CommonException(CodeEnum.ERROR_NULL);
        if (null == users.getUserNickname() || null == users.getUserPhone()
                || users.getUserNickname().isBlank() || users.getUserPhone().isBlank()){
            throw new CommonException(CodeEnum.ERROR_NULL,"目标值不可为空");
        }

        UsersVO usersVO = usersService.updateSelf(users, request);
        return ResultUtil.success("修改成功");
    }

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public ResultVO register(@RequestBody Users users){

        if (null == users) throw new CommonException(CodeEnum.ERROR_NULL);
        if (null == users.getUserUsername() || users.getUserUsername().isBlank()) throw new CommonException(CodeEnum.ERROR_NULL, "用户名不可为空");
        if (null == users.getUserPhone() || users.getUserPhone().isBlank()) throw new CommonException(CodeEnum.ERROR_NULL, "手机号不可为空");
        if (null == users.getUserPassword() || users.getUserPassword().isBlank()) throw new CommonException(CodeEnum.ERROR_NULL, "用户密码不可为空");
        if (null == users.getUserStatus() || users.getUserStatus() < 0 || users.getUserStatus() > 1) throw new CommonException(CodeEnum.ERROR_PARAM, "用户状态错误");

        usersService.register(users);
        return ResultUtil.success("注册成功");
    }

    /**
     * 删除用户接口
     */
    @DeleteMapping("/{userIds}")
    public ResultVO deleteById(@PathVariable Integer userIds){

//        if (null == userIds || userIds.isEmpty()) throw new CommonException(CodeEnum.ERROR_NULL);
//        if (userIds.size() > 50) throw new CommonException(CodeEnum.ERROR_PARAM, "单次操作数据量不可超过50条");
        usersService.removeById(userIds);
        return ResultUtil.success("删除成功");
    }

    /**
     * 修改用户信息接口
     */
    @PutMapping("/")
    public ResultVO updateUsers(@RequestBody Users users){

        if (null == users) throw new CommonException(CodeEnum.ERROR_NULL);
//        System.out.println(users.getUserPassword());
        if (null != users.getUserPassword() && !users.getUserPassword().isBlank()) users.setUserPassword(MD5Util.transformMD5(users.getUserPassword()));
        users.setUserUpdateTime(new Date());
        users.setUserUsername(null);
        usersService.updateById(users);
        return ResultUtil.success("更新成功");
    }

    /**
     * 封禁用户接口
     */
    @PutMapping("/ban")
    public ResultVO updateUsersStatus(@RequestBody List<Integer> userIds){

        if (null == userIds || userIds.isEmpty()) throw new CommonException(CodeEnum.ERROR_NULL);
        if (userIds.size() > 50) throw new CommonException(CodeEnum.ERROR_PARAM, "单次操作数据量不可超过50条");

        usersService.banBatchByIds(userIds);
        return ResultUtil.success("封禁成功");
    }

    /**
     * 分页查询用户接口
     */
    @GetMapping("/{pageNum}/{usersNum}")
    public ResultVO getUsers(@PathVariable Integer pageNum, @PathVariable Integer usersNum){

        if (pageNum <= 0 || usersNum <= 0) throw new CommonException(CodeEnum.ERROR_PARAM, "请求页码和数据量需为正数");
        if (usersNum > 50) throw new CommonException(CodeEnum.ERROR_PARAM, "单次请求数据量不可超过50条");
        Page<Users> usersList = usersService.selectInPage(pageNum, usersNum);
        return ResultUtil.success(usersList);
    }

    /**
     * 分页搜索查询用户接口
     */
    @GetMapping("/{pageNum}/{usersNum}/{searchStr}")
    public ResultVO getUsersBySearch(@PathVariable Integer pageNum, @PathVariable Integer usersNum, @PathVariable String searchStr){

        if (pageNum <= 0 || usersNum <= 0) throw new CommonException(CodeEnum.ERROR_PARAM, "请求页码和数据量需为正数");
        if (usersNum > 50) throw new CommonException(CodeEnum.ERROR_PARAM, "单次请求数据量不可超过50条");
        if (null == searchStr || searchStr.length() < 2) throw new CommonException(CodeEnum.ERROR_PARAM, "搜索内容不可少于2个字符");

        Page<Users> searched = usersService.search(pageNum, usersNum, searchStr);
        return ResultUtil.success(searched);
    }

    @GetMapping("/getAuthority")
    public ResultVO getUsersBySearch(HttpServletRequest request){

        UsersVO usersVO = (UsersVO) request.getSession().getAttribute(FinalValueUtil.USER_LOGIN_STATUS);
        if (null != usersVO) return ResultUtil.success(usersVO.getUserRole());
        return ResultUtil.success(0);
    }
}
