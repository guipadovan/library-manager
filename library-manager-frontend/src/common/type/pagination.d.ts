export interface PaginationResponse<T> {
  content: T[];
  page: {
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
  };
}
