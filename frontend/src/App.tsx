import { useEffect, useState } from "react";

import "@/styles/app.scss";
import { Link } from "react-router-dom";

export default function App() {
    return (
        <div>
            <h1>Hello World</h1>
            <Link to="/login">Login</Link>
            <Link to="/users">Users</Link>
        </div>
    );
}
