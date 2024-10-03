package com.springboot.estore.Estore.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    //this code executes before api to verify the jwt in the header
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Authorization : Bearer nwofewjjfrifrifrijnf
        String requestHeader = request.getHeader("Authorization");

        logger.info("Header --> "+requestHeader);

        String userName = null;

        String token = null;


        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7);

            try{
                userName = jwtHelper.getUsernameFromToken(token);
                logger.info("Username --> "+userName);
            }
            catch(IllegalArgumentException e){
                logger.info("Illegal Argument Exception occured : "+e.getMessage());
            }
            catch(ExpiredJwtException e){
                logger.info("Given jwt is expired : "+e.getMessage());
            }
            catch(MalformedJwtException e){
                logger.info("Malformed Jwt Exception occured : "+e.getMessage());
            }
            catch (Exception e){
                logger.info("General Exception occured");
                e.printStackTrace();
            }
        }
        else
        {
            logger.info("Invalid header. Header is not starting with Bearer");
        }

        if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null) {
            //username contains something
            //authentication is null : the user is not previously authenticated

            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

            if(userName.equals(userDetails.getUsername()) && !jwtHelper.isTokenExpired(token)) {

                //token valid
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }


        }

        filterChain.doFilter(request, response);



    }
}
