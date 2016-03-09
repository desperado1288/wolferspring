package com.wolferx.wolferspring.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Publisher {
  @Id
  @GeneratedValue
  private Long id;
  private String name;

  @JsonBackReference
  @OneToMany(mappedBy = "publisher")
  private List<Book> books;

  protected Publisher() { }

  public Publisher(String name) {
    this.name = name;
  }
}