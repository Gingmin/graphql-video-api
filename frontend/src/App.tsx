import { useEffect, useState } from "react";

import "@/styles/app.scss";
import { Link } from "react-router-dom";

import { useMe } from "@/hooks/useAuth";

import HomePage from "@/pages/home/HomePage";

export default function App() {
    const { data, isLoading } = useMe();
    const user = data?.me;

    console.log("isLoading", isLoading);
    console.log("user", user);

    if (isLoading) {
        return <div>loading...</div>;
    }

    if (!user) {
        return <HomePage />;
    }

    return (
        <div>
            <h1>Hello World</h1>
            {/* <Link to="/users">Users</Link> */}
        </div>
    );
}
