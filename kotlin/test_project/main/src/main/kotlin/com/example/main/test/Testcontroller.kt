package com.example.main.test

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class TestController {

    @RequestMapping("/test")
    fun test(model: Model): String {
        model["title"] = "testTitle" //model.addAttribute("title", "test")
        return "index"
    }

}