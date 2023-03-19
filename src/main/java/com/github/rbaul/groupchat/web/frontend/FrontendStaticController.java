package com.github.rbaul.groupchat.web.frontend;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.HandlerMapping;

@Controller
public class FrontendStaticController {
    @GetMapping({"/", "/login", "/room-chats", "/room-chat"})
    public String redirectToUI(HttpServletRequest request) {
		String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		return "redirect:/index.html?returnUrl=" + fullPath;
    }
}