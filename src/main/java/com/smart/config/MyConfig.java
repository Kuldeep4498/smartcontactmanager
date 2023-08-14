package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class MyConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();

	}

    @Bean
    UserDetailsService getUserDetailsService() {

		return new UserDetailsServiceImpl();

	}

    @Bean
    DaoAuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

		return daoAuthenticationProvider;

	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		/*
		 * http.securityMatcher("/User/**").authorizeHttpRequests(authorize ->
		 * authorize.anyRequest().hasRole("USER")) .securityMatcher("/admin/**")
		 * .authorizeHttpRequests(authorize ->
		 * authorize.anyRequest().hasRole("ADMIN")).securityMatcher("/**")
		 * .authorizeHttpRequests(authorize ->
		 * authorize.anyRequest().permitAll()).formLogin(withDefaults());
		 * 
		 */

		http
		.authorizeHttpRequests(authorize -> {
			try {
				authorize
						.requestMatchers("/user/**").hasRole("USER")
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/**").permitAll().anyRequest().authenticated();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).formLogin();
		
		/*
		 * .authorizeHttpRequests() .requestMatchers("/USER/**").hasRole("USER")
		 * .requestMatchers("ADMIN/**").hasRole("ADMIN") .anyRequest() .permitAll()
		 * .and() .formLogin();
		 */

		return http.build();

	}

}

/*
 * @Configuration
 * 
 * @EnableWebSecurity public class MyConfig extends WebSecurityConfigurerAdapter
 * {
 * 
 * 
 * @Bean public UserDetailsService getUserDetailsService(){
 * 
 * 
 * return new UserDetailsServiceImpl();
 * 
 * 
 * }
 * 
 * 
 * @Bean public PasswordEncoder passwordEncoder(){
 * 
 * return new BCryptPasswordEncoder(); }
 * 
 * 
 * 
 * @Bean public DaoAuthenticationProvider authenticationProvider() {
 * 
 * DaoAuthenticationProvider daoAuthenticationProvider = new
 * DaoAuthenticationProvider();
 * 
 * daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService())
 * ; daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
 * 
 * return daoAuthenticationProvider;
 * 
 * }
 * 
 * //// congfigure method
 * 
 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
 * Exception {
 * 
 * 
 * auth.authenticationProvider(authenticationProvider());
 * 
 * }
 * 
 * @Override protected void configure(HttpSecurity http) throws Exception {
 * 
 * 
 * http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").
 * antMatchers("/USER/**").hasRole("USER")
 * .antMatchers("/**").permitAll().and().formLogin().and().csrf().disable();
 * 
 * }
 * 
 * 
 * 
 * }
 */
