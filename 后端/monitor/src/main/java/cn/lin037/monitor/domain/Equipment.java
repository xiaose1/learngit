package cn.lin037.monitor.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName equipment
 */
@TableName(value ="equipment")
@Data
public class Equipment implements Serializable {
    /**
     * 设备ID
     */
    @TableId(type = IdType.AUTO)
    private Integer equipmentId;

    /**
     * 设备名
     */
    private String equipmentName;

    /**
     * 设备状态 0为关闭 1为开启
     */
    private Integer equipmentStatus;

    /**
     * 设备IP地址
     */
    private String equipmentIp;

    /**
     * 设备监听的植物名
     */
    private String equipmentPlantName;

    /**
     * 设备监听的植物ID
     */
    private Integer equipmentPlantId;

    /**
     * 设备所属用户ID
     */
    private Integer equipmentUserId;

    /**
     * 设备创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date equipmentCreateTime;

    /**
     * 设备更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date equipmentUpdateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}