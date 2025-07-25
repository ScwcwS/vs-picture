package com.vs.vspicturebackend.model.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * 图片标签分类列表视图
 */
@Data
public class PictureTagCategory {

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 分类列表
     */
    private HashMap<String,List<String>> categoryList;
}
