package com.assignment.domain.controller;

import com.assignment.domain.model.Book;
import com.assignment.application.dto.BookDto;
import com.assignment.domain.service.BookService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/book")
public class BooksController {

  private final BookService bookService;

  @GetMapping({"/{bookId}"})
  public ResponseEntity<Book> getBook(@PathVariable("bookId") Integer bookId) {
    Optional<Book> bookByIdOpt = bookService.getBookById(bookId);
    return bookByIdOpt.map(book -> new ResponseEntity<>(book, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @PostMapping("/addBook")
  public ResponseEntity<Void> handleSave(@RequestBody BookDto bookDTO){
    Book savedBook = bookService.saveBook(bookDTO);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Saved-Book-Id: ", String.valueOf(savedBook.getId()));

    return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
  }

  @PutMapping("/updateBook")
  public ResponseEntity<Void> handleUpdate(@PathVariable("bookId") long bookId,
      @RequestBody BookDto bookDTO) {
    bookService.updateBook(bookId, bookDTO);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping({"/{bookId}"})
  public ResponseEntity<Void> deleteBook(@PathVariable("bookId") Integer bookId) {
    bookService.deleteBook(bookId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
