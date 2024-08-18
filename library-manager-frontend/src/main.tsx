import "@mantine/core/styles.css";
import "@mantine/dates/styles.css";
import "@mantine/notifications/styles.css";

import "dayjs/locale/pt-br";

import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { MantineProvider } from "@mantine/core";
import App from "./App.tsx";
import { Notifications } from "@mantine/notifications";
import { ModalsProvider } from "@mantine/modals";
import { BrowserRouter } from "react-router-dom";
import { theme } from "./theme.ts";
import { DatesProvider } from "@mantine/dates";
import dayjs from "dayjs";
import customParseFormat from "dayjs/plugin/customParseFormat";
import { LeaseBookFormModal, ReturnBookFormModal } from "./features/lease";

dayjs.extend(customParseFormat);

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <MantineProvider theme={theme}>
      <ModalsProvider
        modals={{ returnBook: ReturnBookFormModal, lease: LeaseBookFormModal }}
      >
        <DatesProvider settings={{ locale: "pt-br" }}>
          <Notifications />
          <BrowserRouter>
            <App />
          </BrowserRouter>
        </DatesProvider>
      </ModalsProvider>
    </MantineProvider>
  </StrictMode>,
);
