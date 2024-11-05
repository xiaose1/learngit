package cn.lin037.monitor.mapper;

import cn.lin037.monitor.domain.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author 18333
* @description 针对表【users】的数据库操作Mapper
* @createDate 2024-05-27 21:01:51
* @Entity generator.domain.Users
*/
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

}




