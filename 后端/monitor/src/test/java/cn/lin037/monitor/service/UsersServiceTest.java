package cn.lin037.monitor.service;
import java.util.Date;
import java.util.List;

import cn.lin037.monitor.domain.Users;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UsersServiceTest {

    public UsersService usersService;

    @Autowired
    public UsersServiceTest(UsersService usersService) {
        this.usersService = usersService;
    }

    @Test
    void testInsertUser() {

        Users users = new Users();
        users.setUserNickname("林叁柒");
        users.setUserUsername("lin037");
        users.setUserPassword("123456");
        users.setUserPhone("18476095076");
        users.setUserCreateTime(new Date());
        users.setUserUpdateTime(new Date());

        usersService.save(users);
    }

    @Test
    void testDeleteUser() {

        usersService.removeById(1);
    }

    @Test
    void testSelectUsers() {

        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        List<Users> list = usersService.list(usersQueryWrapper);
        System.out.println(list);
    }

    @Test
    void testRegister() {

        Users users = new Users();
        users.setUserNickname("叁柒");
        users.setUserUsername("123456");
        users.setUserPassword("123456");
        users.setUserPhone("18476095073");

        usersService.register(users);
    }

    @Test
    void testSelectInPage() {

        Page<Users> searched = usersService.selectInPage(1, 2);
        System.out.println(searched.getRecords());
    }

    @Test
    void testSearch() {

        Page<Users> searched = usersService.search(1, 10, "封禁");
        System.out.println(searched);
    }
}
