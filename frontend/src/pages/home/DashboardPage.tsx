import { useNavigate } from "react-router-dom";
import { useLogout, useMe } from "@/hooks/useAuth";

export default function DashboardPage() {
    const navigate = useNavigate();
    const { data } = useMe();
    const user = data?.me;

    const logoutMutation = useLogout(() => {
        navigate("/login");
    });

    return (
        <div className="api-page">
            <h1>Home</h1>

            {user && (
                <div className="detail-container" style={{ marginBottom: "1.5rem" }}>
                    <div className="detail-form">
                        <div className="detail-field">
                            <label>Name</label>
                            <span>{user.name}</span>
                        </div>
                        <div className="detail-field">
                            <label>Email</label>
                            <span>{user.email}</span>
                        </div>
                    </div>
                </div>
            )}

            <div className="button-container" style={{ display: "flex", gap: "0.5rem", flexWrap: "wrap" }}>
                <button className="go-button" onClick={() => navigate("/users")}>
                    Users
                </button>
                <button className="go-button" onClick={() => navigate("/person")}>
                    Person
                </button>
                <button className="go-button" onClick={() => navigate("/tag")}>
                    Tag
                </button>
                <button className="go-button" onClick={() => navigate("/genre")}>
                    Genre
                </button>
                <button className="go-button" onClick={() => navigate("/files")}>
                    Files
                </button>
                <button className="go-button" onClick={() => navigate("/upload")}>
                    Upload
                </button>
            </div>

            <div style={{ marginTop: "2rem" }}>
                <button className="confirm-button" onClick={() => logoutMutation.mutate()}>
                    Logout
                </button>
            </div>
        </div>
    );
}
