package com.tak.app_auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WithValidation {
    @GetMapping("/auth/test")
    @ResponseBody
    public String test(HttpServletRequest request, Model model) {
        String userId = (String) request.getAttribute("userId");
        System.out.println("userId = " + userId);
        return userId;
    }

}
