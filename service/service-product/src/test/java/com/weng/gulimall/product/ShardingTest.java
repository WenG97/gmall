// package com.weng.gulimall.product;
//
// import com.weng.gulimall.model.product.BaseTrademark;
// import com.weng.gulimall.product.mapper.BaseTrademarkMapper;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
//
// @SpringBootTest
// public class ShardingTest {
//
//     @Autowired
//     private BaseTrademarkMapper baseTrademarkMapper;
//
//     @Test
//     public void testrw(){
//         BaseTrademark baseTrademark1 = baseTrademarkMapper.selectById(4L);
//         System.out.println(baseTrademark1);
//         BaseTrademark baseTrademark2 = baseTrademarkMapper.selectById(4L);
//         System.out.println(baseTrademark2);
//         BaseTrademark baseTrademark3 = baseTrademarkMapper.selectById(4L);
//         System.out.println(baseTrademark3);
//         BaseTrademark baseTrademark4 = baseTrademarkMapper.selectById(4L);
//         System.out.println(baseTrademark4);
//     }
// }
