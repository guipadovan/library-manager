import { useEffect, useMemo, useState } from "react";
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
  TextInput,
  Title,
  Tooltip,
} from "@mantine/core";
import usePaginatedFetch from "../../../common/hook/usePaginatedFetch";
import { API_ENDPOINTS } from "../../../common/constant/endpoints";
import { BookTable } from "../component/BookTable";
import { IconBrandGoogle, IconPlus } from "@tabler/icons-react";
import { HttpMethod } from "../../../common/service/axiosService";
import useFetch from "../../../common/hook/useFetch";
import { modals } from "@mantine/modals";
import { notifications } from "@mantine/notifications";

export function BookListView() {
  const books = usePaginatedFetch<Book>(API_ENDPOINTS.BOOKS.GET);
  const saveBook = useFetch<Book>(HttpMethod.Post, "", false);
  const updateBook = useFetch<Book>(HttpMethod.Put, "", false);
  const deleteBook = useFetch<boolean>(HttpMethod.Delete, "", false);
  const searchBook = useFetch<Book[]>(HttpMethod.Get, "", false);

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

  // Integração com o Google Books API
  const [searchTerm, setSearchTerm] = useState<string>("");

  const handleSearchBooks = () => {
    if (searchTerm.trim() === "") {
      notifications.show({
        title: "Erro",
        message: "Digite o título do livro que deseja procurar",
        color: "red",
        position: "top-right",
      });
      return;
    }
    void searchBook.refetch(API_ENDPOINTS.BOOKS.SEARCH(searchTerm));
  };

  useEffect(() => {
    if (searchBook.data)
      modals.open({
        title: "Procurar Livro",
        size: "xl",
        children: (
          <BookTable
            data={searchBook.data}
            showIds={false}
            onEditBookLoading={false}
            onEditBook={() => {}}
            onDeleteBookLoading={false}
            onDeleteBook={() => {}}
            actionRenderer={({ book }) => (
              <Tooltip label="Adicionar" position="top">
                <ActionIcon
                  size="lg"
                  color="green"
                  loading={saveBook.loading}
                  onClick={() => {
                    saveBook
                      .refetch(API_ENDPOINTS.BOOKS.CREATE, book)
                      .then(() => {
                        void books.refetch();
                        modals.closeAll();
                        notifications.show({
                          title: "Sucesso",
                          message: "Livro adicionado com sucesso",
                          color: "green",
                          position: "top-right",
                        });
                      });
                  }}
                >
                  <IconPlus size={16} />
                </ActionIcon>
              </Tooltip>
            )}
          />
        ),
      });
  }, [searchBook.data]);

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
              <Group gap={8}>
                <Group gap={2}>
                  <TextInput
                    placeholder="Pesquisar com Google Books"
                    value={searchTerm}
                    onChange={(event) =>
                      setSearchTerm(event.currentTarget.value)
                    }
                  />
                  <Tooltip label="Procurar" position="bottom">
                    <ActionIcon
                      w={36}
                      h={36}
                      size="lg"
                      aria-label="Procurar Livro com Google Books"
                      variant="default"
                      loading={searchBook.loading}
                      onClick={handleSearchBooks}
                    >
                      <IconBrandGoogle size={28} />
                    </ActionIcon>
                  </Tooltip>
                </Group>
                <Tooltip label="Adicionar Livro" position="bottom">
                  <ActionIcon
                    w={36}
                    h={36}
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
