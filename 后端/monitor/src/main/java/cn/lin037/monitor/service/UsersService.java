package cn.lin037.monitor.service;

import cn.lin037.monitor.domain.Users;
import cn.lin037.monitor.domain.vo.UsersVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author 18333
* @description 针对表【users】的数据库操作Service
* @createDate 2024-05-27 21:01:51
*/
public interface UsersService extends IService<Users> {

    UsersVO userLogin(Users users, HttpServletRequest request);

    void logout(HttpServletRequest request);

    UsersVO register(Users users);

    UsersVO getSelfMsg(HttpServletRequest request);

    UsersVO updateSelf(Users users, HttpServletRequest request);

    Page<Users> selectInPage(Integer pageNum, Integer usersNum);

    Page<Users> search(Integer pageNum, Integer usersNum, String searchStr);

    void banBatchByIds(List<Integer> userIds);
}
