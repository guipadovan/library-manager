import { Book } from "./book";
import { User } from "./user";

export interface Lease {
  id: number;
  userId: number;
  user: User;
  bookId: number;
  book: Book;
  leaseDate: string;
  returnDate: string;
  status: string;
}

export interface CreateLease {
  userId: number | string;
  bookId: number | string;
  leaseDate: string;
  returnDate: string;
}
