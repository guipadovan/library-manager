import {
  ActionIcon,
  Button,
  Center,
  Group,
  Stack,
  Table,
  Text,
  Title,
  Tooltip,
} from "@mantine/core";
import { IconEdit, IconTrash } from "@tabler/icons-react";
import type { User } from "../../../common/type";
import dayjs from "dayjs";
import { useState } from "react";

interface UserTableProps {
  data: User[];
  onEditUserLoading: boolean;
  onEditUser: (user: User) => void;
  onDeleteUserLoading: boolean;
  onDeleteUser: (user: User) => void;
  onGenerateRecommendationsLoading: boolean;
  onGenerateRecommendations: (user: User) => void;
}

export function UserTable({
  data,
  onEditUserLoading,
  onEditUser,
  onDeleteUserLoading,
  onDeleteUser,
  onGenerateRecommendationsLoading,
  onGenerateRecommendations,
}: UserTableProps) {
  const [loadingUser, setLoadingUser] = useState<number | undefined>();

  return data.length === 0 ? (
    <Center>
      <Stack align="center" gap="0" mt={16}>
        <Title order={2}>Nenhum usuário encontrado</Title>
        <Text fz={18} c="gray.8">
          Clique no botão + para adicionar um novo usuário
        </Text>
      </Stack>
    </Center>
  ) : (
    <Table.ScrollContainer minWidth={800}>
      <Table
        striped
        highlightOnHover
        stickyHeader
        verticalSpacing="sm"
        horizontalSpacing="md"
      >
        <Table.Thead>
          <Table.Tr>
            <Table.Th>ID</Table.Th>
            <Table.Th>Nome</Table.Th>
            <Table.Th>Email</Table.Th>
            <Table.Th>Data de Registro</Table.Th>
            <Table.Th>Telefone</Table.Th>
            <Table.Th ta="end">Ações</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {data.map((user) => (
            <Table.Tr key={user.id}>
              <Table.Td>{user.id}</Table.Td>
              <Table.Td>{user.name}</Table.Td>
              <Table.Td>{user.email}</Table.Td>
              <Table.Td>
                {dayjs(user.registrationDate).format("DD/MM/YYYY")}
              </Table.Td>
              <Table.Td>{user.phone}</Table.Td>
              <Table.Td>
                <Group justify={"end"} miw={255} gap={4}>
                  <Button
                    size="compact-lg"
                    fz={14}
                    fw={500}
                    loading={
                      onGenerateRecommendationsLoading &&
                      loadingUser === user.id
                    }
                    onClick={() => {
                      onGenerateRecommendations(user);
                      setLoadingUser(user.id);
                    }}
                  >
                    Gerar recomendações
                  </Button>
                  <ActionIcon.Group style={{ justifyContent: "flex-end" }}>
                    <Tooltip label="Editar" position="top">
                      <ActionIcon
                        size="lg"
                        loading={onEditUserLoading && loadingUser === user.id}
                        onClick={() => {
                          onEditUser(user);
                          setLoadingUser(user.id);
                        }}
                      >
                        <IconEdit size={16} />
                      </ActionIcon>
                    </Tooltip>
                    <Tooltip label="Excluir" position="top">
                      <ActionIcon
                        color="red"
                        size="lg"
                        loading={onDeleteUserLoading && loadingUser === user.id}
                        onClick={() => {
                          onDeleteUser(user);
                          setLoadingUser(user.id);
                        }}
                      >
                        <IconTrash size={16} />
                      </ActionIcon>
                    </Tooltip>
                  </ActionIcon.Group>
                </Group>
              </Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>
    </Table.ScrollContainer>
  );
}
