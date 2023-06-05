package com.nnulab.geoneo4jkgtr.Util;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.result.UpdateResult;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;

/**
 * @author : LiuXianYu
 * @date : 2023/5/15 15:49
 */
public class MongoDBUtil {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 添加数据
     * save方法中会进行判断，如果对象包含了 ID 信息就会进行更新，如果没有包含 ID 信息就自动保存。
     *
     * @param ontology
     */
    public void save(Object ontology) {
        mongoTemplate.save(ontology);
    }

    /**
     * 根据用户名查询对象
     *
     * @param name
     * @return
     */
    public KnowledgeGraph findByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, KnowledgeGraph.class);
    }


    /**
     * 更新对象
     * 可以选择更新⼀条数据，或者更新多条数据
     *
     * @param name
     * @param ontology
     * @return
     */
    public long updateOntology(String name, KnowledgeGraph ontology) {
        Query query = new Query(Criteria.where("name").is(name));
        //todo:修改字段值
        Update update = new Update().set("key", ontology.getName());
        //更新查询返回结果集的第⼀条
        UpdateResult result = mongoTemplate.updateFirst(query, update, KnowledgeGraph.class);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,Ontology.class);
//        if (result != null) {
        return result.getModifiedCount();
//        }
//        return 0;
    }

    /**
     * 删除对象
     *
     * @param name
     */
    public void deleteOntologyByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        mongoTemplate.remove(query, KnowledgeGraph.class);
    }
}
