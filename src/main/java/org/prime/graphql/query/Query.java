package org.prime.graphql.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.prime.graphql.core.AuthContext;
import org.prime.graphql.exeption.UnAuthorizedException;
import org.prime.graphql.model.RoleName;
import org.prime.graphql.model.User;
import org.prime.graphql.repository.UserRepository;

import java.util.List;
import java.util.Objects;

public class Query implements GraphQLQueryResolver{

    private  UserRepository userRepository;

    public Query(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers(DataFetchingEnvironment env) {
        AuthContext context = env.getContext();
        if(context.getUser() == null){
            throw new UnAuthorizedException("You are not logged in!!", null);
        }else if(!Objects.equals(context.getUser().getRole().getName(), RoleName.ROLE_ADMIN)){
            throw new UnAuthorizedException("You have no permission to execute this query!", context.getUser().getRole().getName().toString());
        }else{
            return userRepository.findAll();
        }
    }
}
