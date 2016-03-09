package com.wolferx.wolferspring.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Reviewer {
  @Id
  @GeneratedValue
  private Long id;
  private String firstName;
  private String lastName;

  protected Reviewer() { }

  public Reviewer(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }
}