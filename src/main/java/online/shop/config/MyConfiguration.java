package online.shop.config;

import online.shop.dao.impl.DataAbstractFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("online.shop.controller")
@PropertySource("classpath:datasource.properties")
public class MyConfiguration implements WebMvcConfigurer{

    @Autowired
    Environment env;

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/pages/", ".jsp");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(1000);  //SECONDS
    }

    @Bean
    public DriverManagerDataSource driverManagerDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("driverClassName"));
        dataSource.setUsername(env.getProperty("dbUsername"));
        dataSource.setPassword(env.getProperty("password"));
        dataSource.setUrl(env.getProperty("url"));
        return dataSource;
    }

    @Bean
    public DataAbstractFactory dataAbstractFactory(){
        return DataAbstractFactory.getFactory(driverManagerDataSource());
    }
}
