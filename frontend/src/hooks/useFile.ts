import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { gqlClient } from "@/lib/graphql/client";
import { gql } from "graphql-request";
import { ErrorUtil } from "@/utils/error-util";

const FILES = gql`
    query Files($page: Int!, $size: Int!) {
        files(page: $page, size: $size) {
            items {
                id
                name
                originalName
                extension
                mimeType
                fileSize
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

const FILE = gql`
    query File($id: ID!) {
        file(id: $id) {
            id
            name
            originalName
            path
            extension
            mimeType
            fileSize
            createdAt
            modifiedAt
        }
    }
`;

const DELETE_FILE = gql`
    mutation DeleteFile($id: ID!) {
        deleteFile(id: $id)
    }
`;

export const useFiles = (page: number, size: number) => {
    return useQuery({
        queryKey: ["files", { page, size }],
        queryFn: () => gqlClient.request(FILES, { page, size }),
        staleTime: 1000 * 60 * 5,
    });
};

export const useFile = (id: string) => {
    return useQuery({
        queryKey: ["file", id],
        queryFn: () => gqlClient.request(FILE, { id }),
        enabled: !!id,
        staleTime: 1000 * 60 * 5,
    });
};

export const useDeleteFile = (thenFn?: () => void) => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (id: string) => {
            return gqlClient.request(DELETE_FILE, { id });
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["files"] });
            thenFn?.();
        },
        onError: (error: any) => {
            ErrorUtil.errorHandler(error);
        },
    });
};
