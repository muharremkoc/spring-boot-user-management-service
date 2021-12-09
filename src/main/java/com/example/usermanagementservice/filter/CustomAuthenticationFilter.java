package com.example.usermanagementservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.usermanagementservice.services.mail.EmailService;
import com.example.usermanagementservice.services.mail.EmailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
     final AuthenticationManager authenticationManager;

    EmailService emailService=null;
     ObjectMapper errorMapper=new ObjectMapper();
    Map<String,String> errors=new HashMap<>();
    Map<String,Integer> lastLefts=new HashMap<>();
    // private int LAST_LEFT=3;



    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager=authenticationManager;

    }




    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username=request.getParameter("username");
        String password=request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,password);
        try {
            return authenticationManager.authenticate(authenticationToken);
        }catch (AuthenticationException failed){
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            emailService = webApplicationContext.getBean(EmailService.class);
            if(!lastLefts.containsKey(username)){

                lastLefts.put(username, 2);
                errors.put("LAST_LEFT",String.valueOf(2));
                unsuccessfulAuthentication(request,response,failed);
            } else {

                var lastLeft = lastLefts.get(username);
                if (lastLeft==1) {
                    errors.remove("LAST_LEFT");
                    errors.put("RESPONSE","Your attemps finished"+"Please,check your e-mail");
                    emailService.sendMail(username);
                    unsuccessfulAuthentication(request,response,failed);
                } else {
                    lastLeft = lastLeft - 1;
                    lastLefts.put(username, lastLeft);
                    errors.put("LAST_LEFT",String.valueOf(lastLeft));
                    unsuccessfulAuthentication(request,response,failed);
                }

            }
            /*try {
                if (LAST_LEFT>0){
                    if (lastLefts.containsKey(username)){
                        errors.put("LAST_LEFT", String.valueOf(attemps));
                    }else{
                        LAST_LEFT=3;
                        lastLefts.replace(username,LAST_LEFT);
                        errors.put("LAST_LEFT", String.valueOf(LAST_LEFT));
                    }
                    errors.put("WRONG","Username or password is wrong");

                    unsuccessfulAuthentication(request,response,failed);

                    log.error("You have {} last left",LAST_LEFT);
                }else if(LAST_LEFT<=0) {
                    Map<String,String> errors=new HashMap<>();
                    errors.put("error",failed.getMessage());
                    errors.put("wrong","All your rights are expired. Your rights are being reset");

                    errorMapper.writeValue(response.getOutputStream(),errors);
                   LAST_LEFT=3;

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }*/

        }
       return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user=(User) authentication.getPrincipal();
        Algorithm algorithm=Algorithm.HMAC256("secret".getBytes());
        String access_token= JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() +60*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refresh_token= JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() +120*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        /*
        response.setHeader("access_token",access_token);
        response.setHeader("refresh_token",refresh_token);

         */
        Map<String,String> tokens=new HashMap<>();
        tokens.put("access_token",access_token);
        tokens.put("refresh_token",refresh_token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {


                    /*lastLefts.put(request.getParameter("username"),LAST_LEFT);
                    if (lastLefts.containsKey(request.getParameter("username"))){
                        lastLefts.replace(request.getParameter("username"),LAST_LEFT);
                        errors.put("LAST_LEFT", String.valueOf(LAST_LEFT));
                    }else{
                       LAST_LEFT=3;
                        lastLefts.put(request.getParameter("username"),LAST_LEFT);

                    }

                     */
                     errors.put("WRONG","Username or password is wrong");
                     //var ll = lastLefts.get(request.getParameter("username"));
                     errors.put("ERROR",failed.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(404);
                    errorMapper.writeValue(response.getOutputStream(),errors);




    }


}
