package org.prime.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.prime.graphql.model.User;

public class SigninResolver implements GraphQLResolver<SigninPayload> {

    public User user(SigninPayload payload) {
        return payload.getUser();
    }
}
