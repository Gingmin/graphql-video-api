import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children, permissions }: { children: React.ReactNode; permissions: string[] }) => {
    const isAuthenticated = localStorage.getItem("token"); // 또는 인증 상태 확인

    if (!isAuthenticated) {
        // return <Navigate to="/login" replace />;
    }

    return children;
};

export default ProtectedRoute;
