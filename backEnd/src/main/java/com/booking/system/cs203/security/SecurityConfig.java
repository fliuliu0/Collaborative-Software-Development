package com.booking.system.cs203.security;

import com.booking.system.cs203.User.CustomUserDetailsService;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomUserDetailsService customuserDetailsService;

    private UserDetailsService userDetailsService;

    @Value("${jwt.key}")
    private String jwtKey;
    public SecurityConfig(UserDetailsService userSvc, CustomUserDetailsService customuserDetailsService) {
        System.out.println("QQQ: GOOFFFY GOBLIN trying to start SecuirtyConfig");
        this.userDetailsService = userSvc;
        this.customuserDetailsService = customuserDetailsService;
    }
//    public SecurityConfig(){
//        this.userDetailsService = users();
//    }

//    @Bean
//    public UserDetailsService users(){
//        System.out.println("ahrohaisodhai" );
//        return new InMemoryUserDetailsManager(
//                User.withUsername("belbel@gmail.com")
//                        .password("{noop}belbel")
//                        .authorities("READ","ROLE_USER")
//                        .build()
//        );
//    }

//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder(){
//
//        return new BCryptPasswordEncoder();
//    }
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
//@Bean
//public PasswordEncoder encoder() {
//    // auto-generate a random salt internally
//    //String idForEncode = "bcrypt";
//    //encoder.put(idForEncode, new BCryptPasswordEncoder());
//    return new BCryptPasswordEncoder();
//    //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//}
//@Bean(name="myPasswordEncoder")
//public PasswordEncoder getPasswordEncoder() {
//    DelegatingPasswordEncoder delPasswordEncoder=  (DelegatingPasswordEncoder)PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    BCryptPasswordEncoder bcryptPasswordEncoder =new BCryptPasswordEncoder();
//    delPasswordEncoder.setDefaultPasswordEncoderForMatches(bcryptPasswordEncoder);
//    return delPasswordEncoder;
//}
//    @Bean
//    @Autowired
//    public DaoAuthenticationProvider getDaoAuthenticationProvider(@Qualifier("myPasswordEncoder") PasswordEncoder passwordEncoder, UserDetailsService userDetailsServiceJDBC) {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
//        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceJDBC);
//        return daoAuthenticationProvider;
//    }
//@Autowired
//public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//
//    auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder());
//
//}
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        System.out.println("QQQ: GOOFFY GOBLIN, hihihihih");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService){
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(authProvider);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"Access denied: " + accessDeniedException.getMessage() + "\"}");
        };
    }
//
//


//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    JwtEncoder jwtEncoder(){
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes()));
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        byte[] bytes = jwtKey.getBytes();
        //SecretKeySpec originalKey = new SecretKeySpec(bytes, 0, bytes.length, "RSA");
        SecretKeySpec originalKey = new SecretKeySpec(bytes, "HMAC");
        return NimbusJwtDecoder.withSecretKey(originalKey).macAlgorithm(MacAlgorithm.HS512).build();
    }

    public JwtAuthenticationConverter customJwtAuthenticationConverter(){
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new CustomJwtGrantedAuthoritiesConverter());
        return converter;
    }




    @Bean
    @Order(1)
    SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        System.out.println("QQQ: GOOFFY GOBLIN");
    return http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            //.csrf(AbstractHttpConfigurer::disable)
            //.csrf(csrf -> csrf.ignoringRequestMatchers("h2-console/**"))
            .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/sign-up").permitAll()
                            .requestMatchers("/users").permitAll()
                            .requestMatchers("/api/auth/token").permitAll()

                            .requestMatchers(HttpMethod.POST, "/users").permitAll()
                            .requestMatchers(HttpMethod.GET, "/users/all").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/users/email/*").hasAnyRole("ADMIN","USER")
                            .requestMatchers(HttpMethod.PUT, "/users/update/*").hasAnyRole("ADMIN","USER")
//                            .requestMatchers(HttpMethod.POST, "/memberships").permitAll()
                            .requestMatchers(HttpMethod.DELETE, "/users/email/*").hasAnyRole("ADMIN","USER")
                            .requestMatchers(HttpMethod.PUT, "/users/update-membership/**").hasAnyRole("ADMIN","USER")
                            .requestMatchers(HttpMethod.GET, "/users/update-password").hasAnyRole("ADMIN","USER")
                            .requestMatchers(HttpMethod.GET, "/users/*/memberId").hasAnyRole("ADMIN","USER")

                            .requestMatchers(HttpMethod.GET, "/users/booking-count/*").hasAnyRole("ADMIN","USER")
                            .requestMatchers(HttpMethod.GET, "/users/attended-percentage/*").hasAnyRole("ADMIN","USER")
                            .requestMatchers(HttpMethod.GET, "/users/future-booking-count/*").hasAnyRole("ADMIN","USER")
//                            .requestMatchers(HttpMethod.POST, "/expenditures").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.PUT, "/expenditures/*").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.DELETE, "/expenditures/*").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.GET, "/expenditures/member/*").hasAnyRole("ADMIN", "USER")
                            .requestMatchers(HttpMethod.POST, "/memberships/create/*").hasAnyRole("ADMIN", "USER")
                            .requestMatchers(HttpMethod.GET,"/memberships/all").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT,"/memberships/*").hasAnyRole("ADMIN", "USER")
                            .requestMatchers(HttpMethod.POST,"/expenditures").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET,"/expenditures/member/*").hasAnyRole("ADMIN","USER")
                            .requestMatchers(HttpMethod.GET,"/expenditures/store/*").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE,"/expenditures/*").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST,"/bookings").hasAnyRole("ADMIN", "USER")
                            .requestMatchers(HttpMethod.GET,"/bookings/*").hasAnyRole("ADMIN", "USER")
                            .requestMatchers(HttpMethod.GET,"/bookings/user/*").hasAnyRole("ADMIN", "USER")
                            .requestMatchers(HttpMethod.GET,"/stores/**").hasAnyRole("ADMIN","USER")
                            .requestMatchers(HttpMethod.GET,"/stores/*/availability/*").hasAnyRole("ADMIN","USER")
                            .requestMatchers(HttpMethod.DELETE,"/bookings/*").hasAnyRole("ADMIN", "USER")
                            .anyRequest().authenticated()

            
            )
            .userDetailsService(customuserDetailsService)
            .formLogin(form -> form
                    .loginPage("/LogIn")
                    .permitAll()
            )
            .logout(logout -> logout
                    .permitAll()
            )
        //.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter())))
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(Customizer.withDefaults())
        .exceptionHandling(exc -> exc.accessDeniedHandler(accessDeniedHandler()))

        .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern(CorsConfiguration.ALL);
        configuration.setAllowedMethods(List.of(CorsConfiguration.ALL));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}

