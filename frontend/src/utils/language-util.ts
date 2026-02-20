import i18n from "i18next";
import { useTranslation } from "react-i18next";

export const LanguageUtil = {
    changeLanguage: (language: string) => {
        i18n.changeLanguage(language);
    },
};

export const useTranslationText = (text: string = "", textEng: string = "", isForce: boolean = false) => {
    const { t, i18n } = useTranslation();

    if (isForce) {
        return i18n.language === "ko" ? text : textEng;
    }

    return t(text);
};
