package com.casestudy5.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableSpringDataWebSupport
@PropertySource("classpath:upload_file.properties")
@ComponentScan(basePackages = "com.casestudy5")
@EnableJpaRepositories(basePackages = "com.casestudy5")
public class AppConfiguration implements WebMvcConfigurer {

    @Value("${upload.image}")
    private String fileUpload;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println(fileUpload + "fileUpload");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + fileUpload);
    }
}