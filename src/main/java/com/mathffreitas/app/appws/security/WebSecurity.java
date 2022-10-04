package com.mathffreitas.app.appws.security;

import com.mathffreitas.app.appws.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.SING_UP_URL)
                .permitAll()
                .antMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL)
                .permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_REQUEST_URL)
                .permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_URL)
                .permitAll()
                .antMatchers(SecurityConstants.H2_CONSOLE)
                .permitAll()
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
                .permitAll()
                .anyRequest().authenticated() //any other authentication need to be authenticated
                .and().addFilter(getAuthenticationFilter())
                .addFilter(new AuthorizationFilter(authenticationManager()))
                //.and().addFilter(new AuthenticationFilter(authenticationManager())) (/login) url snippet

                // fix the cookie issue (prevent authorization header from been cashed)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //line above is just for h2 usage!
        http.headers().frameOptions().disable();

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    // creating custom user authentication url (login)
    public AuthenticationFilter getAuthenticationFilter() throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl("/users/login");

        return filter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

        //configuration.setAllowedOrigins(Arrays.asList("http://localhost:8088", "http://localhost:4042"));
        //configuration.setAllowedOrigins(Arrays.asList("http://localhost:8088"));
        configuration.setAllowedOrigins(Arrays.asList("*")); //allow all
        //configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedMethods(Arrays.asList("*")); //allow all
        configuration.setAllowCredentials(true);
        //configuration.setAllowedHeaders(Arrays.asList("Authorization","Cache-Control","Content-Type"));
        configuration.setAllowedHeaders(Arrays.asList("*")); //allow all

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //source.registerCorsConfiguration("/users/login", configuration);
        source.registerCorsConfiguration("/**", configuration); //any path
        return source;
    }
}
