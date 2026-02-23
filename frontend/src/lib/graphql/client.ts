import { GraphQLClient } from "graphql-request";

function getOrigin(): string {
    // 브라우저: 현재 origin (dev에선 http://localhost:5173, 운영에선 http://localhost:8080)
    if (typeof window !== "undefined" && window.location?.origin) {
        return window.location.origin;
    }
    // 타입체크/Node 환경 fallback (실행 시에는 보통 브라우저에서만 사용)
    return "http://localhost:8080";
}

function buildHeaders(): Record<string, string> {
    const headers: Record<string, string> = {
        "Content-Type": "application/json",
    };
    return headers;
}

/**
 * GraphQL endpoint client.
 * - base URL: /graphql (Vite proxy 또는 Spring static에서 같은 origin으로 동작)
 * - auth: localStorage.token 이 있으면 Bearer로 자동 첨부
 */
export function createGqlClient() {
    const endpoint = new URL("/graphql", getOrigin()).toString();
    return new GraphQLClient(endpoint, {
        headers: () => buildHeaders(),
        credentials: "include",
    });
}

export const gqlClient = createGqlClient();
