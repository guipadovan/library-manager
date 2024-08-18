import { Navigate } from "react-router-dom";
import { BookListView } from "./features/book";
import { Layout } from "./common/component/Layout.tsx";
import { UserListView } from "./features/user";

export const routes = [
  {
    path: "/",
    element: <Layout />,
    children: [
      {
        path: "/",
        element: <Navigate to="/books" />,
      },
      {
        path: "/books",
        element: <BookListView />,
      },
      {
        path: "/users",
        element: <UserListView />,
      },
    ],
  },
  {
    path: "*",
    element: <Navigate to="/" />,
  },
];
