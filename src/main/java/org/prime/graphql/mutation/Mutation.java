package org.prime.graphql.mutation;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;
import org.prime.graphql.model.AuthData;
import org.prime.graphql.model.User;
import org.prime.graphql.repository.UserRepository;
import org.prime.graphql.resolver.SigninPayload;
import org.prime.graphql.security.jwt.JwtTokenUtil;
import org.prime.graphql.security.jwt.JwtUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Mutation implements GraphQLMutationResolver{

    // NOT AUTOWIRED HERE IT"S NOT WORKING
    private JwtTokenUtil jwtTokenUtil;
    private JwtUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Mutation(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }


    public SigninPayload signinUser(AuthData auth, DataFetchingEnvironment env){
        User user = userRepository.findByUsername(auth.getUsername()).get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(auth.getPassword(), user.getPassword())) {
            logger.info("match");
            UserDetails userDetails = userDetailsService.loadUserByUsername(auth.getUsername());
            logger.info("userdetails: {}", userDetails);
            Authentication authentication = new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword(), userDetails.getAuthorities());
            logger.info("authentication: {}", authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //context.getRequest().get().getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            logger.info("context is ready");
            return new SigninPayload(jwtTokenUtil.generateToken(user.getUsername()), user);
        }
        throw new GraphQLException("Invalid credentials");
    }

}
