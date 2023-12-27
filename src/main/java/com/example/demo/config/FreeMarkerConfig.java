package com.example.demo.config;

import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import freemarker.template.Configuration;
import org.springframework.context.annotation.Primary;

@org.springframework.context.annotation.Configuration
public class FreeMarkerConfig {

    @Primary
    @Bean
    public Configuration freemarkerConfiguration(){
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setClassForTemplateLoading(this.getClass(),"/templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);

        return cfg;
    }

}
