import { createSlice, type PayloadAction } from "@reduxjs/toolkit";

interface UserState {
    id: string;
    name: string;
}

const initialState: UserState = {
    id: "",
    name: "",
};

const userSlice = createSlice({
    name: "user",
    initialState,
    reducers: {
        setUser: (state, action: PayloadAction<UserState>) => {
            state.id = action.payload.id;
            state.name = action.payload.name;
        },
    },
});

export const { setUser } = userSlice.actions;
export default userSlice.reducer;
