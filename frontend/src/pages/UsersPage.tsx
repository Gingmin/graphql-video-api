import React from "react";
import { useQuery } from "@tanstack/react-query";
import { gqlClient } from "@/lib/graphql/client";
import { useNavigate } from "react-router-dom";

import { useLogout } from "@/hooks/useAuth";

function UsersPage() {
    const navigate = useNavigate();

    const logoutMutation = useLogout(() => {
        navigate("/login");
    });

    const handleLogout = () => {
        logoutMutation.mutate();
    };

    return (
        <div className="common-page">
            <h1>Users Page</h1>
            <button onClick={handleLogout}>Logout</button>
            <button onClick={() => navigate("/upload")}>Upload</button>
        </div>
    );
}

export default UsersPage;
