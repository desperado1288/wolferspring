package com.wolferx.wolferspring.controller;

import com.alibaba.fastjson.JSONObject;
import com.wolferx.wolferspring.domain.Author;
import com.wolferx.wolferspring.domain.Book;
import com.wolferx.wolferspring.domain.Publisher;
import com.wolferx.wolferspring.repository.AuthorRepository;
import com.wolferx.wolferspring.repository.BookRepository;
import com.wolferx.wolferspring.repository.PublisherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/books")
public class BookController {
  private static final Logger logger = LoggerFactory.getLogger(BookController.class);

  @Autowired
  private BookRepository bookRepository;
  @Autowired
  public AuthorRepository authorRepository;
  @Autowired
  public PublisherRepository publisherRepository;


  @RequestMapping(method = RequestMethod.GET)
  public Iterable<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  @RequestMapping(value = "/{isbn}", method = RequestMethod.GET)
  public Map<String, Object> getBook(@PathVariable String isbn) {
    Book book =  bookRepository.findBookByIsbn(isbn);

    Map<String, Object> response = new LinkedHashMap<String, Object>();
    response.put("message", "get book with isbn(" + isbn +")");
    response.put("book", book);
    return response;
  }

  @RequestMapping(method = RequestMethod.POST)
  public Map<String, Object> addBook(@RequestBody JSONObject bookJson) {
    JSONObject authorJson = bookJson.getJSONObject("author");
    Author author = new Author(authorJson.getString("firstName"), authorJson.getString("lastName"));
    authorRepository.save(author);
    String isbn = bookJson.getString("isbn");
    JSONObject publisherJson = bookJson.getJSONObject("publisher");
    Publisher publisher = new Publisher(publisherJson.getString("name"));
    publisherRepository.save(publisher);
    String title = bookJson.getString("title");
    String desc = bookJson.getString("desc");
    Book book = new Book(author, isbn, publisher, title);
    book.setDescription(desc);
    bookRepository.save(book);

    Map<String, Object> response = new LinkedHashMap<String, Object>();
    response.put("message", "book add successfully");
    response.put("book", book);
    return response;
  }

  @RequestMapping(value = "/{isbn}", method = RequestMethod.DELETE)
  public Map<String, Object> deleteBook(@PathVariable String isbn) {
    Map<String, Object> response = new LinkedHashMap<String, Object>();
    try {
      bookRepository.deleteBookByIsbn(isbn);
    } catch (NullPointerException e) {
      logger.error("the book is not in database");
      response.put("message", "delete failure");
      response.put("code", 0);
    }

    response.put("message", "delete successfully");
    response.put("code", 1);
    return response;
  }

  @RequestMapping(value = "/{isbn}/{title}", method = RequestMethod.PUT)
  public Map<String, Object> updateBookTitle(@PathVariable String isbn, @PathVariable String title) {
    Map<String, Object> response = new LinkedHashMap<String, Object>();
    Book book;
    try {
      book = bookRepository.findBookByIsbn(isbn);
      book.setTitle(title);
      bookRepository.save(book);
    } catch (NullPointerException e) {
      response.put("message", "can not find the book");
      return response;
    }

    response.put("message", "book update successfully");
    response.put("book", book);
    return response;
  }
}
