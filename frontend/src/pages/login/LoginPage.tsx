import React, { useRef, useState } from "react";

import { useLogin } from "@/hooks/useAuth";
import { useNavigate } from "react-router-dom";

function LoginPage() {
    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const emailInputRef = useRef<HTMLInputElement>(null);
    const passwordInputRef = useRef<HTMLInputElement>(null);

    const loginMutation = useLogin(() => {
        navigate("/users");
    });

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const formData = new FormData(e.target as HTMLFormElement);
        // const email = formData.get("email") as string;
        // const password = formData.get("password") as string;
        const _email = email;
        const _password = password;
        console.log(_email, _password);

        if (_email === "") {
            emailInputRef.current?.focus();
            return;
        }
        if (_password === "") {
            passwordInputRef.current?.focus();
            return;
        }

        loginMutation.mutate({ email: _email, password: _password });
    };
    return (
        <div className="common-page">
            <h1>Login Page</h1>
            <form onSubmit={handleSubmit}>
                <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
                <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
                <button type="submit">Login</button>
            </form>
            <button onClick={() => navigate("/signup")}>Sign Up</button>
        </div>
    );
}

export default LoginPage;
