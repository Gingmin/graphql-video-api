import { Navigate } from "react-router-dom";

import { useAppSelector } from "@/app/hooks";

const PublicOnlyRoute = ({ children }: { children: React.ReactNode }) => {
    const userId = useAppSelector((state) => state.userSlice.id);

    if (userId) {
        return <Navigate to="/home" replace />;
    }

    return children;
};

export default PublicOnlyRoute;
