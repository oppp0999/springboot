package com.example.firstboot

import org.slf4j.LoggerFactory
import org.springframework.aop.ThrowsAdvice
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class HelloController{
    val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/hello")
    fun hello(): String{
        logger.trace("trace line")
        logger.debug("debug")

        logger.info("info")
        logger.warn("info")
        logger.error("info")

        logger.info("INFO {} {}", 1, "abc")
        try{
            val x = 1/0
        }
        catch ( e:Throwable){
            logger.error("ERR failed to compute x", e)
        }



        return "hello"
    }
}