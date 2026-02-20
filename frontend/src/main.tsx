import { Provider } from "react-redux";
import { createRoot } from "react-dom/client";

import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { RouterProvider } from "react-router-dom";

import { store } from "@/app/store";
import router from "@/pages/page-config";

import "@/i18n";

const queryClient = new QueryClient();

createRoot(document.getElementById("root")!).render(
    <Provider store={store}>
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router} />
            <ReactQueryDevtools initialIsOpen={false} />
        </QueryClientProvider>
    </Provider>,
);
