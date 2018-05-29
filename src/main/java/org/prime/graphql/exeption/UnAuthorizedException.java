package org.prime.graphql.exeption;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnAuthorizedException extends RuntimeException implements GraphQLError {

    private Map<String, Object> extensions = new HashMap<>();

    public UnAuthorizedException(String message, String userRole) {
        super(message);
        extensions.put("userRole", userRole);
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.DataFetchingException;
    }
}