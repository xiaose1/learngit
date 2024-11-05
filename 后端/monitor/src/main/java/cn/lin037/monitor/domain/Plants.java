package cn.lin037.monitor.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName plants
 */
@TableName(value ="plants")
@Data
public class Plants implements Serializable {
    /**
     * 植物ID
     */
    @TableId(type = IdType.AUTO)
    private Integer plantId;

    /**
     * 植物名
     */
    private String plantName;

    /**
     * 植物图片uri
     */
    private String plantImageUri;

    /**
     * 植物信息
     */
    private String plantInformation;

    /**
     * 植物适应温度
     */
    private BigDecimal plantTemperature;

    /**
     * 植物适应湿度
     */
    private BigDecimal plantHumidity;

    /**
     * 植物创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date plantCreateTime;

    /**
     * 植物更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date plantUpdateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}