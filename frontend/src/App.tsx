import { useEffect } from "react";

import "@/styles/app.scss";
import "@/styles/page.scss";

import { useAppDispatch } from "@/app/hooks";
import { initialState, setUser } from "@/features/user-slice";

import { useMe } from "@/hooks/useAuth";

import { Outlet } from "react-router-dom";

export default function App() {
    const dispatch = useAppDispatch();
    const { data, isLoading } = useMe();
    const user = data?.me;

    useEffect(() => {
        if (user) {
            dispatch(setUser({ id: user.id, email: user.email, name: user.name }));
        } else {
            dispatch(setUser(initialState));
        }
    }, [user, dispatch]);

    if (isLoading) {
        return <div>loading...</div>;
    }
    return <Outlet />;
}
