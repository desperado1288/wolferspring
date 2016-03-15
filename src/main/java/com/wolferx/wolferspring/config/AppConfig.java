package com.wolferx.wolferspring.config;

import com.wolferx.wolferspring.data.dao.PostDao;
import org.skife.jdbi.v2.DBI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Autowired
    private DBI appDbi;

    @Bean
    public PostDao postDao() {
        return this.appDbi.onDemand(PostDao.class);
    }

}
