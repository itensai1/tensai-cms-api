package com.tensai.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.modulith.Modulithic;

@ConfigurationPropertiesScan
@Modulithic(sharedModules = "shared")
@SpringBootApplication
public class TensaiCmsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TensaiCmsApiApplication.class, args);
    }

}
