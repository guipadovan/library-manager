import { Button, Group, TextInput } from "@mantine/core";
import { IconBook } from "@tabler/icons-react";
import { useForm } from "@mantine/form";
import { useState } from "react";
import { notifications } from "@mantine/notifications";
import { ContextModalProps } from "@mantine/modals";

interface ReturnBookFormModalProps {
  onSave: (values: { bookId: number | string }) => Promise<void>;
}

export function ReturnBookFormModal({
  context,
  id,
  innerProps,
}: ContextModalProps<ReturnBookFormModalProps>) {
  const form = useForm<{ bookId: number | string }>({
    initialValues: {
      bookId: "",
    },
    validate: {
      bookId: (value) => (value ? null : "O ID do livro é obrigatório"),
    },
  });

  const [loading, setLoading] = useState<boolean>(false);

  const handleSave = (values: { bookId: number | string }) => {
    setLoading(true);
    innerProps
      .onSave(values)
      .then(() => {
        form.reset();
        setLoading(false);
        context.closeModal(id);
        notifications.show({
          title: "Sucesso",
          message: "Livro retornado com sucesso",
          color: "green",
        });
      })
      .catch((err) => {
        if (err.response.status === 400) {
          form.setErrors(err.response.data);
        } else {
          notifications.show({
            title: "Erro",
            message: err.response.data.message,
            color: "red",
          });
        }
        setLoading(false);
      });
  };

  return (
    <form onSubmit={form.onSubmit((values) => handleSave(values))}>
      <TextInput
        withAsterisk
        type="number"
        label="Livro"
        placeholder="ID do livro"
        leftSection={<IconBook size={16} />}
        {...form.getInputProps("bookId")}
      />
      <Group justify="flex-end" mt="md">
        <Button onClick={() => context.closeModal(id)} variant="outline">
          Cancelar
        </Button>
        <Button type="submit" loading={loading}>
          Salvar
        </Button>
      </Group>
    </form>
  );
}
