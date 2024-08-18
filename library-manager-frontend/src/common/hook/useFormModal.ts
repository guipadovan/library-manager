import { useEffect, useState } from "react";
import { useForm, UseFormInput, UseFormReturnType } from "@mantine/form";
import { notifications } from "@mantine/notifications";

interface UseFormModalProps<T> {
  initialValues: UseFormInput<T>["initialValues"];
  validate: UseFormInput<T>["validate"];
  entityToEdit?: T;
  saveEntity: (entity: T) => Promise<T>;
  refetch: () => Promise<void>;
}

const useFormModal = <T extends Record<string, any>>({
  initialValues,
  validate,
  entityToEdit,
  saveEntity,
  refetch,
}: UseFormModalProps<T>): {
  form: UseFormReturnType<T>;
  handleSave: (values: T, onClose: () => void) => void;
  loading: boolean;
} => {
  const form = useForm<T>({ initialValues, validate });

  const [loading, setLoading] = useState<boolean>(false);

  const handleSave = (values: T, onClose: () => void) => {
    setLoading(true);
    saveEntity(values)
      .then(() => {
        form.reset();
        void refetch();
        setLoading(false);
        onClose();
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

  useEffect(() => {
    if (entityToEdit != null) {
      form.setValues(entityToEdit);
    } else {
      form.reset();
    }
  }, [entityToEdit]);

  return { form, handleSave, loading };
};

export default useFormModal;
