package cn.lin037.monitor.service.impl;

import cn.lin037.monitor.domain.vo.UsersVO;
import cn.lin037.monitor.enums.CodeEnum;
import cn.lin037.monitor.exception.CommonException;
import cn.lin037.monitor.service.UsersService;
import cn.lin037.monitor.utils.MD5Util;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lin037.monitor.domain.Users;
import cn.lin037.monitor.mapper.UsersMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static cn.lin037.monitor.utils.FinalValueUtil.USER_LOGIN_STATUS;

/**
* @author 18333
* @description 针对表【users】的数据库操作Service实现
* @createDate 2024-05-27 21:01:51
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService {

    @Override
    public UsersVO userLogin(Users users, HttpServletRequest request) {

        //查询是否存在用户
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUserUsername, users.getUserUsername());
        Users selectedUser = baseMapper.selectOne(queryWrapper);

        //判断用户是否存在以及用户密码是否正确
        if (null == selectedUser
                || !MD5Util.compareMD5(users.getUserPassword(), selectedUser.getUserPassword())){
            throw new CommonException(CodeEnum.ERROR_PARAM, "账号或密码错误");
        }

        //信息脱敏
        UsersVO safetyUser = new UsersVO(selectedUser);
        request.getSession().setAttribute(USER_LOGIN_STATUS, safetyUser);

        return safetyUser;
    }

    @Override
    public void logout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATUS);
    }

    @Override
    public UsersVO register(Users users) {

        //查询是否存在用户
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUserUsername, users.getUserUsername());
        List<Users> list = baseMapper.selectList(queryWrapper);

        //判断该用户是否已存在
        if (null != list && !list.isEmpty()) throw new CommonException(CodeEnum.ERROR_PARAM, "该用户已存在");

        //如果不存在则保存新用户数据
        users.setUserPassword(MD5Util.transformMD5(users.getUserPassword()));
        baseMapper.insert(users);
        return new UsersVO(users);
    }

    @Override
    public UsersVO getSelfMsg(HttpServletRequest request) {

        UsersVO usersVO = (UsersVO) request.getSession().getAttribute(USER_LOGIN_STATUS);
        if (null == usersVO) throw new CommonException(CodeEnum.ERROR_UNAUTHORIZED, "用户未登录");
        return usersVO;
    }

    @Override
    public UsersVO updateSelf(Users users, HttpServletRequest request) {

        //判断是否登录
        UsersVO usersVO = (UsersVO) request.getSession().getAttribute(USER_LOGIN_STATUS);
        if (null == usersVO || null == usersVO.getUserId()) throw new CommonException(CodeEnum.ERROR_UNAUTHORIZED, "用户未登录");

        //更新数据库
        LambdaUpdateWrapper<Users> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Users::getUserId, usersVO.getUserId());
        if (users.getUserNickname() != null && !users.getUserNickname().isBlank()) updateWrapper.set(Users::getUserNickname, users.getUserNickname());
        if (users.getUserPhone() != null && !users.getUserPhone().isBlank()) updateWrapper.set(Users::getUserPhone, users.getUserPhone());
        updateWrapper.set(Users::getUserUpdateTime, new Date());
        if (users.getUserPassword() != null && !users.getUserPassword().isBlank()) updateWrapper.set(Users::getUserPassword, MD5Util.transformMD5(users.getUserPassword()));
        baseMapper.update(updateWrapper);

        //更新Session中的登录信息
        UsersVO newUsersVO = new UsersVO(users);
        newUsersVO.setUserId(usersVO.getUserId());
        request.getSession().setAttribute(USER_LOGIN_STATUS, newUsersVO);
        return newUsersVO;
    }

    @Override
    public Page<Users> selectInPage(Integer pageNum, Integer usersNum) {

        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Users.class, tableFieldInfo -> !tableFieldInfo.getColumn().equals("user_password"));
        return baseMapper.selectPage(new Page<>(pageNum, usersNum), queryWrapper);
    }

    @Override
    public Page<Users> search(Integer pageNum, Integer usersNum, String searchStr) {

        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Users.class, tableFieldInfo -> !tableFieldInfo.getColumn().equals("user_password"))
                .like(Users::getUserNickname, searchStr)
                .or().like(Users::getUserUsername, searchStr)
                .or().like(Users::getUserPhone, searchStr);
        if ("封禁".equals(searchStr))
            queryWrapper.or().like(Users::getUserStatus, "0");
        if ("正常".equals(searchStr))
            queryWrapper.or().like(Users::getUserStatus, "1");
        return baseMapper.selectPage(new Page<>(pageNum, usersNum), queryWrapper);
    }

    @Override
    @Transactional
    public void banBatchByIds(List<Integer> userIds) {

        if (null != userIds){


            for (int id :
                    userIds) {

                LambdaUpdateWrapper<Users> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(Users::getUserId, id);
                updateWrapper.set(Users::getUserStatus, 1);
                updateWrapper.set(Users::getUserUpdateTime, new Date());

                baseMapper.update(updateWrapper);
            }
        }
    }

}




