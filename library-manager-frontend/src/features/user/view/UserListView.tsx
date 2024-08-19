import { useEffect, useMemo, useState } from "react";
import { UserFormModal } from "../component/UserFormModal";
import type { Book, User } from "../../../common/type";
import {
  ActionIcon,
  Card,
  Center,
  Container,
  Group,
  LoadingOverlay,
  Pagination,
  Stack,
  Text,
  Title,
  Tooltip,
} from "@mantine/core";
import usePaginatedFetch from "../../../common/hook/usePaginatedFetch";
import { API_ENDPOINTS } from "../../../common/constant/endpoints";
import { UserTable } from "../component/UserTable";
import { IconPlus } from "@tabler/icons-react";
import { HttpMethod } from "../../../common/service/axiosService";
import useFetch from "../../../common/hook/useFetch";
import { modals } from "@mantine/modals";
import { BookTable } from "../../book";
import { notifications } from "@mantine/notifications";

export function UserListView() {
  const users = usePaginatedFetch<User>(API_ENDPOINTS.USERS.GET);
  const saveUser = useFetch<User>(HttpMethod.Post, "", false);
  const updateUser = useFetch<User>(HttpMethod.Put, "", false);
  const deleteUser = useFetch<boolean>(HttpMethod.Delete, "", false);
  const generateRecommendations = useFetch<Book[]>(HttpMethod.Get, "", false);

  const [modalOpened, setModalOpened] = useState(false);
  const [userToEdit, setUserToEdit] = useState<User | null>(null);

  const confirmDeleteUser = (userId: number) =>
    modals.openConfirmModal({
      title: "Confirmar exclusão",
      children: "Tem certeza de que deseja excluir este usuário?",
      labels: { confirm: "Excluir", cancel: "Cancelar" },
      onConfirm: () =>
        deleteUser
          .refetch(API_ENDPOINTS.USERS.DELETE(userId), null)
          .then(() => {
            void users.refetch();
          }),
    });

  const [recommendedUser, setRecommendedUser] = useState<User | null>(null);

  const handleGenerateRecommendations = async (user: User) => {
    void generateRecommendations
      .refetch(API_ENDPOINTS.RECOMMENDATIONS.GET(user.id))
      .then(() => {
        setRecommendedUser(user);
      });
  };

  useEffect(() => {
    if (generateRecommendations.error !== null) {
      notifications.show({
        title: "Erro ao buscar recomendações",
        message: `Erro ao buscar recomendações para ${recommendedUser?.name}`,
        color: "red",
        position: "top-right",
      });
    } else {
      const { data } = generateRecommendations;
      if ((data === null || data.length === 0) && recommendedUser !== null) {
        notifications.show({
          title: "Nenhuma recomendação encontrada",
          message: `Não existem livros recomendados para ${recommendedUser?.name}`,
          color: "yellow",
          position: "top-right",
        });
      } else {
        if (data !== null && recommendedUser !== null)
          modals.open({
            size: "xl",
            title: `Recomendações para ${recommendedUser?.name}`,
            children: (
              <BookTable
                data={data}
                showActions={false}
                onEditBookLoading={false}
                onEditBook={() => {}}
                onDeleteBookLoading={false}
                onDeleteBook={() => {}}
              />
            ),
          });
      }
    }
    setRecommendedUser(null);
  }, [generateRecommendations.data, generateRecommendations.error]);

  const handleAddUser = () => {
    setUserToEdit(null);
    setModalOpened(true);
  };

  const handleEditUser = (user: User) => {
    setUserToEdit(user);
    setModalOpened(true);
  };

  const handleSaveUser = (user: User) => {
    const endpoint =
      user.id === 0
        ? API_ENDPOINTS.USERS.CREATE
        : API_ENDPOINTS.USERS.UPDATE(user.id);
    const fetchMethod = user.id === 0 ? saveUser : updateUser;

    return fetchMethod.refetch(endpoint, user);
  };

  // Calcula a altura mínima do Card com base no número de livros
  const minHeight = useMemo(
    () =>
      `calc(${(users.data ? users.data.content.length : 0) * 60}px + 100px)`,
    [users.data],
  );

  return (
    <>
      <Container fluid maw="1440px">
        <Card shadow="xs" padding="lg" style={{ minHeight: minHeight }}>
          <LoadingOverlay
            visible={users.loading}
            zIndex={5}
            overlayProps={{ radius: "sm", blur: 2 }}
          />
          <Card.Section withBorder inheritPadding py="xs">
            <Group justify="space-between">
              <Title order={3} ta="center">
                Usuários
              </Title>
              <Tooltip label="Adicionar Usuário" position="bottom">
                <ActionIcon
                  size="lg"
                  bg="green"
                  aria-label="Adicionar Usuário"
                  variant="success"
                  onClick={handleAddUser}
                >
                  <IconPlus size={28} />
                </ActionIcon>
              </Tooltip>
            </Group>
          </Card.Section>
          {users.error && (
            <Center>
              <Stack gap={4} align="center">
                <Title order={3} mt={16}>
                  Erro ao carregar página
                </Title>
                <Text c="red" fz={16}>
                  {users.error.message}
                </Text>
              </Stack>
            </Center>
          )}
          {!users.loading && !users.error && users.data && (
            <>
              <Card.Section>
                <UserTable
                  data={users.data.content}
                  onEditUserLoading={saveUser.loading || updateUser.loading}
                  onEditUser={handleEditUser}
                  onDeleteUserLoading={deleteUser.loading}
                  onDeleteUser={(user: User) => confirmDeleteUser(user.id)}
                  onGenerateRecommendationsLoading={
                    generateRecommendations.loading
                  }
                  onGenerateRecommendations={handleGenerateRecommendations}
                />
              </Card.Section>
              <Center>
                <Pagination
                  total={users.data.page.totalPages}
                  value={users.currentPage}
                  onChange={(page) => users.setCurrentPage(page)}
                  mt="md"
                />
              </Center>
            </>
          )}
        </Card>
      </Container>
      <UserFormModal
        opened={modalOpened}
        refetch={users.refetch}
        onClose={() => setModalOpened(false)}
        onSave={handleSaveUser}
        userToEdit={userToEdit || undefined}
      />
    </>
  );
}
