import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { gqlClient } from "@/lib/graphql/client";
import { gql } from "graphql-request";
import { ErrorUtil } from "@/utils/error-util";

const ADD_TAG = gql`
    mutation AddTag($code: String!) {
        addTag(code: $code) {
            id
            code
            createdAt
            modifedAt
        }
    }
`;

const MODIFY_TAG = gql`
    mutation ModifyTag($id: ID!, $code: String!) {
        modifyTag(id: $id, code: $code) {
            id
            code
            createdAt
            modifiedAt
        }
    }
`;

const DELETE_TAG = gql`
    mutation DeleteTag($id: ID!) {
        deleteTag(id: $id)
    }
`;

const TAGS = gql`
    query Tags($page: Int!, $size: Int!) {
        tags(page: $page, size: $size) {
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

const TAG = gql`
    query Tag($id: ID!) {
        tag(id: $id) {
            id
            code
            createdAt
            modifiedAt
            translations {
                id
                tagId
                language
                name
                createdAt
                modifiedAt
            }
        }
    }
`;

const ADD_TAG_TRNASLATION = gql`
    mutation AddTagTranslation($tagId: ID!, $language: String!, $name: String!) {
        addTagTranslation(tagId: $tagId, language: $language, name: $name) {
            id
            tagId
            language
            name
            createdAt
            modifiedAt
        }
    }
`;

const MODIFY_TAG_TRNASLATION = gql`
    mutation ModifyTagTranslation($id: ID!, $language: String!, $name: String!) {
        modifyTagTranslation(id: $id, language: $language, name: $name) {
            id
            language
            name
            createdAt
            modifiedAt
        }
    }
`;

const DELETE_TAG_TRNASLATION = gql`
    mutation DeleteTagTranslation($id: ID!) {
        deleteTagTranslation(id: $id)
    }
`;

export const useTags = (page: number, size: number) => {
    return useQuery({
        queryKey: ["tags", { page, size }],
        queryFn: () => gqlClient.request(TAGS, { page, size }),
        staleTime: 1000 * 60 * 5,
    });
};

export const useTag = (id: string) => {
    return useQuery({
        queryKey: ["tag", id],
        queryFn: () => gqlClient.request(TAG, { id }),
        enabled: !!id,
        staleTime: 1000 * 60 * 5,
    });
};

export const useAddTag = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (vars: { code: string }) => {
            return gqlClient.request(ADD_TAG, vars);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["tags"] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useModifyTag = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (vars: { id: string; code: string }) => {
            return gqlClient.request(MODIFY_TAG, vars);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["tags"] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useDeleteTag = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (id: string) => {
            return gqlClient.request(DELETE_TAG, { id });
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["tags"] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useAddTagTranslation = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (vars: { tagId: string; language: string; name: string }) => {
            return gqlClient.request(ADD_TAG_TRNASLATION, vars);
        },
        onSuccess: (_data, vars) => {
            queryClient.invalidateQueries({ queryKey: ["tag", vars.tagId] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useModifyTagTranslation = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (vars: { id: string; tagId: string; language: string; name: string }) => {
            return gqlClient.request(MODIFY_TAG_TRNASLATION, vars);
        },
        onSuccess: (_data, vars) => {
            queryClient.invalidateQueries({ queryKey: ["tag", vars.tagId] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};

export const useDeleteTagTranslation = (tagId: string, thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (id: string) => {
            return gqlClient.request(DELETE_TAG_TRNASLATION, { id });
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["tag", tagId] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};
