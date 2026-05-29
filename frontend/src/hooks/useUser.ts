import { useQuery } from "@tanstack/react-query";
import { gqlClient } from "@/lib/graphql/client";
import { gql } from "graphql-request";

const USERS = gql`
    query Users($page: Int!, $size: Int!) {
        users(page: $page, size: $size) {
            items {
                id
                name
                email
                latestLoginIp
                lastLoginDate
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

export const useUsers = (page: number, size: number) => {
    return useQuery({
        queryKey: ["users", { page, size }],
        queryFn: () => gqlClient.request(USERS, { page, size }),
        staleTime: 1000 * 60 * 5,
    });
};
