import { Button, Group, TextInput } from "@mantine/core";
import { IconBook, IconCalendar, IconUser } from "@tabler/icons-react";
import { useForm } from "@mantine/form";
import { useState } from "react";
import { notifications } from "@mantine/notifications";
import { ContextModalProps } from "@mantine/modals";
import type { CreateLease } from "../../../common/type";
import { DateInput } from "@mantine/dates";
import dayjs from "dayjs";

interface LeaseBookFormModalProps {
  onSave: (createLease: CreateLease) => Promise<void>;
}

export function LeaseBookFormModal({
  context,
  id,
  innerProps,
}: ContextModalProps<LeaseBookFormModalProps>) {
  const form = useForm<CreateLease>({
    initialValues: {
      userId: "",
      bookId: "",
      leaseDate: new Date().toISOString().split("T")[0],
      returnDate: "",
    },
    validate: {
      userId: (value) => (value ? null : "O ID do usuário é obrigatório"),
      bookId: (value) => (value ? null : "O ID do livro é obrigatório"),
      leaseDate: (value) =>
        value ? null : "A data de empréstimo é obrigatória",
      returnDate: (value) =>
        value ? null : "A data de devolução é obrigatória",
    },
  });

  const [loading, setLoading] = useState<boolean>(false);

  const handleSave = (values: CreateLease) => {
    setLoading(true);
    innerProps
      .onSave(values)
      .then(() => {
        form.reset();
        setLoading(false);
        context.closeModal(id);
        notifications.show({
          title: "Sucesso",
          message: "Livro emprestado com sucesso",
          color: "green",
          position: "top-right",
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
            position: "top-right",
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
        label="Usuário"
        placeholder="ID do usuário"
        leftSection={<IconUser size={16} />}
        {...form.getInputProps("userId")}
      />
      <TextInput
        withAsterisk
        type="number"
        label="Livro"
        placeholder="ID do livro"
        leftSection={<IconBook size={16} />}
        {...form.getInputProps("bookId")}
      />
      <DateInput
        label="Data de empréstimo"
        placeholder="Data de empréstimo"
        weekendDays={[]}
        leftSection={<IconCalendar size={16} />}
        value={
          form.values.leaseDate
            ? dayjs(form.values.leaseDate, "YYYY-MM-DD").toDate()
            : null
        }
        onChange={(date) =>
          form.setFieldValue("leaseDate", date ? date.toISOString() : "")
        }
        locale="pt-br"
        error={form.errors.leaseDate}
        valueFormat="DD/MM/YYYY"
        clearable
        withAsterisk
        dateParser={(input) => dayjs(input, "DD/MM/YYYY").toDate()}
      />
      <DateInput
        label="Data de devolução"
        placeholder="Data de devolução"
        weekendDays={[]}
        leftSection={<IconCalendar size={16} />}
        value={
          form.values.returnDate
            ? dayjs(form.values.returnDate, "YYYY-MM-DD").toDate()
            : null
        }
        onChange={(date) =>
          form.setFieldValue("returnDate", date ? date.toISOString() : "")
        }
        locale="pt-br"
        error={form.errors.returnDate}
        valueFormat="DD/MM/YYYY"
        clearable
        withAsterisk
        dateParser={(input) => dayjs(input, "DD/MM/YYYY").toDate()}
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
