package com.sofn.bigdata.web;

import com.sofn.common.web.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "决策模板管理" )
@RestController
@RequestMapping("/decisionTemplate")
public class DecisionTemplateController extends BaseController {

}
