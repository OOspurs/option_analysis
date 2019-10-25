package com.spurspro.option;

import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * MongoConfig 连接多个数据源
 * Created by Jimmy.Liu on 2018-09-18 04:29
 *
 * @since 2.01.06
 */

@Configuration
public class MongoConfig {
    //@Value("${mongo.host}")
    @Value("${spring.data.mongodb.order.uri}")
    private String MONGO_ORDER_URI;
    @Value("${spring.data.mongodb.member.uri}")
    private String MONGO_MEMBER_URI;
    @Value("${spring.data.mongodb.user.uri}")
    private String MONGO_USER_URI;
    @Value("${spring.data.mongodb.product.uri}")
    private String MONGO_PRODUCT_URI;
    @Value("${spring.data.mongodb.payment.uri}")
    private String MONGO_PAYMENT_URI;
    @Value("${spring.data.mongodb.inventory.uri}")
    private String MONGO_INVENTORY_URI;
    @Value("${spring.data.mongodb.promotion.uri}")
    private String MONGO_PROMOTION_URI;
    @Value("${spring.data.mongodb.statistics.uri}")
    private String MONGO_STATISTICS_URI;

    @Bean
    public MongoMappingContext mongoMappingContext() {
        MongoMappingContext mappingContext = new MongoMappingContext();
        return mappingContext;
    }

    // ==================== 连接到 mongodb1 服务器 ======================================

    @Bean //使用自定义的typeMapper去除写入mongodb时的“_class”字段
    public MappingMongoConverter mappingMongoConverter1() throws Exception {
        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(this.dbOrderFactory());
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, this.mongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

    @Bean
    @Primary
    public MongoDbFactory dbUserFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_USER_URI));
    }

    @Bean
    @Primary
    public MongoTemplate mongoUserTemplate() throws Exception {
        return new MongoTemplate(this.dbUserFactory(), this.mappingMongoConverter1());
    }

    @Bean
    public MongoDbFactory dbOrderFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_ORDER_URI));
    }

    @Bean
    public MongoTemplate mongoOrderTemplate() throws Exception {
        return new MongoTemplate(this.dbOrderFactory(), this.mappingMongoConverter1());
    }

    // ===================== 连接到 mongodb2 服务器 =================================
//不需要去除写入mongodb时的“_class”字段

/* //可以使用Mongo或MongoDbFactory两种对象进行配置
   @Bean
   public Mongo mongo2() throws Exception {
      return new MongoClient(new MongoClientURI(MONGO_DATA_SYNC_URI));
   }
   @Bean
   public MongoTemplate mongoMemberTemplate() throws Exception {
      return new MongoTemplate(mongo2(), "log2DbName");
   }
*/

    @Bean
    public MongoDbFactory dbMemberFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_MEMBER_URI));
    }

    @Bean
    public MongoTemplate mongoMemberTemplate() throws Exception {
        return new MongoTemplate(dbMemberFactory());
    }

    @Bean
    public MongoDbFactory dbProductFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_PRODUCT_URI));
    }

    @Bean
    public MongoTemplate mongoProductTemplate() throws Exception {
        return new MongoTemplate(dbProductFactory());
    }

    @Bean
    public MongoDbFactory dbPaymentFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_PAYMENT_URI));
    }

    @Bean
    public MongoTemplate mongoPaymentTemplate() throws Exception {
        return new MongoTemplate(dbPaymentFactory());
    }

    @Bean
    public MongoDbFactory dbInventoryFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_INVENTORY_URI));
    }

    @Bean
    public MongoTemplate mongoInventoryTemplate() throws Exception {
        return new MongoTemplate(dbInventoryFactory());
    }

    @Bean
    public MongoDbFactory dbPromotionFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_PROMOTION_URI));
    }

    @Bean
    public MongoTemplate mongoPromotionTemplate() throws Exception {
        return new MongoTemplate(dbPromotionFactory());
    }

    @Bean
    public MongoDbFactory dbStatisticsFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_STATISTICS_URI));
    }

    @Bean
    public MongoTemplate mongoStatisticsTemplate() throws Exception {
        return new MongoTemplate(dbStatisticsFactory());
    }
}
