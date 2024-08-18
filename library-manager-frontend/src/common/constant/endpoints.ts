export const API_ENDPOINTS = {
  BOOKS: {
    GET: "v1/books",
    CREATE: "v1/books",
    UPDATE: (id: number) => `v1/books/${id}`,
    DELETE: (id: number) => `v1/books/${id}`,
  },
  USERS: {
    GET: "v1/users",
    CREATE: "v1/users",
    UPDATE: (id: number) => `v1/users/${id}`,
    DELETE: (id: number) => `v1/users/${id}`,
  },
  RECOMMENDATIONS: {
    GET: (userId: number) => `v1/recommendations/${userId}`,
  },
  LEASES: {
    CREATE: "v1/leases",
    RETURN: (bookId: number) => `v1/leases/${bookId}/return`,
  },
};
