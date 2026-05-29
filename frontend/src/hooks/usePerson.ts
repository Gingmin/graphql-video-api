import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { gqlClient } from "@/lib/graphql/client";
import { gql } from "graphql-request";

import { ErrorUtil } from "@/utils/error-util";

const ADD_PERSON = gql`
    mutation AddPerson($code: String!, $birthDate: String, $nationality: String) {
        addPerson(code: $code, birthDate: $birthDate, nationality: $nationality) {
            id
            code
            birthDate
            nationality
            createdAt
            modifiedAt
        }
    }
`;

const MODIFY_PERSON = gql`
    mutation ModifyPerson($id: ID!, $code: String!, $birthDate: String, $nationality: String) {
        modifyPerson(id: $id, code: $code, birthDate: $birthDate, nationality: $nationality) {
            id
            code
            birthDate
            nationality
            createdAt
            modifiedAt
        }
    }
`;

const DELETE_PERSON = gql`
    mutation DeletePerson($id: ID!) {
        deletePerson(id: $id)
    }
`;

const PERSONS = gql`
    query Persons($page: Int!, $size: Int!) {
        persons(page: $page, size: $size) {
            items {
                id
                code
                birthDate
                nationality
                createdAt
                modifiedAt
            }
            pageInfo {
                page
                size
                totalElements
                totalPages
                hasNext
                hasPrev
            }
        }
    }
`;

const PERSON = gql`
    query Person($id: ID!) {
        person(id: $id) {
            id
            code
            birthDate
            nationality
            createdAt
            modifiedAt
            translations {
                id
                targetId
                language
                name
                createdAt
                modifiedAt
            }
        }
    }
`;

const ADD_TRANSLATION = gql`
    mutation AddTranslation($targetId: ID!, $language: String!, $name: String!) {
        addTranslation(targetId: $targetId, language: $language, name: $name) {
            id
            targetId
            language
            name
            createdAt
            modifiedAt
        }
    }
`;

const MODIFY_TRANSLATION = gql`
    mutation ModifyTranslation($id: ID!, $language: String!, $name: String!) {
        modifyTranslation(id: $id, language: $language, name: $name) {
            id
            language
            name
            createdAt
            modifiedAt
        }
    }
`;

const DELETE_TRANSLATION = gql`
    mutation DeleteTranslation($id: ID!) {
        deleteTranslation(id: $id)
    }
`;

export const usePersons = (page: number, size: number) => {
    return useQuery({
        queryKey: ["persons", { page, size }],
        queryFn: () => gqlClient.request(PERSONS, { page, size }),
        staleTime: 1000 * 60 * 5,
    });
};

export const usePerson = (id: string) => {
    return useQuery({
        queryKey: ["person", id],
        queryFn: () => gqlClient.request(PERSON, { id }),
        enabled: !!id,
        staleTime: 1000 * 60 * 5,
    });
};

export const useAddPerson = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (vars: { code: string; birthDate: string; nationality: string }) => {
            return gqlClient.request(ADD_PERSON, vars);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["persons"] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useModifyPerson = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (vars: { id: string; code: string; birthDate: string; nationality: string }) => {
            return gqlClient.request(MODIFY_PERSON, vars);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["persons"] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useDeletePerson = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (id: string) => {
            return gqlClient.request(DELETE_PERSON, { id });
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["persons"] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useAddPersonTranslation = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (vars: { targetId: string; language: string; name: string }) => {
            return gqlClient.request(ADD_TRANSLATION, vars);
        },
        onSuccess: (_data, vars) => {
            queryClient.invalidateQueries({ queryKey: ["person", vars.targetId] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useModifyPersonTranslation = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (vars: { id: string; targetId: string; language: string; name: string }) => {
            return gqlClient.request(MODIFY_TRANSLATION, vars);
        },
        onSuccess: (_data, vars) => {
            queryClient.invalidateQueries({ queryKey: ["person", vars.targetId] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useDeletePersonTranslation = (personId: string, thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (id: string) => {
            return gqlClient.request(DELETE_TRANSLATION, { id });
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["person", personId] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};
