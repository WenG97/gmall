package com.weng.gulimall.feign.ware;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


//url:指定请求的绝对路径
@FeignClient(name = "service-ware",url = "${app.ware-url:http://localhost:14001/}")
public interface WareFeignClient {


    @GetMapping("/hasStock")
    String hasStock(@RequestParam("skuId") Long skuId,
                  @RequestParam("num") Integer num);
}
