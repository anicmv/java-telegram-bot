package com.github.anicmv.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * @author anicmv
 * @date 2025/3/30 18:48
 * @description TODO
 */
@Data
@Builder
@TableName("fadian")
public class FaDian {
    private Integer id;
    private String content;
}
