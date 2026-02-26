import { createSlice, type PayloadAction } from "@reduxjs/toolkit";

interface UserState {
    id: string;
    email: string;
    name: string;
}

export const initialState: UserState = {
    id: "",
    email: "",
    name: "",
};

const userSlice = createSlice({
    name: "user",
    initialState,
    reducers: {
        setUser: (state, action: PayloadAction<UserState>) => {
            state.id = action.payload.id;
            state.email = action.payload.email;
            state.name = action.payload.name;
        },
    },
});

export const { setUser } = userSlice.actions;
export default userSlice.reducer;
