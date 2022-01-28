package com.assignment.domain.service;

import com.assignment.application.dto.BookDto;
import com.assignment.domain.model.Book;
import java.util.Optional;

public interface BookService {

  Optional<Book> getBookById(long bookId);
  Book saveBook(BookDto bookDTO);
  void updateBook(long bookId, BookDto bookDTO);
  void deleteBook(long bookId);

}
