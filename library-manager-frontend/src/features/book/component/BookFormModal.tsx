import { Button, Group, Modal, TextInput } from "@mantine/core";
import { DateInput } from "@mantine/dates";
import {
  IconBarcode,
  IconBook,
  IconCalendar,
  IconTag,
  IconUser,
} from "@tabler/icons-react";
import type { Book } from "../../../common/type";
import useFormModal from "../../../common/hook/useFormModal";
import dayjs from "dayjs";

interface BookFormModalProps {
  opened: boolean;
  refetch: () => Promise<void>;
  bookToEdit?: Book;
  onClose: () => void;
  onSave: (book: Book) => Promise<Book>;
}

export function BookFormModal({
  opened,
  refetch,
  bookToEdit,
  onClose,
  onSave,
}: BookFormModalProps) {
  const { form, handleSave, loading } = useFormModal<Book>({
    initialValues: {
      id: 0,
      title: "",
      author: "",
      isbn: "",
      publicationDate: "",
      category: "",
    },
    validate: {
      id: () => null,
      title: (value) => (value ? null : "O título é obrigatório"),
      author: (value) => (value ? null : "O autor é obrigatório"),
      isbn: (value) => (value ? null : "O ISBN é obrigatório"),
      publicationDate: (value) =>
        value ? null : "A data de publicação é obrigatória",
      category: (value) => (value ? null : "A categoria é obrigatória"),
    },
    entityToEdit: bookToEdit,
    saveEntity: onSave,
    refetch,
  });

  return (
    <Modal
      opened={opened}
      onClose={onClose}
      title={bookToEdit ? "Editar Livro" : "Novo Livro"}
    >
      <form onSubmit={form.onSubmit((values) => handleSave(values, onClose))}>
        <TextInput
          withAsterisk
          label="Título"
          placeholder="Título do livro"
          leftSection={<IconBook size={16} />}
          {...form.getInputProps("title")}
        />
        <TextInput
          withAsterisk
          label="Autor"
          placeholder="Autor do livro"
          leftSection={<IconUser size={16} />}
          {...form.getInputProps("author")}
        />
        <TextInput
          withAsterisk
          label="ISBN"
          placeholder="ISBN do livro"
          leftSection={<IconBarcode size={16} />}
          {...form.getInputProps("isbn")}
        />
        <DateInput
          label="Data de Publicação"
          placeholder="Data de publicação"
          weekendDays={[]}
          leftSection={<IconCalendar size={16} />}
          value={
            form.values.publicationDate
              ? dayjs(form.values.publicationDate, "YYYY-MM-DD").toDate()
              : null
          }
          onChange={(date) =>
            form.setFieldValue(
              "publicationDate",
              date ? date.toISOString() : "",
            )
          }
          locale="pt-br"
          error={form.errors.publicationDate}
          valueFormat="DD/MM/YYYY"
          clearable
          withAsterisk
          dateParser={(input) => dayjs(input, "DD/MM/YYYY").toDate()}
        />
        <TextInput
          withAsterisk
          label="Categoria"
          placeholder="Categoria"
          leftSection={<IconTag size={16} />}
          {...form.getInputProps("category")}
        />
        <Group justify="flex-end" mt="md">
          <Button onClick={onClose} variant="outline">
            Cancelar
          </Button>
          <Button type="submit" loading={loading}>
            Salvar
          </Button>
        </Group>
      </form>
    </Modal>
  );
}
