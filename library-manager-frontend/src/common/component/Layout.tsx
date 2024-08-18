import { Link, Outlet, useLocation } from "react-router-dom";
import { AppShell, Burger, Button, Group, Stack, Title } from "@mantine/core";
import { useDisclosure, useMediaQuery } from "@mantine/hooks";
import {
  IconBook,
  IconBookDownload,
  IconBooks,
  IconBookUpload,
  IconUser,
} from "@tabler/icons-react";
import { modals } from "@mantine/modals";
import { API_ENDPOINTS } from "../constant/endpoints";
import { HttpMethod } from "../service/axiosService";
import type { CreateLease, Lease } from "../type";
import useFetch from "../hook/useFetch";

export function Layout() {
  const [mobileOpened, { toggle: toggleMobile }] = useDisclosure();
  const [desktopOpened, { toggle: toggleDesktop }] = useDisclosure(true);
  const isSmallScreen = useMediaQuery("(max-width: 62em)");
  const location = useLocation();

  const getButtonVariant = (path: string) =>
    location.pathname === path ? "light" : "subtle";

  const leaseBook = useFetch<Lease>(HttpMethod.Post, "", false);
  const returnBook = useFetch<Lease>(HttpMethod.Post, "", false);

  const handleLeaseBook = () => {
    modals.openContextModal({
      modal: "lease",
      title: "Emprestar Livro",
      innerProps: {
        onSave: (createLease: CreateLease) => {
          return leaseBook.refetch(API_ENDPOINTS.LEASES.CREATE, createLease);
        },
      },
    });
  };

  const handleReturnBook = () => {
    modals.openContextModal({
      modal: "returnBook",
      title: "Retornar Livro",
      innerProps: {
        onSave: (values: { bookId: number }) => {
          return returnBook.refetch(API_ENDPOINTS.LEASES.RETURN(values.bookId));
        },
      },
    });
  };

  return (
    <AppShell
      header={{ height: 60 }}
      navbar={{
        width: 250,
        breakpoint: "sm",
        collapsed: { mobile: !mobileOpened, desktop: !desktopOpened },
      }}
      padding="md"
    >
      <AppShell.Header>
        <Group h="100%" px="md">
          <Burger
            lineSize={2}
            opened={mobileOpened}
            onClick={toggleMobile}
            hiddenFrom="sm"
            size="sm"
          />
          <Burger
            opened={desktopOpened}
            lineSize={2}
            onClick={toggleDesktop}
            visibleFrom="sm"
            size="sm"
          />
          <Group gap={6}>
            <IconBooks stroke={1} size={40} />
            <Title order={isSmallScreen ? 6 : 4} c="gray.9">
              Library Manager
            </Title>
          </Group>
          <Group ml="auto">
            <Button
              color="green"
              pr={isSmallScreen ? "0" : undefined}
              leftSection={<IconBookUpload />}
              onClick={handleLeaseBook}
            >
              {isSmallScreen ? null : "Emprestar Livro"}
            </Button>
            <Button
              pr={isSmallScreen ? "0" : undefined}
              leftSection={<IconBookDownload />}
              onClick={handleReturnBook}
            >
              {isSmallScreen ? null : "Retornar Livro"}
            </Button>
          </Group>
        </Group>
      </AppShell.Header>
      <AppShell.Navbar p="sm">
        <Stack dir={"column"} gap="xs">
          <Button
            justify="space-between"
            fullWidth
            component={Link}
            to="/books"
            variant={getButtonVariant("/books")}
            leftSection={<IconBook size={20} />}
            rightSection={<span />}
          >
            Livros
          </Button>
          <Button
            justify="space-between"
            fullWidth
            component={Link}
            to="/users"
            variant={getButtonVariant("/users")}
            leftSection={<IconUser size={20} />}
            rightSection={<span />}
          >
            Usuários
          </Button>
        </Stack>
      </AppShell.Navbar>
      <AppShell.Main>
        <Outlet />
      </AppShell.Main>
    </AppShell>
  );
}
