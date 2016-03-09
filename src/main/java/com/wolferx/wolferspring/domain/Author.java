package com.wolferx.wolferspring.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class Author {
  @Id
  @GeneratedValue
  private Long id;
  private String firstName;
  private String lastName;

  @JsonBackReference
  @OneToMany(mappedBy = "author")
  private List<Book> books;

  protected Author() {

  }

  public Author(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }
}