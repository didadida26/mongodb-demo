package com.zjn.mongodb;

import com.zjn.mongodb.entity.Person;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringBootTest
public class MongoTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 插入文档
     */
    @Test
    public void save(){
        for (int i = 0; i < 10; i++) {
            Person person = new Person();
            person.setId(ObjectId.get());
            person.setName(UUID.randomUUID().toString() + i);
            person.setAddress(UUID.randomUUID().toString() + i);
            person.setAge(new Random().nextInt(10) + i);

            mongoTemplate.save(person);
        }
    }

    /**
     * 查询所有
     */
    @Test
    public void findAll(){
        List<Person> list = mongoTemplate.findAll(Person.class);
        for (Person person : list) {
            System.out.println(person);
        }
    }

    @Test // 查询年龄小于10岁的
    public void criterea(){

        // 构造查询条件
        Query query = new Query(Criteria.where("age").lt(10));

        List<Person> list = mongoTemplate.find(query, Person.class);

        for (Person person : list) {
            System.out.println(person);
        }
    }

    /**
     * 分页条件查询
     */
    @Test
    public void page(){
        // 查询总数
        Criteria criteria = Criteria.where("age").lt(10);
        Query queryCount = new Query(criteria);
        long count = mongoTemplate.count(queryCount, Person.class);
        System.out.println("总数："+count);

        Query queryLimit = new Query(criteria)
                .limit(2) // 每页条数
                .skip(2); // (page-1)*pageSize
        List<Person> list = mongoTemplate.find(queryLimit, Person.class);
        for (Person person : list) {
            System.out.println(person);
        }
    }

    /**
     * 更新
     */
    @Test
    public void update(){
        // 查询条件
        Query query = new Query(Criteria.where("id").is("63368917e01589016d4a24a6"));

        Update update = new Update();
        update.set("age", 30);
        mongoTemplate.updateFirst(query, update, Person.class);

        // 更新后查询
        List<Person> list = mongoTemplate.find(query, Person.class);
        for (Person person : list) {
            System.out.println(person);
        }
    }

    /**
     * 删除
     */
    @Test
    public void remove(){
        Query query = new Query(Criteria.where("id").is("63368917e01589016d4a24a6"));

        mongoTemplate.remove(query, Person.class);

        // 删除后就查询不到了
        List<Person> list = mongoTemplate.find(query, Person.class);
        System.out.println(list);
    }
}
