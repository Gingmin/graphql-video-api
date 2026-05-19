import { useRef, useState } from "react";

import { useNavigate } from "react-router-dom";
import { useQueryClient, useMutation } from "@tanstack/react-query";
import { gqlClient } from "@/lib/graphql/client";
import { gql } from "graphql-request";

export default function SignUpPage() {
    const navigate = useNavigate();

    const queryClient = useQueryClient();
    const useSignUp = useMutation({
        mutationFn: (vars: { name: string; email: string; password: string }) => {
            return gqlClient.request(
                gql`
                    mutation SignUp($name: String!, $email: String!, $password: String!) {
                        signUp(name: $name, email: $email, password: $password) {
                            id
                            email
                            name
                        }
                    }
                `,
                vars,
            );
        },
        onSuccess: () => {
            queryClient.clear();

            navigate("/login");
        },
        onError: (error) => {
            console.error(error);
        },
    });

    const [name, setName] = useState("");
    // const [nameTouched, setNameTouched] = useState(false);
    // const nameInvalid = nameTouched && name.trim().length > 0 && name.trim().length < 2;
    const nameInputRef = useRef<HTMLInputElement>(null);

    const [email, setEmail] = useState("");
    // const emailTouched, setEmailTouched] = useState(false);
    // const emailInvalid = emailTouched && email.trim().length > 0 && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim());
    const emailInputRef = useRef<HTMLInputElement>(null);

    const [password, setPassword] = useState("");
    // const passwordTouched, setPasswordTouched] = useState(false);
    // const passwordInvalid = passwordTouched && password.trim().length > 0 && password.trim().length < 8;
    const passwordInputRef = useRef<HTMLInputElement>(null);

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const _name = name;
        const _email = email;
        const _password = password;

        if (_name === "") {
            nameInputRef.current?.focus();
            return;
        }
        if (_email === "") {
            emailInputRef.current?.focus();
            return;
        }
        if (_password === "") {
            passwordInputRef.current?.focus();
            return;
        }

        useSignUp.mutate({ name: _name, email: _email, password: _password });
    };

    return (
        <div className="common-page">
            <h1>Sign Up Page</h1>
            <form onSubmit={handleSubmit}>
                <input type="text" placeholder="Name" value={name} onChange={(e) => setName(e.target.value)} ref={nameInputRef} />
                <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} ref={emailInputRef} />
                <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} ref={passwordInputRef} />
                <button type="submit">Sign Up</button>
            </form>
            <button type="button" onClick={() => navigate("/login")}>
                Login
            </button>
        </div>
    );
}
