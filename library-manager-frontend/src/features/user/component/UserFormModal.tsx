import { Button, Group, InputBase, Modal, TextInput } from "@mantine/core";
import { DateInput } from "@mantine/dates";
import type { User } from "../../../common/type";
import {
  IconBarcode,
  IconCalendar,
  IconPhone,
  IconUser,
} from "@tabler/icons-react";
import dayjs from "dayjs";
import { IMaskInput } from "react-imask";
import useFormModal from "../../../common/hook/useFormModal";
import { useEffect } from "react";

interface UserFormModalProps {
  opened: boolean;
  refetch: () => Promise<void>;
  userToEdit?: User;
  onClose: () => void;
  onSave: (user: User) => Promise<User>;
}

export function UserFormModal({
  opened,
  refetch,
  userToEdit,
  onClose,
  onSave,
}: UserFormModalProps) {
  const { form, handleSave, loading } = useFormModal<User>({
    initialValues: {
      id: 0,
      name: "",
      email: "",
      registrationDate: "",
      phone: "",
    },
    validate: {
      name: (value) => (value ? null : "O nome é obrigatório"),
      email: (value) =>
        value
          ? /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)
            ? null
            : "Email inválido"
          : "O email é obrigatório",
      registrationDate: (value) =>
        value ? null : "A data de registro é obrigatória",
      phone: (value) => (value ? null : "O telefone é obrigatório"),
    },
    entityToEdit: userToEdit,
    saveEntity: onSave,
    refetch,
  });

  useEffect(() => {
    if (userToEdit == null) {
      form.setFieldValue("registrationDate", new Date().toISOString());
    }
  }, [userToEdit]);

  return (
    <Modal
      opened={opened}
      onClose={onClose}
      title={userToEdit ? "Editar Usuário" : "Novo Usuário"}
    >
      <form onSubmit={form.onSubmit((values) => handleSave(values, onClose))}>
        <TextInput
          withAsterisk
          label="Nome"
          placeholder="Nome do usuário"
          leftSection={<IconUser size={16} />}
          {...form.getInputProps("name")}
        />
        <TextInput
          withAsterisk
          label="Email"
          placeholder="Email do usuário"
          leftSection={<IconBarcode size={16} />}
          {...form.getInputProps("email")}
        />
        <DateInput
          label="Data de Registro"
          placeholder="Data de registro"
          weekendDays={[]}
          leftSection={<IconCalendar size={16} />}
          value={
            form.values.registrationDate
              ? dayjs(form.values.registrationDate, "YYYY-MM-DD").toDate()
              : null
          }
          onChange={(date) =>
            form.setFieldValue(
              "registrationDate",
              date ? date.toISOString() : "",
            )
          }
          locale={"pt-br"}
          error={form.errors.registrationDate}
          valueFormat="DD/MM/YYYY"
          clearable
          withAsterisk
          dateParser={(input) => dayjs(input, "DD/MM/YYYY").toDate()}
        />
        <InputBase
          withAsterisk
          component={IMaskInput}
          mask={[
            {
              mask: "(00) 0000-0000",
              lazy: true,
            },
            {
              mask: "(00) 00000-0000",
              lazy: true,
            },
          ]}
          label="Telefone"
          placeholder="Telefone"
          leftSection={<IconPhone size={16} />}
          {...form.getInputProps("phone")}
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
