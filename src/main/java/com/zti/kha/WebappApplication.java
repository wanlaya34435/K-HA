package com.zti.kha;

import com.twelvemonkeys.servlet.image.IIOProviderContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
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

    private static final String SSL_CERTIFICATE = "rds-ca-2015-us-east-1.pem";
    private static final String KEY_STORE_TYPE = "JKS";
    private static final String KEY_STORE_PROVIDER = "SUN";
    private static final String KEY_STORE_FILE_PREFIX = "sys-connect-via-ssl-test-cacerts";
    private static final String KEY_STORE_FILE_SUFFIX = ".jks";
    private static final String DEFAULT_KEY_STORE_PASSWORD = "changeit";
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
//        SSLContextHelper.setSslProperties();
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
    protected static class SSLContextHelper {
        /**
         * This method sets the SSL properties which specify the key store file, its type and password:
         *
         * @throws Exception
         */
        private static void setSslProperties() {

            try {
                System.setProperty("javax.net.ssl.trustStore", createKeyStoreFile());
            } catch (Exception e) {

                e.printStackTrace();
            }
            System.setProperty("javax.net.ssl.trustStoreType", KEY_STORE_TYPE);
            System.setProperty("javax.net.ssl.trustStorePassword", DEFAULT_KEY_STORE_PASSWORD);
        }


        private static String createKeyStoreFile() throws Exception {
            return createKeyStoreFile(createCertificate()).getPath();
        }

        /**
         * This method generates the SSL certificate
         *
         * @return
         * @throws Exception
         */
        private static X509Certificate createCertificate() throws Exception {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            URL url = new File(SSL_CERTIFICATE).toURI().toURL();
            if (url == null) {
                throw new Exception();
            }
            try (InputStream certInputStream = url.openStream()) {
                return (X509Certificate) certFactory.generateCertificate(certInputStream);
            }
        }

        /**
         * This method creates the Key Store File
         *
         * @param rootX509Certificate - the SSL certificate to be stored in the KeyStore
         * @return
         * @throws Exception
         */
        private static File createKeyStoreFile(X509Certificate rootX509Certificate) throws Exception {
            File keyStoreFile = File.createTempFile(KEY_STORE_FILE_PREFIX, KEY_STORE_FILE_SUFFIX);
            try (FileOutputStream fos = new FileOutputStream(keyStoreFile.getPath())) {
                KeyStore ks = KeyStore.getInstance(KEY_STORE_TYPE, KEY_STORE_PROVIDER);
                ks.load(null);
                ks.setCertificateEntry("rootCaCertificate", rootX509Certificate);
                ks.store(fos, DEFAULT_KEY_STORE_PASSWORD.toCharArray());
            }
            return keyStoreFile;
        }

    }
    }

