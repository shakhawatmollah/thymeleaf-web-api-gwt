package com.shakhawat.authapp.controller;

import com.shakhawat.authapp.service.SecurityUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("path", path);
                return "errors/404";
            }
            else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("username", SecurityUtils.getCurrentUserFullName());
                return "errors/403";
            }
            else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("error", "Internal Server Error");
                model.addAttribute("message", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));

                // Only include stack trace in development
                if (exception instanceof Exception) {
                    model.addAttribute("trace", ((Exception) exception).getMessage());
                }
                return "errors/500";
            }
        }

        return "errors/500";
    }
}
