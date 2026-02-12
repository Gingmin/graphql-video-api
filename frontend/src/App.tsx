import { useEffect, useState } from "react";

import "@/styles/app.scss";

type GqlResponse<T> = {
    data?: T;
    errors?: Array<{ message: string }>;
};

async function gql<T>(query: string, variables?: Record<string, unknown>): Promise<T> {
    const res = await fetch("/graphql", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ query, variables }),
    });

    const json = (await res.json()) as GqlResponse<T>;
    if (json.errors?.length) {
        throw new Error(json.errors.map((e) => e.message).join("\n"));
    }
    if (!json.data) {
        throw new Error("No data");
    }
    return json.data;
}

export function App() {
    const [value, setValue] = useState<string>("loading...");
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        (async () => {
            try {
                const data = await gql<{ hello: string }>(`query { hello }`);
                setValue(data.hello);
            } catch (e) {
                setError(String(e));
            }
        })();
    }, []);

    return (
        <div style={{ fontFamily: "system-ui, -apple-system, Segoe UI, Roboto, Arial", padding: 24 }}>
            <h1 style={{ margin: 0 }}>gingflix</h1>
            <p style={{ marginTop: 8, color: "#6b7280" }}>React(Vite) → GraphQL(/graphql) 연결 테스트</p>
            {error ? (
                <pre style={{ background: "#111827", color: "#e5e7eb", padding: 12, borderRadius: 8, overflowX: "auto" }}>{error}</pre>
            ) : (
                <div style={{ marginTop: 12, padding: 12, border: "1px solid #e5e7eb", borderRadius: 8 }}>
                    <div style={{ color: "#6b7280", fontSize: 12 }}>Query.hello response</div>
                    <div style={{ fontSize: 18, marginTop: 6 }}>{value}</div>
                </div>
            )}
            <div style={{ marginTop: 16, display: "flex", gap: 10, flexWrap: "wrap" }}>
                <a href="http://localhost:8080/graphiql" target="_blank" rel="noreferrer">
                    GraphiQL (8080)
                </a>
                <span style={{ color: "#9ca3af" }}>|</span>
                <a href="http://localhost:5173" target="_blank" rel="noreferrer">
                    Vite dev (5173)
                </a>
            </div>
        </div>
    );
}
