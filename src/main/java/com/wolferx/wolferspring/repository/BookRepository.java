package com.wolferx.wolferspring.repository;

import com.wolferx.wolferspring.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
  Book findBookByIsbn(String isbn);
  Boolean deleteBookByIsbn(String isbn);
}