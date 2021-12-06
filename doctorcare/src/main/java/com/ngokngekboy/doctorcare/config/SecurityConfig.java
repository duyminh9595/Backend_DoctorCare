package com.ngokngekboy.doctorcare.config;

import com.ngokngekboy.doctorcare.dao.AdminRepository;
import com.ngokngekboy.doctorcare.dao.DoctorRepository;
import com.ngokngekboy.doctorcare.dao.PatientRepository;
import com.ngokngekboy.doctorcare.jwt.JwtValidator;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.core.config.ProjectionDefinitionConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private Environment environment;
    private PatientRepository patientRepository;
    private AdminRepository adminRepository;
    private DoctorRepository doctorRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterBefore(new JwtValidator(environment,patientRepository,adminRepository,doctorRepository),UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class);
        http.cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/khoa/themmoi","/admin/core/**","/admin/checktokenvalid","/hosobenhan/search/getAllByDate").hasRole("ADMIN")
                .antMatchers("/patient/core/**").hasRole("PATIENT")
                .antMatchers("/doctor/core/**").hasRole("DOCTOR")
                .anyRequest().permitAll();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }




}
