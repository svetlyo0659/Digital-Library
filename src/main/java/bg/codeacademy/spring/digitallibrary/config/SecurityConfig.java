package bg.codeacademy.spring.digitallibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    http
        .authorizeRequests()
        // On Start user are ALLOWED to CREATE ACCOUNTS;
        .antMatchers(HttpMethod.POST, "/api/v1/user").permitAll()
        // all other API requests are AUTHORIZED by ROLE / USERNAME & PASSWORD
        .antMatchers(HttpMethod.GET, "/api/v1/user").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/api/v1/user/**/remove").hasRole("ADMIN")
        .antMatchers(HttpMethod.PUT, "/api/v1/user/{user_id}/enable").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/api/v1/library").hasAnyRole("READER", "AUTHOR")
        .antMatchers(HttpMethod.POST, "/api/v1/library/add").hasAnyRole("READER", "AUTHOR")
        .antMatchers(HttpMethod.PATCH, "/api/v1/library/**/rate").hasAnyRole("READER", "AUTHOR")
        .antMatchers(HttpMethod.DELETE, "/api/v1/library/remove").hasAnyRole("READER", "AUTHOR")
        .antMatchers(HttpMethod.GET, "/api/v1/library/recent").hasAnyRole("READER", "AUTHOR")
        .antMatchers(HttpMethod.GET, "/api/v1/library/numberOfLibraries").hasAnyRole("AUTHOR")
        .antMatchers(HttpMethod.GET, "/api/v1/book").hasAnyRole("AUTHOR", "READER")
        .antMatchers(HttpMethod.POST, "/api/v1/book").hasRole("AUTHOR")
        .antMatchers(HttpMethod.PATCH, "/api/v1/book/{book_id}/enable").hasRole("AUTHOR")
        .antMatchers(HttpMethod.GET, "/api/v1/library/download/**").hasAnyRole("READER", "AUTHOR")

        .and()
        .httpBasic();

    http.csrf().disable();
    http.headers().frameOptions().disable();
  }


  @Bean
  PasswordEncoder passwordEncoder()
  {
    return new BCryptPasswordEncoder();
  }

}