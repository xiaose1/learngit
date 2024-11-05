package cn.lin037.monitor.domain.vo;

import cn.lin037.monitor.domain.Users;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class UsersVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 用户名
     */
    private String userUsername;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户状态（0为正常，1为封禁）
     */
    private Integer userStatus;

    /**
     * 用户角色（0为普通用户，1为管理员）
     */
    private Integer userRole;

    /**
     * 用户创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date userCreateTime;

    /**
     * 用户更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date userUpdateTime;

    public UsersVO(Users users) {
        this.userId = users.getUserId();
        this.userNickname = users.getUserNickname();
        this.userUsername = users.getUserUsername();
        this.userPhone = users.getUserPhone();
        this.userStatus = users.getUserStatus();
        this.userRole = users.getUserRole();
        this.userCreateTime = users.getUserCreateTime();
        this.userUpdateTime = users.getUserUpdateTime();
    }

    public UsersVO(Integer userId, String userNickname, String userUsername, String userPhone, Integer userStatus, Integer userRole, Date userCreateTime, Date userUpdateTime) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.userUsername = userUsername;
        this.userPhone = userPhone;
        this.userStatus = userStatus;
        this.userRole = userRole;
        this.userCreateTime = userCreateTime;
        this.userUpdateTime = userUpdateTime;
    }
}
