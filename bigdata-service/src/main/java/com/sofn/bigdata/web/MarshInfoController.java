package com.sofn.bigdata.web;

import com.sofn.bigdata.model.Result;
import com.sofn.bigdata.service.MarshInfoService;
import com.sofn.bigdata.utils.MarshJudgeUtil;
import com.sofn.bigdata.vo.MarshInfoForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

/**
 * 数据录入控制器
 */
@Api(tags = "农业大数据-沼气站点数据录入接口")
@RestController
@EnableSwagger2
@RequestMapping("/marshInfo")
public class MarshInfoController {

    @Autowired
    private MarshInfoService marshInfoService;

    @ApiOperation(value = "沼气站点数据统一录入接口")
    @ResponseBody
    @RequestMapping(value = "/marshInfos/toHdfs", method = RequestMethod.POST)
    public Result getDictTypeByName(
            @RequestBody(required = true) @ApiParam(name = "marshInfoForm", value = "当前站点下，在某个周期内的数据") MarshInfoForm marshInfoForm,
            BindingResult bindingResult) {
        Map<String, Object> result = new HashMap<>();
        if (bindingResult.hasErrors()) {
            return Result.error(500, bindingResult.getFieldError().getDefaultMessage());
        }

        try {
            // 1.过滤出有效数据
            List<String> toHiveList = new ArrayList<>();

            int invalidIndex = MarshJudgeUtil.checkAndFormatMarshInfos(marshInfoForm, toHiveList);

            // 2.所有数据有效对数据进行处理
            if (marshInfoForm.getMarshInfoList().size() == invalidIndex) {
                marshInfoService.handleData(toHiveList);
                return Result.ok("录入[ " + toHiveList.size() + " ]条有效数据");
            } else {
                return Result.error("ArrayIndex:" + invalidIndex + " ,the array element is invalid!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation(value = "数据共享接口模拟")
    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Object test() {
        Map<String, Object> result = new HashMap<>();
        List data = new ArrayList();
        result.put("data", data);

        String[] nameArr = {"李白","白居易","杜甫","苏轼","辛弃疾","李清照",
                "刘禹锡","王维","李商隐","纳兰性德","杜牧","陆游","陶渊明",
                "孟浩然","元稹","李煜","柳宗元","岑参","韩愈","王安石","欧阳修",
                "齐己","贾岛","韦应物","曹操","温庭筠","柳永","刘长卿","李贺","曹植",
                "王昌龄","张籍","孟郊","皎然","贯休","许浑","罗隐","杨万里","陆龟蒙",
                "张祜","王建","韦庄","诸葛亮","范仲淹","王勃","姚合","晏殊","屈原","卢纶",
                "杜荀鹤","岳飞","周邦彦","晏几道","钱起","韩偓版","皮日休","秦观","吴文英",
                "朱熹","高适","方干","马致远","李峤","权德舆","皇甫冉","左丘明","刘辰翁",
                "郑谷","黄庭坚","贺铸","赵长卿","张九龄","卓文君","戴叔伦司","马迁周","敦颐",
                "文天祥","张说","张炎","郑燮","程垓","朱敦儒","吴融","白朴","刘克庄","李端",
                "司空图","顾权况","张乔","马戴","吴潜","张孝祥","韩翃","郦道元","陈著","宋之问","贺知章"};
        String[] dynastyArr = {"唐","宋","元","明","清"};
        String[] poetryArr = new String[50];
        poetryArr[0] = "长风破浪会有时，直挂云帆济沧海";
        poetryArr[1] = "天长地久有时尽，此恨绵绵无绝期";
        poetryArr[2] = "感时花溅泪，恨别鸟惊心";
        poetryArr[3] = "明月几时有，把酒问青天";
        poetryArr[4] = "却道海棠依旧";
        for(int i = 0 ;i<50;i++){
            Map<String,Object> record = new HashMap<>();
            data.add(record);
            record.put("id", UUID.randomUUID().toString().replace("-",""));
            record.put("name", nameArr[i]);
            record.put("dynasty", dynastyArr[i%5]);
            if(i>4){
                record.put("poetry","两只老虎，两只老虎，跑得快");
            }else {
                record.put("poetry",poetryArr[i]);
            }
        }

        result.put("code", 200);
        result.put("msg", "成功请求");
        return result;
    }
}
