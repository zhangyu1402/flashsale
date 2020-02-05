package com.zhangyu.seckill.controller;


import com.zhangyu.seckill.dto.Response;
import com.zhangyu.seckill.dto.SeckillResult;
import com.zhangyu.seckill.enums.SeckillStateEnum;
import com.zhangyu.seckill.exception.SeckillException;
import com.zhangyu.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    SeckillService seckillService;


    @RequestMapping(value = "/order/{seckillId}/{phone}/{md5}",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Response<SeckillResult> execute(@PathVariable("seckillId") Long seckillId,
                                                 @PathVariable("phone") Long phone,
                                                 @PathVariable("md5") String md5) throws SeckillException {
        //springmvc valid
        if (phone == null) {
            SeckillResult result = new SeckillResult(SeckillStateEnum.NO_LOGIN);
            return new Response<>(false,"phone missing");
        }
        SeckillResult seckillResult = seckillService.executeSeckill(seckillId,phone, md5);
        return new Response<>(true, seckillResult);
    }

    @RequestMapping(value = "/test",
            method = RequestMethod.POST)
    @ResponseBody
    public String  test() throws SeckillException {
        //springmvc valid
        return "success";
    }

}
