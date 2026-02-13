export type RestError = Error & { status?: number; body?: unknown };

async function parseBody(res: Response) {
    const ct = res.headers.get("content-type") ?? "";
    if (ct.includes("application/json")) {
        return await res.json();
    }
    return await res.text();
}

async function request<T>(method: string, url: string, body?: unknown): Promise<T> {
    const token = localStorage.getItem("token");

    const res = await fetch(url, {
        method,
        headers: {
            ...(body != null ? { "Content-Type": "application/json" } : {}),
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
        body: body != null ? JSON.stringify(body) : undefined,
    });

    const parsed = await parseBody(res);

    if (!res.ok) {
        const err: RestError = new Error(typeof parsed === "string" ? parsed : res.statusText);
        err.status = res.status;
        err.body = parsed;
        throw err;
    }

    return parsed as T;
}

export const rest = {
    get: <T>(url: string) => request<T>("GET", url),
    post: <T>(url: string, body?: unknown) => request<T>("POST", url, body),
    put: <T>(url: string, body?: unknown) => request<T>("PUT", url, body),
    patch: <T>(url: string, body?: unknown) => request<T>("PATCH", url, body),
    del: <T>(url: string) => request<T>("DELETE", url),
};

