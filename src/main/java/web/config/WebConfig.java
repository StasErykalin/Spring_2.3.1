package web.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableWebMvc
@ComponentScan("web")
@PropertySource("classpath:hibernate.properties")
@EnableTransactionManagement
@EnableJpaRepositories("web")
public class WebConfig implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;

    private final Environment environment;
    public WebConfig(ApplicationContext applicationContext, Environment environment){
        this.applicationContext = applicationContext;
        this.environment = environment;
    }
    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/pages");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }
    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry){
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("hibernate.connection.driver_class"));
        dataSource.setUrl(environment.getRequiredProperty("hibernate.connection.url"));
        dataSource.setUsername(environment.getRequiredProperty("hibernate.connection.username"));
        dataSource.setPassword(environment.getRequiredProperty("hibernate.connection.password"));
        return dataSource;
    }
    public Properties hibernateProperties(){
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        return properties;
    }
//    @Bean
//    public LocalSessionFactoryBean sessionFactory(){
//        LocalSessionFactoryBean localSessionFactory = new LocalSessionFactoryBean();
//        localSessionFactory.setDataSource(dataSource());
//        localSessionFactory.setPackagesToScan("web/model");
//        localSessionFactory.setHibernateProperties(hibernateProperties());
//        return localSessionFactory;
//    }
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        final LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(dataSource());
        entityManager.setPackagesToScan("web/model");
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(vendorAdapter);
        entityManager.setJpaProperties(hibernateProperties());
        return entityManager;
    }
//    @Bean
//    public PlatformTransactionManager transactionManager(){
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(sessionFactory().getObject());
//        return transactionManager;
//    }
    @Bean
    public PlatformTransactionManager transactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}
