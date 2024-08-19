import { useMemo, useState } from "react";
import { BookFormModal } from "../component/BookFormModal";
import type { Book } from "../../../common/type";
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
import { BookTable } from "../component/BookTable";
import { IconPlus } from "@tabler/icons-react";
import { HttpMethod } from "../../../common/service/axiosService";
import useFetch from "../../../common/hook/useFetch";
import { modals } from "@mantine/modals";

export function BookListView() {
  const books = usePaginatedFetch<Book>(API_ENDPOINTS.BOOKS.GET);
  const saveBook = useFetch<Book>(HttpMethod.Post, "", false);
  const updateBook = useFetch<Book>(HttpMethod.Put, "", false);
  const deleteBook = useFetch<boolean>(HttpMethod.Delete, "", false);

  const [modalOpened, setModalOpened] = useState(false);
  const [bookToEdit, setBookToEdit] = useState<Book | null>(null);

  const confirmDeleteBook = (bookId: number) =>
    modals.openConfirmModal({
      title: "Confirmar exclusão",
      children: "Tem certeza de que deseja excluir este livro?",
      labels: { confirm: "Excluir", cancel: "Cancelar" },
      onConfirm: () => {
        deleteBook
          .refetch(API_ENDPOINTS.BOOKS.DELETE(bookId), null)
          .then(() => {
            void books.refetch();
          });
      },
    });

  const handleAddBook = () => {
    setBookToEdit(null);
    setModalOpened(true);
  };

  const handleEditBook = (book: Book) => {
    setBookToEdit(book);
    setModalOpened(true);
  };

  const handleSaveBook = (book: Book) => {
    const endpoint =
      book.id === 0
        ? API_ENDPOINTS.BOOKS.CREATE
        : API_ENDPOINTS.BOOKS.UPDATE(book.id);
    const fetchMethod = book.id === 0 ? saveBook : updateBook;

    return fetchMethod.refetch(endpoint, book);
  };

  // Calcula a altura mínima do Card com base no número de livros
  const minHeight = useMemo(
    () =>
      `calc(${(books.data ? books.data.content.length : 0) * 60}px + 100px)`,
    [books.data],
  );

  return (
    <>
      <Container fluid maw="1440px">
        <Card shadow="xs" padding="lg" style={{ minHeight: minHeight }}>
          <LoadingOverlay
            visible={books.loading}
            zIndex={5}
            overlayProps={{ radius: "sm", blur: 2 }}
          />
          <Card.Section withBorder inheritPadding py="xs">
            <Group justify="space-between">
              <Title order={3} ta="center">
                Livros
              </Title>
              <Tooltip label="Adicionar Livro" position="bottom">
                <ActionIcon
                  size="lg"
                  bg="green"
                  aria-label="Adicionar Livro"
                  variant="success"
                  onClick={handleAddBook}
                >
                  <IconPlus size={28} />
                </ActionIcon>
              </Tooltip>
            </Group>
          </Card.Section>
          {books.error && (
            <Center>
              <Stack gap={4} align="center">
                <Title order={3} mt={16}>
                  Erro ao carregar página
                </Title>
                <Text c="red" fz={16}>
                  {books.error.message}
                </Text>
              </Stack>
            </Center>
          )}
          {!books.loading && !books.error && books.data && (
            <>
              <Card.Section>
                <BookTable
                  data={books.data.content}
                  onEditBookLoading={saveBook.loading || updateBook.loading}
                  onEditBook={handleEditBook}
                  onDeleteBookLoading={deleteBook.loading}
                  onDeleteBook={(book: Book) => confirmDeleteBook(book.id)}
                />
              </Card.Section>
              <Center>
                <Pagination
                  total={books.data.page.totalPages}
                  value={books.currentPage}
                  onChange={(page) => books.setCurrentPage(page)}
                  mt="md"
                />
              </Center>
            </>
          )}
        </Card>
      </Container>
      <BookFormModal
        opened={modalOpened}
        onClose={() => setModalOpened(false)}
        onSave={handleSaveBook}
        refetch={books.refetch}
        bookToEdit={bookToEdit || undefined}
      />
    </>
  );
}
