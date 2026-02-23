import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { gqlClient } from "@/lib/graphql/client";
import { gql } from "graphql-request";

const ME = gql`
    query {
        me {
            id
            email
            name
        }
    }
`;

const LOGIN = gql`
    mutation Login($email: String!, $password: String!) {
        login(email: $email, password: $password) {
            user {
                id
                email
                name
            }
        }
    }
`;

const LOGOUT = gql`
    mutation {
        logout
    }
`;

export const useMe = () => {
    return useQuery({
        queryKey: ["me"],
        queryFn: () => gqlClient.request(ME),
        retry: false,
        staleTime: Infinity,
    });
};

export const useLogin = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (vars: { email: string; password: string }) => {
            return gqlClient.request(LOGIN, vars);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["me"] });
        },
    });
};

export const useLogout = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: () => {
            return gqlClient.request(LOGOUT);
        },
        onSuccess: () => {
            queryClient.setQueryData(["me"], null);
        },
    });
};
