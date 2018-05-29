package org.prime.graphql.core;

import com.coxautodev.graphql.tools.SchemaParser;
import graphql.servlet.GraphQLContext;
import graphql.servlet.SimpleGraphQLServlet;
import org.prime.graphql.handler.CustomGraphQLErrorHandler;
import org.prime.graphql.model.User;
import org.prime.graphql.mutation.Mutation;
import org.prime.graphql.query.Query;
import org.prime.graphql.repository.UserRepository;
import org.prime.graphql.security.jwt.JwtTokenUtil;
import org.prime.graphql.security.jwt.JwtUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


//@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    private UserRepository userRepository;
    private JwtTokenUtil jwtTokenUtil;
    private JwtUserDetailsService userDetailsService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public GraphQLEndpoint(UserRepository userRepository, JwtTokenUtil jwtTokenUtil,JwtUserDetailsService userDetailsService) {
        super(new SimpleGraphQLServlet.Builder(SchemaParser.newParser()
                .files("graphql/schema.graphqls")
                .resolvers(new Query(userRepository), new Mutation(userRepository, jwtTokenUtil, userDetailsService))
                .build()
                .makeExecutableSchema()).withGraphQLErrorHandler(new CustomGraphQLErrorHandler()));
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected GraphQLContext createContext(Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        //logger.info("auth Context ");
        User user = request
                .map(req -> req.getHeader("Authorization"))
                .filter(token -> !token.isEmpty())
                .map(token -> token.replace("Bearer ", ""))
                .map(authToken -> userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(authToken)).get())
                .orElse(null);
        if (Optional.ofNullable(user).isPresent()) {
            logger.info("logged in user is {}", user.getUsername());
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                logger.info("context is null");
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request.get()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            logger.info("user is null!");
        }

        return new AuthContext(user, request, response);
    }

}
