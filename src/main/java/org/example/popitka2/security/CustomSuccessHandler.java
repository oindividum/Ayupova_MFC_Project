//package org.example.popitka2.security;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//import java.io.IOException;
//@Component
//public class CustomSuccessHandler implements AuthenticationSuccessHandler {
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            response.sendRedirect("/admin/dashboard"); // ✅ ADMIN попадает в админ-панель
//        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MFC_EMPLOYEE"))) {
//            response.sendRedirect("/mfc/dashboard"); // ✅ Сотрудники МФЦ направляются сюда
//        } else {
//            response.sendRedirect("/user/dashboard"); // ✅ Обычные пользователи остаются на user_dashboard
//        }
//    }
//}
package org.example.popitka2.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/";
        String welcomeMessage = "";

        for (GrantedAuthority authority : authorities) {
            switch (authority.getAuthority()) {
                case "ROLE_ADMIN":
                    redirectUrl = "/admin/dashboard";
                    welcomeMessage = "Добро пожаловать, Администратор!";
                    break;
                case "ROLE_USER":
                    redirectUrl = "/user/dashboard";
                    welcomeMessage = "Добро пожаловать, Пользователь!";
                    break;
                case "ROLE_MFC_EMPLOYEE":
                    redirectUrl = "/mfc/dashboard";
                    welcomeMessage = "Добро пожаловать, Сотрудник МФЦ!";
                    break;
            }
        }

        request.getSession().setAttribute("welcomeMessage", welcomeMessage);
        response.sendRedirect(redirectUrl);
    }
}
