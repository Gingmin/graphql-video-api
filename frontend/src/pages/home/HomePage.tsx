import React, { useEffect, useId, useState } from "react";
import backImg from "@/assets/images/back.jpg";
// import netflixLogo from "@/assets/logo/Netflix_Logo_PMS.png";
import netflixLogo from "@/assets/logo/logo.svg";
import { LanguageDropdown, UiAnchorButton } from "@/ui";
import { useTranslation } from "react-i18next";
import { LanguageUtil } from "@/utils/language-util";

function HomePage() {
    const { t, i18n } = useTranslation();
    const emailId = useId();
    const [email, setEmail] = useState("");
    const [emailTouched, setEmailTouched] = useState(false);
    const [lang, setLang] = useState<"ko" | "en">("ko");

    const emailInvalid = emailTouched && email.trim().length > 0 && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim());

    const currentI18nLang = String(i18n?.resolvedLanguage ?? i18n?.language ?? "");

    useEffect(() => {
        if (currentI18nLang.includes("ko")) {
            setLang("ko");
        } else {
            setLang("en");
        }
    }, [currentI18nLang]);

    useEffect(() => {
        console.log(lang);
        LanguageUtil.changeLanguage(lang);
    }, [lang]);

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (emailInvalid) {
            return;
        }
        console.log(email);
    };

    return (
        <div className="basic-layout">
            <div className="gingflix-sans-font-loaded">
                <div className="home-page">
                    <div className="home-wrap">
                        <div className="gradient-area">
                            <div className=""></div>
                            <div className=""></div>
                        </div>

                        <div className="home-wrap-header">
                            <div className="header-inner">
                                <div className="header-main">
                                    <header
                                        style={
                                            {
                                                "--wct--layout-container--alignItems": "center",
                                                "--wct--layout-container--columnSpacing": "16px",
                                                "--wct--layout-container--flexDirection": "row",
                                                "--wct--layout-container--justifyContent": "space-between",
                                                "--wct--layout-container--padding": "0px",
                                                "--wct--layout-container--rowSpacing": "0px",
                                                "--wct--layout-container--width": "calc(100% + 16px)",
                                            } as React.CSSProperties
                                        }
                                    >
                                        <div
                                            className="header-main-left"
                                            style={
                                                {
                                                    "--wct--layout-item--flex-xs": "0 auto",
                                                    "--wct--layout-item--flex-s": "0 0 calc(41.66666666666667% - 16px)",
                                                    "--wct--layout-item--flex-m": "0 0 calc(33.333333333333336% - 16px)",
                                                    "--wct--layout-item--flex-l": "0 0 calc(33.333333333333336% - 16px)",
                                                    "--wct--layout-item--flex-xl": "0 0 calc(33.333333333333336% - 16px)",
                                                    "--wct--layout-item--flex-xxl": "0 0 calc(33.333333333333336% - 16px)",
                                                    "--wct--layout-item--flex-xxxl": "0 0 calc(33.333333333333336% - 16px)",
                                                    "--wct--layout-item--padding": "0px",
                                                    "--wct--layout-item--width-xs": "auto",
                                                } as React.CSSProperties
                                            }
                                        >
                                            <div className="logo">
                                                <span>
                                                    <img src={netflixLogo} alt="Netflix" />
                                                </span>
                                            </div>
                                        </div>
                                        <div
                                            className="header-main-right"
                                            style={
                                                {
                                                    "--wct--layout-item--flex-xs": "0 auto",
                                                    "--wct--layout-item--flex-s": "0 0 calc(58.333333333333336% - 16px)",
                                                    "--wct--layout-item--flex-m": "0 0 calc(66.66666666666667% - 16px)",
                                                    "--wct--layout-item--flex-l": "0 0 calc(66.66666666666667% - 16px)",
                                                    "--wct--layout-item--flex-xl": "0 0 calc(66.66666666666667% - 16px)",
                                                    "--wct--layout-item--flex-xxl": "0 0 calc(66.66666666666667% - 16px)",
                                                    "--wct--layout-item--flex-xxxl": "0 0 calc(66.66666666666667% - 16px)",
                                                    "--wct--layout-item--justifyContent": "flex-end",
                                                    "--wct--layout-item--padding": "0px",
                                                    "--wct--layout-item--width-xs": "auto",
                                                } as React.CSSProperties
                                            }
                                        >
                                            <div className="right-area">
                                                <div
                                                    className="button-container"
                                                    style={
                                                        {
                                                            "--wct--layout-container--columnSpacing": "12px",
                                                            "--wct--layout-container--flexDirection": "row",
                                                            "--wct--layout-container--justifyContent": "flex-end",
                                                            "--wct--layout-container--padding": "3px",
                                                            "--wct--layout-container--rowSpacing": "0px",
                                                            "--wct--layout-container--width": "calc(100% + 12px)",
                                                        } as React.CSSProperties
                                                    }
                                                >
                                                    <div className="">
                                                        <LanguageDropdown
                                                            value={lang}
                                                            onChange={(v) => setLang(v === "en" ? "en" : "ko")}
                                                            options={[
                                                                { value: "ko", label: "한국어" },
                                                                { value: "en", label: "English" },
                                                            ]}
                                                        />
                                                    </div>
                                                    <div className="button-container-right">
                                                        <UiAnchorButton href="/login">로그인</UiAnchorButton>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </header>
                                </div>
                            </div>
                        </div>

                        <div className="home-wrap-content">
                            <div className="home-wrap-content-inner">
                                <div className="content-back">
                                    <div className="content-back-wrap">
                                        <div dir="ltr"></div>
                                        <img src={backImg} alt="back" srcSet={backImg} aria-hidden={true} />
                                    </div>
                                </div>
                                <div className="content-main">
                                    <div className="content-area">
                                        <div className="text-container">
                                            <div className="">
                                                <h1>영화, 시리즈 등을 무제한으로</h1>
                                                <p>7,000원으로 시작하세요. 멤버십은 언제든지 해지 가능합니다.</p>
                                            </div>
                                            <div className="">
                                                <div className="form-container">
                                                    <form aria-label="넷플릭스에 가입하거나 멤버십을 재시작하세요." onSubmit={handleSubmit}>
                                                        <h3>시청할 준비가 되셨나요? 멤버십을 등록하거나 재시작하려면 이메일 주소를 입력하세요.</h3>
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
                                                                    <label htmlFor={emailId}>이메일 주소</label>
                                                                    <div className="input-help"></div>
                                                                </div>
                                                            </div>
                                                            <button type="submit">
                                                                <span>시작하기</span>
                                                                <span className="btn-arrow" aria-hidden={true}>
                                                                    <svg viewBox="0 0 24 24" focusable="false" aria-hidden="true">
                                                                        <path d="M9 5l8 7-8 7" />
                                                                    </svg>
                                                                </span>
                                                            </button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="content-footer">
                                        <div className="curve-container">
                                            <div className="curve-1"></div>
                                            <div className="curve-2"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default HomePage;
