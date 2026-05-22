export const ErrorUtil = {
    errorHandler: (error: any) => {
        const msg = error.response?.errors?.[0]?.message ?? "알 수 없는 오류";
        alert(msg);
    },
};
