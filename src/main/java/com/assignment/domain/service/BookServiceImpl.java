package com.assignment.domain.service;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.assignment.domain.model.Book;
import com.assignment.application.dto.BookDto;
import com.assignment.domain.repository.BookRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;

  @Override
  public Optional<Book> getBookById(long bookId) {
    return bookRepository.findById(bookId);
  }

  @Override
  public Book saveBook(BookDto bookDTO) {
    Book bookToSave = Book.builder().title(bookDTO.getTitle()).build();

    bookRepository.save(bookToSave);
    return bookToSave;
  }

  @Override
  public void updateBook(long bookId, BookDto bookDTO) {
    Book bookToUpdate = bookRepository.getById(bookId);
    bookToUpdate.setTitle(bookDTO.getTitle());

    bookRepository.save(bookToUpdate);
  }

  @Override
  public void deleteBook(long bookId) {
    bookRepository.deleteById(bookId);
  }
}
