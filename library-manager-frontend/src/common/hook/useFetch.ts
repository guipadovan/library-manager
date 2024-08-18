import { useCallback, useEffect, useRef, useState } from "react";
import { HttpMethod, makeRequest } from "../service/axiosService.ts";

interface FetchState<T> {
  data: T | null;
  loading: boolean;
  error: Error | null;
}

export interface UseFetchReturn<T> extends FetchState<T> {
  refetch: (newUrl?: string, newData?: unknown) => Promise<T>;
}

const useFetch = <T>(
  httpMethod: HttpMethod,
  initialUrl: string,
  executeOnMount = true,
): UseFetchReturn<T> => {
  const [state, setState] = useState<FetchState<T>>({
    data: null,
    loading: executeOnMount,
    error: null,
  });

  const urlRef = useRef(initialUrl);
  const dataRef = useRef<unknown | undefined>(undefined);

  const fetchData = useCallback(async () => {
    setState((prev) => ({ ...prev, loading: true, error: null }));

    try {
      const response = await makeRequest(
        httpMethod,
        urlRef.current,
        dataRef.current,
      );
      const responseData = response.data as T;
      setState({ data: responseData, loading: false, error: null });
      return responseData;
    } catch (err) {
      const error =
        err instanceof Error ? err : new Error("Something went wrong.");
      setState({ data: null, loading: false, error });

      throw error;
    }
  }, [httpMethod]);

  useEffect(() => {
    if (executeOnMount) {
      void fetchData();
    }
  }, [executeOnMount, fetchData]);

  const refetch = useCallback(
    async (newUrl?: string, newData?: unknown) => {
      if (newUrl) urlRef.current = newUrl;
      if (newData !== undefined) dataRef.current = newData;
      return fetchData();
    },
    [fetchData],
  );

  return { ...state, refetch };
};

export default useFetch;
