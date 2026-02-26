import LoginPage from "./login/LoginPage";
import { createBrowserRouter } from "react-router-dom";
import React from "react";
import ProtectedRoute from "./ProtectedRoute";
import UsersPage from "./UsersPage";
import App from "../App";
import SignUpPage from "@/pages/login/SignUpPage";
import HomePage from "./home/HomePage";
import PublicOnlyRoute from "./PublicOnlyRoute";

export interface PageConfig {
    path: string;
    component: React.ComponentType;
    layout?: "auth" | "admin" | "default";
    meta?: {
        title?: string;
        requiresAuth?: boolean;
        permissions?: string[];
    };
}

export const publicPages: PageConfig[] = [
    {
        path: "/",
        component: HomePage,
        layout: "default",
        meta: { title: "Home" },
    },
    {
        path: "/login",
        component: LoginPage,
        layout: "auth",
        meta: { title: "Login" },
    },
    {
        path: "/signup",
        component: SignUpPage,
        layout: "auth",
        meta: { title: "Sign Up" },
    },
];

export const pages: PageConfig[] = [
    {
        path: "/users",
        component: UsersPage,
        layout: "admin",
        meta: {
            title: "사용자 관리",
            requiresAuth: true,
            permissions: ["admin"],
        },
    },
];

const router = createBrowserRouter([
    {
        path: "/",
        element: <App />,
        children: [
            ...publicPages.map(({ path, component: Component, meta }) => ({
                path,
                element: (
                    <PublicOnlyRoute>
                        <Component />
                    </PublicOnlyRoute>
                ),
            })),
            ...pages.map(({ path, component: Component, meta }) => ({
                path,
                element: meta?.requiresAuth ? (
                    <ProtectedRoute permissions={meta.permissions || []}>
                        <Component />
                    </ProtectedRoute>
                ) : (
                    <Component />
                ),
            })),
        ],
    },
]);

export default router;
