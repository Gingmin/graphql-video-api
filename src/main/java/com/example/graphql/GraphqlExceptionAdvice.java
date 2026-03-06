package com.example.graphql;

import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;

@ControllerAdvice
public class GraphqlExceptionAdvice {
    
    @GraphQlExceptionHandler(IllegalArgumentException.class)
    public GraphQLError handleIllegalArgument(IllegalArgumentException ex, DataFetchingEnvironment env) {
        return GraphQLError.newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message(ex.getMessage())
            .path(env.getExecutionStepInfo().getPath())
            .location(env.getField().getSourceLocation())
            .build();
    }

    @GraphQlExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public GraphQLError handleDataIntegrity(
        org.springframework.dao.DataIntegrityViolationException ex,
        DataFetchingEnvironment env
    ) {
        return GraphQLError.newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message("Error: " + ex.getMessage())
            .path(env.getExecutionStepInfo().getPath())
            .location(env.getField().getSourceLocation())
            .build();
    }
}
