package com.casestudy5.config;
import com.casestudy5.config.jwt.CustomAccessDeniedHandler;
//import com.casestudy5.config.jwt.CustomOAuth2LoginSuccessHandler;
import com.casestudy5.config.jwt.JWTTokenHelper;
import com.casestudy5.config.jwt.JwtAuthenticationTokenFilter;
import com.casestudy5.config.jwt.RestAuthenticationEntryPoint;
import com.casestudy5.model.entity.user.AuthProvider;
import com.casestudy5.model.entity.user.Role;
import com.casestudy5.model.entity.user.RoleName;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.repo.IUserRepository;
import com.casestudy5.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public RestAuthenticationEntryPoint restServicesEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }


    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new DefaultOAuth2UserService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService); // Inject your UserDetailsService
        provider.setPasswordEncoder(passwordEncoder); // Use BCryptPasswordEncoder
        return provider;
    }


    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return userRequest -> {
            OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

            String email = oAuth2User.getAttribute("email");
            if (email == null) {
                throw new RuntimeException("Email not found in OAuth2User attributes!");
            }

            // Check if the user exists
            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                // Create a new user if not found
                user = new User();
                user.setEmail(email);
                user.setAuthProvider(AuthProvider.GOOGLE); // Set auth provider based on OAuth2
                user.setEnabled(true); // Enable user on successful OAuth2 login
                user.setRoles(Set.of(new Role(RoleName.ROLE_USER))); // Assign roles
                userRepository.save(user);
            } else {
                // Update existing user
                user.setAuthProvider(AuthProvider.GOOGLE); // Set the correct auth provider
                user.setEnabled(true); // Make sure user is enabled
                userRepository.save(user);
            }

            // Generate JWT token
            String token = jwtTokenHelper.generateToken(email);

            // You can store the token and return it in the response to the frontend
            return new DefaultOAuth2User(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), "sub");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors(httpSecurityCorsConfigurer -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Arrays.asList("*"));
                    configuration.setAllowedMethods(Arrays.asList("*"));
                    configuration.setAllowedHeaders(Arrays.asList("*"));
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", configuration);
                    httpSecurityCorsConfigurer.configurationSource(source);
                }).csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                        .requestMatchers("/api/auth/register","/api/auth/login", "/oauth2/success", "/login/oauth2/code/google", "/oauth2/authorize/google").permitAll()
                        .requestMatchers("/api/users/reset", "/api/users/send-reset-link", "/api/users/reset-password").permitAll()
                        .requestMatchers("/api/users/request-seller-role").hasAnyAuthority("ROLE_USER")
                        .requestMatchers("/api/products/all", "/api/products/seller/{userId}", "api/products/view/**").permitAll()
                        .requestMatchers("/api/order-items/**","/api/orders/**").permitAll()
                        .requestMatchers("/api/categories/**").permitAll()
                        .requestMatchers("/api/payments/**").permitAll()
                        .requestMatchers("/api/role").permitAll()
                        .requestMatchers("/api/reviews/product-check/**").permitAll()
                        .requestMatchers("/api/admin/**","/api/users/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/users/**","/api/transactions/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers("/api/products/seller", "/api/products/**","/api/orders/pending","/api/orders/seller").hasAnyAuthority("ROLE_SELLER")
                        .requestMatchers("/api/cart/**","/api/reviews/product/**").hasAnyAuthority("ROLE_USER")
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint ->
                                endpoint.baseUri("/oauth2/authorize/google"))
                        .redirectionEndpoint(endpoint ->
                                endpoint.baseUri("/login/oauth2/code/google"))
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(oAuth2UserService())) // OAuth2UserService to fetch user info
                        .successHandler((request, response, authentication) -> {
                            Object principal = authentication.getPrincipal();

                            if (principal instanceof OAuth2User) {
                                OAuth2User oAuth2User = (OAuth2User) principal;
                                String email = oAuth2User.getAttribute("email");
                                if (email == null) {
                                    throw new RuntimeException("Email not found in OAuth2User attributes!");
                                }
                                // You can generate a JWT token for the OAuth2 authenticated user
                                String token = jwtTokenHelper.generateToken(email);
                                response.sendRedirect("http://localhost:3000/oauth2/callback?token=" + token);
                            } else if (principal instanceof UserDetails) {
                                UserDetails userDetails = (UserDetails) principal;
                                // Handle traditional authentication flow
                                String username = userDetails.getUsername();
                                String token = jwtTokenHelper.generateToken(username);
                                response.sendRedirect("http://localhost:3000/oauth2/callback?token=" + token);
                            }
                        })
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                )

                .exceptionHandling(customizer -> customizer.accessDeniedHandler(customAccessDeniedHandler()))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}