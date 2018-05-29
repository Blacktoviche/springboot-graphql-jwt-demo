package org.prime.graphql;

import graphql.servlet.DefaultGraphQLErrorHandler;
import org.prime.graphql.core.GraphQLEndpoint;
import org.prime.graphql.repository.UserRepository;
import org.prime.graphql.security.jwt.JwtTokenUtil;
import org.prime.graphql.security.jwt.JwtUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SpringbootGraphqlJwtDemoApplication {

    public static final Logger log = LoggerFactory.getLogger(DefaultGraphQLErrorHandler.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringbootGraphqlJwtDemoApplication.class, args);
    }

    @Bean
    @Autowired
    public ServletRegistrationBean graphQLServlet(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService) {
        return new ServletRegistrationBean(new GraphQLEndpoint(userRepository, jwtTokenUtil, userDetailsService), "/graphql");
    }
}
