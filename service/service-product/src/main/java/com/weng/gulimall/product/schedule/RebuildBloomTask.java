package com.weng.gulimall.product.schedule;


import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.product.bloom.BloomOpsService;
import com.weng.gulimall.product.bloom.BloomQueryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RebuildBloomTask {

    @Autowired
    private BloomQueryDataService bloomQueryDataService;

    @Autowired
    private BloomOpsService bloomOpsService;

    @Scheduled(cron = "0 0 0 */7 * ?")
    public void rebuildTask(){
        bloomOpsService.rebuildBloom(SysRedisConst.BLOOM_SKUID,bloomQueryDataService);
    }
}
