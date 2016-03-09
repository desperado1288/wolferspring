package com.wolferx.wolferspring;

import com.wolferx.wolferspring.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class StartupRunner implements CommandLineRunner {

  protected final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

  @Autowired
  private BookRepository bookRepository;

  // Using Jdbc directly if use pure SQL
  //@Autowired
  //private JdbcTemplate jdbcTemplate;

  @Override
  public void run(String... strings) throws Exception {

    logger.info("StartupRunner Starting...");
    logger.info("BookRepository: " + bookRepository.toString());
    logger.info("Number of books: " + bookRepository.count());
  }
}