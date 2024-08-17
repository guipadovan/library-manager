import { Navigate } from "react-router-dom";

export const routes = [
  {
    path: "/books",
    element: <div>Books</div>,
  },
  {
    path: "/users",
    element: <div>Users</div>,
  },
  {
    path: "/",
    element: <Navigate to="/books" />,
  },
];
