import LoginPage from "./login/LoginPage";
import { createBrowserRouter } from "react-router-dom";
import React from "react";

import App from "@/App";
import ProtectedRoute from "@/pages/ProtectedRoute";
import PublicOnlyRoute from "@/pages/PublicOnlyRoute";

import HomePage from "@/pages/home/HomePage";
import SignUpPage from "@/pages/login/SignUpPage";

import UsersPage from "@/pages/UsersPage";
import FileUploadPage from "@/pages/upload/FileUploadPage";

export interface PageConfig {
    path: string;
    component: React.ComponentType;
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
        meta: { title: "Home" },
    },
    {
        path: "/login",
        component: LoginPage,
        meta: { title: "Login" },
    },
    {
        path: "/signup",
        component: SignUpPage,
        meta: { title: "Sign Up" },
    },
];

export const pages: PageConfig[] = [
    {
        path: "/users",
        component: UsersPage,
        meta: {
            title: "사용자 관리",
            requiresAuth: true,
            permissions: ["admin"],
        },
    },
    {
        path: "/upload",
        component: FileUploadPage,
        meta: {
            title: "파일 업로드",
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
