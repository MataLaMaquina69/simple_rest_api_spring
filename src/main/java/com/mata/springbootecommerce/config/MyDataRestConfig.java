package com.mata.springbootecommerce.config;

import com.mata.springbootecommerce.entity.Country;
import com.mata.springbootecommerce.entity.Product;
import com.mata.springbootecommerce.entity.ProductCategory;
import com.mata.springbootecommerce.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;
    @Autowired
    public MyDataRestConfig(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        HttpMethod[] unsupportedActions = {HttpMethod.DELETE, HttpMethod.PUT,HttpMethod.POST};

        disabeHttpMethods(Product.class, config, unsupportedActions);
        //disable http methods for product: put, post,update
        disabeHttpMethods(ProductCategory.class, config, unsupportedActions);
        disabeHttpMethods(State.class, config, unsupportedActions);
        disabeHttpMethods(Country.class, config, unsupportedActions);


        exposeIds(config);
    }

    private void disabeHttpMethods(Class currentClass,RepositoryRestConfiguration config, HttpMethod[] unsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(currentClass)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(unsupportedActions)))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        //expose entity ids

        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        List<Class> entityClasses = new ArrayList<>();

        for(EntityType entity: entities){
            entityClasses.add(entity.getJavaType());
        }
        Class[] domainTipe = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTipe);
    }
}
