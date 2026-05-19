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
            <div className="button-container">
                <div>
                    <button className="confirm-button" onClick={handleLogout}>
                        Logout
                    </button>
                </div>
                <div>
                    <button className="go-button" onClick={() => navigate("/person")}>
                        Person
                    </button>
                    <button className="go-button" onClick={() => navigate("/upload")}>
                        Upload
                    </button>
                </div>
            </div>
        </div>
    );
}

export default UsersPage;
