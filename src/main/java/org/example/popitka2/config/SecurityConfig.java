package org.example.popitka2.config;

import org.example.popitka2.security.CustomSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.example.popitka2.service.MyUserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/", "/auth/**", "/css/**", "/js/**", "/images/**").permitAll() // ✅ Разрешаем доступ к статическим ресурсам
                            .requestMatchers("/mfc/**").hasRole("MFC_EMPLOYEE")
                            // 🔹 Доступ для обычных пользователей
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/admin/users", "/admin/developmend").hasRole("ADMIN") // ✅ Доступ только для админов

                            .requestMatchers("/admin/analytics").hasRole("ADMIN")
                            .requestMatchers( "/admin/dashboard/author").hasRole("ADMIN")
                            .requestMatchers("/moskvich_card/request", "/moskvich_card/status").hasRole("USER")
                            .requestMatchers("/user/dashboard", "/user/profile").hasRole("USER")
                            .requestMatchers("/user/moskvich_card").hasAnyRole("USER", "ADMIN")
                            .requestMatchers(HttpMethod.POST, "/user/update_profile", "/user/cancel_appointment").hasRole("USER")
                            .requestMatchers("/user/profile").hasRole("USER")
                            .requestMatchers("/user/**").hasRole("USER")

                            .requestMatchers("/moskvich_card/**").hasAnyRole("MFC_EMPLOYEE", "USER") // или другие роли
                            // 🔹 Доступ для сотрудников МФЦ
                            .requestMatchers("/mfc/moskvich_card/update_status").hasRole("MFC_EMPLOYEE")

                            .requestMatchers("/mfc/moskvich_card").hasRole("MFC_EMPLOYEE")
                            .requestMatchers(HttpMethod.GET, "/mfc/moskvich_card/edit/**").hasRole("MFC_EMPLOYEE") // ✅ Доступ к редактированию
                            .requestMatchers(HttpMethod.POST, "/mfc/moskvich_card/update_status").hasRole("MFC_EMPLOYEE") // ✅ Разрешаем POST-запросы
                            .requestMatchers(HttpMethod.PUT, "/mfc/moskvich_card/update_status").hasRole("MFC_EMPLOYEE") // ✅ Разрешаем PUT-запросы
                            .requestMatchers(HttpMethod.PATCH, "/mfc/moskvich_card/update_status").hasRole("MFC_EMPLOYEE") // ✅ Разрешаем PATCH-запросы
                            .requestMatchers("/mfc/moskvich_card/update_status").hasRole("MFC_EMPLOYEE") // ✅ Разрешаем маршрут

                            .requestMatchers("/admin/**").hasRole("ADMIN") // 🔥 Теперь доступ ко всем страницам `/admin/...`

                        .requestMatchers("/director/**").hasRole("DIRECTOR")

                        // 🔹 Доступ к страницам МФЦ
                        .requestMatchers("/mfc/**").hasRole("MFC_EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/mfc/appointments/book").hasRole("MFC_EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/mfc/appointments/update").hasRole("MFC_EMPLOYEE")

                        // 🔹 Разрешаем доступ ко страницам "в разработке"
                        .requestMatchers("/mfc/reports", "/mfc/support", "/mfc/settings").permitAll()
                        .requestMatchers("/admin/development").permitAll()

                        // 🔹 Доступ к аутентификации
                        .requestMatchers("/auth/login", "/auth/register").permitAll()

                        // 🔹 Разрешаем выход
                        .requestMatchers("/logout").permitAll()

                )

                .formLogin(login -> login
                        .loginPage("/auth/login")
                        .successHandler(authenticationSuccessHandler()) // ✅ Кастомный обработчик входа
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 🔥 Теперь соответствует Thymeleaf
                        .logoutSuccessUrl("/") // ✅ После выхода перенаправляем на главную страницу
                        .permitAll()

                );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(MyUserDetailsService myUserDetailsService) {
        return myUserDetailsService;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSuccessHandler();
    }


}

