import { useEffect } from "react";

import "@/styles/app.scss";

import { useAppDispatch } from "@/app/hooks";
import { setUser } from "@/features/user-slice";

import { useMe } from "@/hooks/useAuth";

import HomePage from "@/pages/home/HomePage";

export default function App() {
    const dispatch = useAppDispatch();
    const { data, isLoading } = useMe();
    const user = data?.me;

    useEffect(() => {
        if (user) {
            dispatch(setUser({ id: user.id, email: user.email, name: user.name }));
        }
    }, [user, dispatch]);

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
