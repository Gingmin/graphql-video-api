import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { DateUtil } from "@/utils/date-util";
import { useUsers } from "@/hooks/useUser";

type User = {
    id: string;
    name: string;
    email: string;
    latestLoginIp?: string;
    lastLoginDate?: string;
    createdAt?: string;
    modifiedAt?: string;
};

export default function UsersPage() {
    const navigate = useNavigate();

    const [page, setPage] = useState(1);
    const [size, setSize] = useState(5);

    const [selectedUser, setSelectedUser] = useState<User | null>(null);

    const usersQuery = useUsers(page, size);

    const handleRowClick = (user: User) => {
        setSelectedUser(user);
    };

    const handleCloseDetail = () => {
        setSelectedUser(null);
    };

    return (
        <div className="api-page">
            <h1>Users Page</h1>

            <div className="button-container">
                <div>
                    <button className="go-button" onClick={() => navigate("/home")}>
                        Home
                    </button>
                </div>
            </div>

            <div className="list-container">
                {usersQuery.isLoading && <p>Loading...</p>}
                {usersQuery.isError && <p>Error: {usersQuery.error.message}</p>}
                <table>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Latest Login IP</th>
                            <th>Last Login</th>
                            <th>Created At</th>
                        </tr>
                    </thead>
                    <tbody>
                        {usersQuery.data?.users.items.map((user: User) => (
                            <tr key={user.id} className={selectedUser?.id === user.id ? "selected" : ""} onClick={() => handleRowClick(user)}>
                                <td>{user.name}</td>
                                <td>{user.email}</td>
                                <td>{user.latestLoginIp ?? "-"}</td>
                                <td>{DateUtil.formatDateTime(user.lastLoginDate)}</td>
                                <td>{DateUtil.formatDateTime(user.createdAt)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>

                {usersQuery.data?.users.items.length !== 0 && (
                    <div className="pagination-container">
                        <button onClick={() => setPage(page - 1)} disabled={page <= 1}>
                            Previous
                        </button>
                        {(() => {
                            const totalPages = usersQuery.data?.users.pageInfo.totalPages ?? 0;
                            const start = Math.max(1, Math.min(page - 2, totalPages - 4));
                            const end = Math.min(start + 4, totalPages);
                            return Array.from({ length: end - start + 1 }, (_, i) => {
                                const p = start + i;
                                return (
                                    <button key={p} className={p === page ? "active" : ""} onClick={() => setPage(p)} disabled={p === page}>
                                        {p}
                                    </button>
                                );
                            });
                        })()}
                        <button onClick={() => setPage(page + 1)} disabled={page >= usersQuery.data?.users.pageInfo.totalPages}>
                            Next
                        </button>
                    </div>
                )}
            </div>

            {selectedUser && (
                <div className="detail-container">
                    <div className="detail-header">
                        <h2>User Detail</h2>
                        <button className="close-button" onClick={handleCloseDetail}>
                            ✕
                        </button>
                    </div>
                    <div className="detail-form">
                        <div className="detail-field">
                            <label>Name</label>
                            <span>{selectedUser.name}</span>
                        </div>
                        <div className="detail-field">
                            <label>Email</label>
                            <span>{selectedUser.email}</span>
                        </div>
                        <div className="detail-field">
                            <label>Latest Login IP</label>
                            <span>{selectedUser.latestLoginIp ?? "-"}</span>
                        </div>
                        <div className="detail-field">
                            <label>Last Login</label>
                            <span>{DateUtil.formatDateTime(selectedUser.lastLoginDate)}</span>
                        </div>
                        <div className="detail-field">
                            <label>Created At</label>
                            <span>{DateUtil.formatDateTime(selectedUser.createdAt)}</span>
                        </div>
                        <div className="detail-field">
                            <label>Modified At</label>
                            <span>{DateUtil.formatDateTime(selectedUser.modifiedAt)}</span>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
