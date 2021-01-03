package com.springcloud.controller;



import com.springcloud.entities.CommonResult;
import com.springcloud.entities.Payment;
import com.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import sun.plugin.com.DispatchClient;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Vetch
 * @Description
 * @create 2020-12-29-11:26
 */

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody  Payment payment) {
        int result = paymentService.create(payment);
        log.info("****插入结果： " + result);
        if (result > 0) {
            return new CommonResult(200, "插入已成功,serverPort="+serverPort, result);
        } else {
            return new CommonResult(444, "插入失败,serverPort="+serverPort, null);
        }
    }
    @GetMapping (value = "/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id")Long id) {
        Payment payment = paymentService.getPaymentById(id);
        log.info("****插入结果： " + payment);
        if (payment != null) {
            return new CommonResult(200, "查询成功,serverPort="+serverPort, payment);
        } else {
            return new CommonResult(444, "查询id=: "+id+"失败,serverPort="+serverPort, null);
        }
    }

    @GetMapping (value = "/payment/discovery")
    public Object discovery() {
        //获取所有注册的服务
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            System.out.println(service);
        }
        //获取服务名为CLOUD-PAYMENT-SERVICE的实例
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId()+"-"+instance.getHost()+"-"+instance.getPort()+"-"+instance.getUri());
        }
        return this.discoveryClient;
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB() {
        return serverPort;
    }

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            return serverPort;
        }
    }

    @GetMapping(value="/payment/zipkin")
    public String paymentZipkin() {
        return "hello,i am paymentZipkin server fallback,O(∩_∩)O哈哈~";
    }
}
