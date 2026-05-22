export const DateUtil = {
    formatDateTime: (iso?: string): string => {
        if (!iso) {
            return "";
        }
        const d = new Date(iso);
        const pad = (n: number) => String(n).padStart(2, "0");

        const year = d.getFullYear();
        const month = pad(d.getMonth() + 1);
        const day = pad(d.getDate());
        const hours = pad(d.getHours());
        const minutes = pad(d.getMinutes());
        const seconds = pad(d.getSeconds());
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
};
