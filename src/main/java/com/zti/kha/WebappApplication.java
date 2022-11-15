package com.zti.kha;

import com.mongodb.MongoClientOptions;
import com.twelvemonkeys.servlet.image.IIOProviderContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Locale;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class WebappApplication extends SpringBootServletInitializer {


            private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebappApplication.class);
    }

    /**
     * On startup
     * @param servletContext
     * @throws ServletException
     */
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        // fix bugs ImageIO (Resize file)
        servletContext.addListener(IIOProviderContextListener.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WebappApplication.class, args);
    }

    @Bean("messageSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource=new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:locale/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }
// Comment code when dev
    @Bean
    public  MongoClientOptions mongoClientOptions(){
        String keyPath = "ssl/ap-se-store.jks";
        String keyType = "JKS";
        String keyPassword = "zeal1tech";
        try {
            Resource resource = new ClassPathResource(keyPath);
            System.setProperty("javax.net.ssl.trustStore", resource.getFile().getAbsolutePath());
            System.setProperty("javax.net.ssl.keyStore", resource.getFile().getAbsolutePath());
        } catch (Exception e) {

            e.printStackTrace();
        }
        System.setProperty("javax.net.ssl.trustStoreType", keyType);
        System.setProperty("javax.net.ssl.trustStorePassword", keyPassword);
        System.setProperty("javax.net.ssl.keyStoreType", keyType);
        System.setProperty("javax.net.ssl.keyStorePassword", keyPassword);

        MongoClientOptions.Builder builder = MongoClientOptions.builder();
        MongoClientOptions options=builder.sslEnabled(true).build();
        return options;
    }



    }

