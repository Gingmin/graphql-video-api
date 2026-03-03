import React, { useEffect, useId, useRef, useState } from "react";

import { useLogin } from "@/hooks/useAuth";
import { useLocation, useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import netflixLogo from "@/assets/logo/logo.svg";
import { UiButton } from "@/ui/Buttons";

function LoginPage() {
    const navigate = useNavigate();
    const { t } = useTranslation();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const emailInputRef = useRef<HTMLInputElement>(null);
    const passwordInputRef = useRef<HTMLInputElement>(null);

    const emailId = useId();
    const passwordId = useId();
    const [emailTouched, setEmailTouched] = useState(false);
    const emailInvalid = emailTouched && email.trim().length > 0 && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim());

    const { state } = useLocation();

    useEffect(() => {
        if (state) {
            setEmail(state.email);
        }
    }, [state]);

    const loginMutation = useLogin(() => {
        navigate("/users");
    });

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const _email = email;
        const _password = password;

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
        <div className="login-page">
            <div className="header-container">
                <header>
                    <a href="/">
                        <img src={netflixLogo} alt="Netflix" />
                    </a>
                </header>
            </div>
            <div className="form-container">
                <div className="">
                    <form aria-label="넷플릭스에 가입하거나 멤버십을 재시작하세요." onSubmit={handleSubmit}>
                        <h1>{t("loginInfo")}</h1>
                        <h2>{t("newAccount")}</h2>
                        <div className="form-input-container">
                            <div className="input-wrap">
                                <div className="input-control" data-invalid={emailInvalid ? "true" : "false"}>
                                    <input
                                        id={emailId}
                                        autoComplete="email"
                                        minLength={5}
                                        maxLength={50}
                                        type="email"
                                        dir="ltr"
                                        name="email"
                                        value={email}
                                        placeholder=" "
                                        onChange={(e) => setEmail(e.target.value)}
                                        onBlur={() => setEmailTouched(true)}
                                        aria-invalid={emailInvalid}
                                    />
                                    <label htmlFor={emailId}>{t("emailAddress")}</label>
                                    <div className="input-help"></div>
                                </div>
                            </div>
                        </div>
                        <div className="form-input-container">
                            <div className="input-wrap">
                                <div className="input-control">
                                    <input
                                        id={passwordId}
                                        autoComplete="password"
                                        minLength={5}
                                        maxLength={50}
                                        type="password"
                                        dir="ltr"
                                        name="password"
                                        value={password}
                                        placeholder=" "
                                        onChange={(e) => setPassword(e.target.value)}
                                    />
                                    <label htmlFor={passwordId}>{t("password")}</label>
                                </div>
                            </div>
                        </div>
                    </form>
                    <div className="button-container">
                        <div>
                            <UiButton type="submit" onClick={(e) => handleSubmit(e as unknown as React.FormEvent<HTMLFormElement>)}>
                                {t("next")}
                            </UiButton>
                        </div>
                        <div>
                            <UiButton onClick={() => navigate("/signup")}>{t("signUp")}</UiButton>
                        </div>
                    </div>
                </div>
            </div>
            <div className="footer-container">
                <div className="footer-area">
                    <footer>
                        <div>
                            <div className="footer-links">
                                <div>{t("questionPhone")}</div>
                                <div>{t("frequentlyAskedQuestions")}</div>
                                <div>{t("businessRegistrationNumber")}: 000-00-00000</div>
                            </div>
                        </div>
                    </footer>
                </div>
            </div>
        </div>
        // <div className="login-page">
        //     <h1>Login Page</h1>
        //     <form onSubmit={handleSubmit}>
        //         <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
        //         <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
        //         <button type="submit">Login</button>
        //     </form>
        //     <button onClick={() => navigate("/signup")}>Sign Up</button>
        // </div>
    );
}

export default LoginPage;
