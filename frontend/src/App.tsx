import { useEffect, useState } from "react";

import "@/styles/app.scss";
import { Link } from "react-router-dom";

import HomePage from "@/pages/home/HomePage";

export default function App() {
    const isAuthenticated = localStorage.getItem("token");
    if (!isAuthenticated) {
        return <HomePage />;
    }

    return (
        <div>
            <h1>Hello World</h1>
            <Link to="/login">Login</Link>
            <Link to="/users">Users</Link>
        </div>
    );
}
