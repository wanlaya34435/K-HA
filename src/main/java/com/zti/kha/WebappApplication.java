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
        SSLContextHelper.setSslProperties();
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

//    protected static class SSLContextHelper {
//
//        private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
//        private static final String DEFAULT_SSL_CERTIFICATE = "ssl/rds-combined-ca-bundle.pem";
//        private static final String SSL_CERTIFICATE = "sslCertificate";
//        private static final String KEY_STORE_TYPE = "JKS";
//        private static final String KEY_STORE_PROVIDER = "SUN";
//        private static final String KEY_STORE_FILE_PREFIX = "sys-connect-via-ssl-test-cacerts";
//        private static final String KEY_STORE_FILE_SUFFIX = ".jks";
//        private static final String DEFAULT_KEY_STORE_PASSWORD = "changeit";
//        private static final String SSL_TRUST_STORE = "javax.net.ssl.trustStore";
//        private static final String SSL_TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";
//        private static final String SSL_TRUST_STORE_TYPE = "javax.net.ssl.trustStoreType";
//
//
//
//        private static void setSslProperties() {
//
//            try {
////                String sslCertificate= System.getProperty(SSL_CERTIFICATE);
//                String sslCertificate = "C:/Users/ZTI61-13/Desktop/AWSDocDB/ap-southeast-1-bundle.p7b";
////                if(ObjectUtils.isEmpty(sslCertificate)) {
////                    sslCertificate= DEFAULT_SSL_CERTIFICATE;
////                }
//                logger.info(" ssl certificate path {}",sslCertificate);
//                System.setProperty(SSL_TRUST_STORE, createKeyStoreFile(sslCertificate));
//                System.setProperty(SSL_TRUST_STORE_TYPE, KEY_STORE_TYPE);
//                System.setProperty(SSL_TRUST_STORE_PASSWORD, DEFAULT_KEY_STORE_PASSWORD);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        private static String createKeyStoreFile(String sslCertificate) throws Exception {
//            return createKeyStoreFile(createCertificate(sslCertificate)).getPath();
//        }
//
//        private static X509Certificate createCertificate(String sslCertificate) throws Exception {
//            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
//            URL url = new File(sslCertificate).toURI().toURL();
//            if (url == null) {
//                throw new Exception();
//            }
//            try (InputStream certInputStream = url.openStream()) {
//                return (X509Certificate) certFactory.generateCertificate(certInputStream);
//            }
//        }
//
//        private static File createKeyStoreFile(X509Certificate rootX509Certificate) throws Exception {
//            File keyStoreFile = File.createTempFile(KEY_STORE_FILE_PREFIX, KEY_STORE_FILE_SUFFIX);
//            try (FileOutputStream fos = new FileOutputStream(keyStoreFile.getPath())) {
//                KeyStore ks = KeyStore.getInstance(KEY_STORE_TYPE, KEY_STORE_PROVIDER);
//                ks.load(null);
//                ks.setCertificateEntry("rootCaCertificate", rootX509Certificate);
//                ks.store(fos, DEFAULT_KEY_STORE_PASSWORD.toCharArray());
//            }
//            return keyStoreFile;
//        }
//
//    }

    protected static class SSLContextHelper {

        private static final Logger logger = LoggerFactory.getLogger(SSLContextHelper.class);
//        private static final String SSL_CERTIFICATE = "C:/Users/ZTI61-13/Desktop/AWSDocDB/ap-southeast-1-bundle.p7b";
//        private static final String SSL_CERTIFICATE = "ssl/rds-combined-ca-bundle.pem";
        private static final String SSL_CERTIFICATE = "ssl/amz-ap-se1.cer";

        private static final String KEY_STORE_TYPE = "JKS";
        private static final String KEY_STORE_PROVIDER = "SUN";
        private static final String KEY_STORE_FILE_PREFIX = "sys-connect-via-ssl-test-cacerts";
        private static final String KEY_STORE_FILE_SUFFIX = ".jks";
        private static final String DEFAULT_KEY_STORE_PASSWORD = "zeal1tech";

        /**
         * This method sets the SSL properties which specify the key store file, its type and password:
         *
         * @throws Exception
         */
        private static void setSslProperties() {

            try {
//                System.setProperty("javax.net.ssl.trustStore", createKeyStoreFile());
//                String keyPath = createKeyStoreFile();
                String keyPath = "ssl/ap-se-store.jks";
                Resource resource = new ClassPathResource(keyPath);
//                File f = new File(keyPath);
//                System.out.println(f.getAbsolutePath());
                logger.info(">>>>> keyPath: "+resource.getFile().getAbsolutePath());
                System.out.println(">>>>> keyPath: "+resource.getFile().getAbsolutePath());
                System.setProperty("javax.net.ssl.trustStore", resource.getFile().getAbsolutePath());
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

