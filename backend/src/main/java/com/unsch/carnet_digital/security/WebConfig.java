package com.unsch.carnet_digital.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        /*
        System.out.println("Sirviendo imágenes desde:");
        System.out.println("file:///D:/JUNIOR/control_ph/");

        registry.addResourceHandler("/control_ph/**")
                .addResourceLocations("file:///D:/JUNIOR/control_ph/");
        */
    }
}
