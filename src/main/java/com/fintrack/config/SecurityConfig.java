// // package com.fintrack.config;

// // import org.springframework.context.annotation.Bean;
// // import org.springframework.context.annotation.Configuration;
// // import org.springframework.security.config.Customizer;
// // import
// //
// org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// // import org.springframework.security.web.SecurityFilterChain;
// // import
// // org.springframework.security.config.annotation.web.builders.HttpSecurity;

// // @Configuration
// // @EnableWebSecurity
// // public class SecurityConfig {

// // @Bean
// // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
// {
// // http
// // .authorizeHttpRequests(auth -> auth
// // .requestMatchers("/", "/about", "/contact", "/signin", "/signup",
// // "/css/**", "/js/**", "/assets/**",
// // "/static/**")
// // .permitAll()
// // .anyRequest().authenticated())
// // .formLogin(login -> login
// // .loginPage("/signin")
// // .defaultSuccessUrl("/dashboard", true)
// // .failureUrl("/dashboard")
// // .permitAll())
// // .logout(logout -> logout
// // .logoutUrl("/logout")
// // .logoutSuccessUrl("/signin?logout")
// // .permitAll())
// // .csrf(csrf -> csrf.disable()); // optional for basic apps

// // return http.build();
// // }
// // }

// package com.fintrack.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// // import org.springframework.security.config.Customizer;
// import
// org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

// @Bean
// public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
// http
// .authorizeHttpRequests(auth -> auth
// .requestMatchers("/", "/about", "/contact", "/signin", "/signup",
// "/css/**", "/js/**", "/assets/**",
// "/static/**")
// .permitAll()
// .anyRequest().authenticated())
// .formLogin(login -> login
// .loginPage("/signin")
// .defaultSuccessUrl("/dashboard", true)
// .failureUrl("/signin")
// .permitAll())
// .logout(logout -> logout
// .logoutUrl("/logout")
// .logoutSuccessUrl("/signin?logout")
// .permitAll())
// .csrf(csrf -> csrf.disable()); // optional for basic apps

// return http.build();
// }
// }