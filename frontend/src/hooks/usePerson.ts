import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { gqlClient } from "@/lib/graphql/client";
import { gql } from "graphql-request";

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
        }
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
            const msg = error.response?.errors?.[0]?.message ?? "알 수 없는 오류";
            alert(msg);
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
            const msg = error.response?.errors?.[0]?.message ?? "알 수 없는 오류";
            alert(msg);
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
            const msg = error.response?.errors?.[0]?.message ?? "알 수 없는 오류";
            alert(msg);
        },
    });
};
