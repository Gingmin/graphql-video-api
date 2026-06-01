import LoginPage from "./login/LoginPage";
import { createBrowserRouter } from "react-router-dom";
import React from "react";

import App from "@/App";
import ProtectedRoute from "@/pages/ProtectedRoute";
import PublicOnlyRoute from "@/pages/PublicOnlyRoute";

import HomePage from "@/pages/home/HomePage";
import SignUpPage from "@/pages/login/SignUpPage";

import DashboardPage from "@/pages/home/DashboardPage";
import UsersPage from "@/pages/user/UsersPage";
import FileUploadPage from "@/pages/upload/FileUploadPage";
import FilePage from "@/pages/file/FilePage";
import PersonPage from "@/pages/person/PersonPage";
import TagPage from "@/pages/Tag/TagPage";
import GenrePage from "@/pages/genre/GenrePage";

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
        path: "/home",
        component: DashboardPage,
        meta: {
            title: "홈",
            requiresAuth: true,
            permissions: ["admin"],
        },
    },
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
    {
        path: "/files",
        component: FilePage,
        meta: {
            title: "파일 관리",
            requiresAuth: true,
            permissions: ["admin"],
        },
    },
    {
        path: "/person",
        component: PersonPage,
        meta: {
            title: "사람 관리",
            requiresAuth: true,
            permissions: ["admin"],
        },
    },
    {
        path: "/tag",
        component: TagPage,
        meta: {
            title: "태그 관리",
            requiresAuth: true,
            permissions: ["admin"],
        },
    },
    {
        path: "/genre",
        component: GenrePage,
        meta: {
            title: "장르 관리",
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
