package com.abin.app.micro_app.model.response;

import com.abin.app.micro_app.model.CreateAIWorkResultVO;
import lombok.Data;

import java.util.List;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/13
 * @Desc:
 */
@Data
public class GetCreateProgressResponse {

    private List<CreateAIWorkResultVO> voList;
}
