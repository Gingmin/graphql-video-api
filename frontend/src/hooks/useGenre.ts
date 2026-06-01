import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { gqlClient } from "@/lib/graphql/client";
import { gql } from "graphql-request";
import { ErrorUtil } from "@/utils/error-util";

const ADD_GENRE = gql`
    mutation AddGenre($code: String!) {
        addGenre(code: $code) {
            id
            code
            createdAt
            modifiedAt
        }
    }
`;

const MODIFY_GENRE = gql`
    mutation ModifyGenre($id: ID!, $code: String!) {
        modifyGenre(id: $id, code: $code) {
            id
            code
            createdAt
            modifiedAt
        }
    }
`;

const DELETE_GENRE = gql`
    mutation DeleteGenre($id: ID!) {
        deleteGenre(id: $id)
    }
`;

const GENRES = gql`
    query Genres($page: Int!, $size: Int!) {
        genres(page: $page, size: $size) {
            items {
                id
                code
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

const GENRE = gql`
    query Genre($id: ID!) {
        genre(id: $id) {
            id
            code
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

export const useGenres = (page: number, size: number) => {
    return useQuery({
        queryKey: ["genres", { page, size }],
        queryFn: () => gqlClient.request(GENRES, { page, size }),
        staleTime: 1000 * 60 * 5,
    });
};

export const useGenre = (id: string) => {
    return useQuery({
        queryKey: ["genre", id],
        queryFn: () => gqlClient.request(GENRE, { id }),
        enabled: !!id,
        staleTime: 1000 * 60 * 5,
    });
};

export const useAddGenre = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (vars: { code: string }) => {
            return gqlClient.request(ADD_GENRE, vars);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["genres"] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useModifyGenre = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (vars: { id: string; code: string }) => {
            return gqlClient.request(MODIFY_GENRE, vars);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["genres"] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useDeleteGenre = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (id: string) => {
            return gqlClient.request(DELETE_GENRE, { id });
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["genres"] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useAddGenreTranslation = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (vars: { targetId: string; language: string; name: string }) => {
            return gqlClient.request(ADD_TRANSLATION, vars);
        },
        onSuccess: (_data, vars) => {
            queryClient.invalidateQueries({ queryKey: ["genre", vars.targetId] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useModifyGenreTranslation = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (vars: { id: string; targetId: string; language: string; name: string }) => {
            return gqlClient.request(MODIFY_TRANSLATION, vars);
        },
        onSuccess: (_data, vars) => {
            queryClient.invalidateQueries({ queryKey: ["genre", vars.targetId] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useDeleteGenreTranslation = (genreId: string, thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (id: string) => {
            return gqlClient.request(DELETE_TRANSLATION, { id });
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["genre", genreId] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};
