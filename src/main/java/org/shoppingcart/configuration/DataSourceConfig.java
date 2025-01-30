//package org.shoppingcart.configuration;
//
//import javax.sql.DataSource;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Bean;
//
//@Configuration
//public class DataSourceConfig {
//
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver"); // Use setter methods
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/shopping_cart");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("abg@1234");
//        return dataSource;
//    }
//}
