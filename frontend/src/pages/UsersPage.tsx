import React from "react";
import { useQuery } from "@tanstack/react-query";
import { gqlClient } from "@/lib/graphql/client";

function UsersPage() {
    const helloQuery = useQuery({
        queryKey: ["hello"],
        queryFn: async () => {
            const data = await gqlClient.request<{ hello: string }>(`query { hello }`);
            return data.hello;
        },
    });

    return (
        <div>
            <h1>Users Page</h1>
            <div style={{ marginTop: 12 }}>
                <div style={{ color: "#6b7280", fontSize: 12 }}>GraphQL Query.hello</div>
                {helloQuery.isLoading ? (
                    <div>loading...</div>
                ) : helloQuery.isError ? (
                    <pre style={{ whiteSpace: "pre-wrap" }}>{String(helloQuery.error)}</pre>
                ) : (
                    <div style={{ fontSize: 16 }}>{helloQuery.data}</div>
                )}
            </div>
        </div>
    );
}

export default UsersPage;
