import { configureStore } from "@reduxjs/toolkit";

import * as reducers from "@/features";

export const store = configureStore({
    reducer: reducers,
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
