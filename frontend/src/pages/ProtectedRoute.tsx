import { Navigate } from "react-router-dom";

import { useMe } from "@/hooks/useAuth";

const ProtectedRoute = ({ children, permissions }: { children: React.ReactNode; permissions: string[] }) => {
    const { data, isLoading } = useMe();
    if (isLoading) {
        return <div>loading...</div>;
    }
    if (!data?.me) {
        return <Navigate to="/login" replace />;
    }

    return children;
};

export default ProtectedRoute;
