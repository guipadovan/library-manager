import type React from "react";
import { useEffect, useMemo, useRef, useState } from "react";
import useFetch from "./useFetch.ts";
import type { PaginationResponse } from "../type";
import { HttpMethod } from "../service/axiosService.ts";

export interface UsePaginatedFetchReturn<T> {
  pageSize: number;
  setPageSize: React.Dispatch<React.SetStateAction<number>>;
  currentPage: number;
  setCurrentPage: React.Dispatch<React.SetStateAction<number>>;
  currentUrl: string;
  setCurrentUrl: React.Dispatch<React.SetStateAction<string>>;
  loading: boolean;
  data: PaginationResponse<T> | null;
  error: Error | null;
  refetch: (newUrl?: string, newData?: unknown) => Promise<void>;
}

function usePaginatedFetch<T>(
  initialUrl: string,
  urlUpdater: (url: string) => string = (url: string) => url,
  executeOnMount?: boolean | null,
  initialPageSize?: number,
  initialCurrentPage?: number,
): UsePaginatedFetchReturn<T> {
  const [pageSize, setPageSize] = useState(initialPageSize ?? 15);
  const [currentPage, setCurrentPage] = useState(initialCurrentPage ?? 1);

  const getInitialUrl = () =>
    urlUpdater(`${initialUrl}?page=${currentPage - 1}&size=${pageSize}`);

  const [currentUrl, setCurrentUrl] = useState(getInitialUrl);

  const { loading, data, error, refetch } = useFetch<PaginationResponse<T>>(
    HttpMethod.Get,
    currentUrl,
    executeOnMount ?? true,
  );

  useEffect(() => {
    setCurrentUrl(getInitialUrl);
  }, [pageSize, currentPage]);

  const initialized = useRef(false);

  useEffect(() => {
    if (initialized.current) {
      void refetch(currentUrl);
    } else {
      initialized.current = true;
    }
  }, [currentUrl, refetch]);

  return useMemo(
    () => ({
      pageSize,
      setPageSize,
      currentPage,
      setCurrentPage,
      currentUrl,
      setCurrentUrl,
      loading,
      data,
      error,
      refetch,
    }),
    [pageSize, currentPage, currentUrl, loading, data, error, refetch],
  );
}

export default usePaginatedFetch;
