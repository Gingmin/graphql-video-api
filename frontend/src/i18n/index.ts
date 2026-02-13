import i18n from "i18next";
import { initReactI18next } from "react-i18next";

import ko from "./ko";
import en from "./en";

import LanguageDetector from "i18next-browser-languagedetector";

export const detectBrowserLanguage = (): string => {
    const browserLang = navigator.language.toLowerCase();
    if (browserLang.startsWith("ko")) {
        return "ko";
    }
    return "en";
};

export const setLanguage = (language?: string) => {
    let targetLanguage: string;

    if (language) {
        targetLanguage = language;
    } else {
        targetLanguage = detectBrowserLanguage();
    }

    if (["ko", "en"].includes(targetLanguage)) {
        i18n.changeLanguage(targetLanguage);
    } else {
        i18n.changeLanguage("en");
    }
};

i18n.use(LanguageDetector)
    .use(initReactI18next)
    .init({
        resources: {
            ko: {
                translation: ko,
            },
            en: {
                translation: en,
            },
            "ko-KR": {
                translation: ko,
            },
            "en-US": {
                translation: en,
            },
        },
        lng: detectBrowserLanguage(),
        fallbackLng: "en",
        interpolation: {
            escapeValue: false,
        },
        detection: {
            order: ["navigator", "localStorage", "querystring", "cookie", "htmlTag"],
            caches: [],
        },
    });
export default i18n;
