package com.wolferx.wolferspring.config;

import com.wolferx.wolferspring.common.filter.CorsFilter;
import com.wolferx.wolferspring.jdbi.dao.CommentDao;
import com.wolferx.wolferspring.jdbi.dao.PostDao;
import com.wolferx.wolferspring.jdbi.dao.UserDao;
import org.skife.jdbi.v2.DBI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class AppConfig {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private DBI appDbi;

    //***********
    //** CORS
    //***********
    @Bean
    public FilterRegistrationBean corsFilter() {
        final FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        final Filter corsFilter = new CorsFilter();
        beanFactory.autowireBean(corsFilter);
        filterRegistration.setFilter(corsFilter);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    //***********
    //** DAO
    //***********
    @Bean
    public PostDao postDao() { return this.appDbi.onDemand(PostDao.class); }
    @Bean
    public CommentDao commentDao() { return this.appDbi.onDemand(CommentDao.class); }
    @Bean
    public UserDao userDao() { return this.appDbi.onDemand(UserDao.class); }
}
