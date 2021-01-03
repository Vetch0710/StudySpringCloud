package com.springcloud.controller;

import com.springcloud.entities.CommonResult;
import com.springcloud.entities.Payment;
import com.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB() {
        return serverPort;
    }
}
