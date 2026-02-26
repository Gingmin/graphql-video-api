import { Navigate } from "react-router-dom";

import { useAppSelector } from "@/app/hooks";

const ProtectedRoute = ({ children, permissions }: { children: React.ReactNode; permissions: string[] }) => {
    const userId = useAppSelector((state) => state.userSlice.id);

    if (!userId) {
        return <Navigate to="/login" replace />;
    }

    return children;
};

export default ProtectedRoute;
