import {
  ActionIcon,
  Center,
  Stack,
  Table,
  Text,
  Title,
  Tooltip,
} from "@mantine/core";
import { IconEdit, IconTrash } from "@tabler/icons-react";
import type { Book } from "../../../common/type";
import dayjs from "dayjs";
import { ReactNode, useState } from "react";

interface ActionRendererProps {
  book: Book;
}

interface BookTableProps {
  data: Book[];
  onEditBookLoading: boolean;
  onEditBook: (book: Book) => void;
  onDeleteBookLoading: boolean;
  onDeleteBook: (book: Book) => void;
  actionRenderer?: (props: ActionRendererProps) => ReactNode;
  showIds?: boolean;
  showActions?: boolean;
}

export function BookTable({
  data,
  onEditBookLoading,
  onEditBook,
  onDeleteBookLoading,
  onDeleteBook,
  actionRenderer,
  showIds = true,
  showActions = true,
}: BookTableProps) {
  const [loadingBook, setLoadingBook] = useState<number | undefined>();

  return data.length === 0 ? (
    <Center>
      <Stack align="center" gap="0" mt={16}>
        <Title order={2}>Nenhum livro encontrado</Title>
        <Text fz={18} c="gray.8">
          Clique no botão + para adicionar um novo livro
        </Text>
      </Stack>
    </Center>
  ) : (
    <Table.ScrollContainer minWidth={600}>
      <Table
        striped
        highlightOnHover
        stickyHeader
        verticalSpacing="sm"
        horizontalSpacing="md"
      >
        <Table.Thead>
          <Table.Tr>
            {showIds && <Table.Th>ID</Table.Th>}
            <Table.Th>Título</Table.Th>
            <Table.Th>Autor</Table.Th>
            <Table.Th>Data de Publicação</Table.Th>
            <Table.Th>Categoria</Table.Th>
            {showActions && <Table.Th ta="end">Ações</Table.Th>}
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {data.map((book) => (
            <Table.Tr key={book.id}>
              {showIds && <Table.Td>{book.id}</Table.Td>}
              <Table.Td>{book.title}</Table.Td>
              <Table.Td>{book.author}</Table.Td>
              <Table.Td>
                {dayjs(book.publicationDate).format("DD/MM/YYYY")}
              </Table.Td>
              <Table.Td>{book.category}</Table.Td>
              {showActions && (
                <Table.Td>
                  {actionRenderer ? (
                    actionRenderer({ book })
                  ) : (
                    <ActionIcon.Group style={{ justifyContent: "flex-end" }}>
                      <Tooltip label="Editar" position="top">
                        <ActionIcon
                          size="lg"
                          loading={onEditBookLoading && loadingBook === book.id}
                          onClick={() => {
                            onEditBook(book);
                            setLoadingBook(book.id);
                          }}
                        >
                          <IconEdit size={16} />
                        </ActionIcon>
                      </Tooltip>
                      <Tooltip label="Excluir" position="top">
                        <ActionIcon
                          color="red"
                          size="lg"
                          loading={
                            onDeleteBookLoading && loadingBook === book.id
                          }
                          onClick={() => {
                            onDeleteBook(book);
                            setLoadingBook(book.id);
                          }}
                        >
                          <IconTrash size={16} />
                        </ActionIcon>
                      </Tooltip>
                    </ActionIcon.Group>
                  )}
                </Table.Td>
              )}
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>
    </Table.ScrollContainer>
  );
}
