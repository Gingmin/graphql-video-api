import React from "react";

type CommonProps = {
    variant?: "primary";
    className?: string;
    children: React.ReactNode;
};

function cls(base: string, className?: string) {
    return className ? `${base} ${className}` : base;
}

export function UiAnchorButton(props: CommonProps & React.AnchorHTMLAttributes<HTMLAnchorElement>) {
    const { variant = "primary", className, children, ...rest } = props;
    return (
        <a {...rest} className={cls(`ui-btn ui-btn--${variant}`, className)}>
            {children}
        </a>
    );
}

export function UiButton(props: CommonProps & React.ButtonHTMLAttributes<HTMLButtonElement>) {
    const { variant = "primary", className, children, ...rest } = props;
    return (
        <button {...rest} className={cls(`ui-btn ui-btn--${variant}`, className)}>
            {children}
        </button>
    );
}

