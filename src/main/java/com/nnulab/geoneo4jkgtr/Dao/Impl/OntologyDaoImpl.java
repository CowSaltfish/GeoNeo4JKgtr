package com.nnulab.geoneo4jkgtr.Dao.Impl;

import com.mongodb.WriteResult;
import com.nnulab.geoneo4jkgtr.Dao.OntologyDao;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : LiuXianYu
 * @date : 2022/12/1 22:03
 */
@Repository
public class OntologyDaoImpl implements OntologyDao {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 添加数据
     * save ⽅法中会进⾏判断，如果对象包含了 ID 信息就会进⾏更新，如果没有包含 ID 信息就⾃动保存。
     *
     * @param ontology
     */
    @Override
    public void save(KnowledgeGraph ontology) {
        mongoTemplate.save(ontology);
    }

    /**
     * 根据⽤户名查询对象
     *
     * @param name
     * @return
     */
    @Override
    public KnowledgeGraph findByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        KnowledgeGraph ontology = mongoTemplate.findOne(query, KnowledgeGraph.class);
        return ontology;
    }


    /**
     * 更新对象
     * 可以选择更新⼀条数据，或者更新多条数据
     *
     * @param name
     * @param ontology
     * @return
     */
    @Override
    public long updateOntology(String name, KnowledgeGraph ontology) {
        Query query = new Query(Criteria.where("name").is(name));
        //todo:修改字段值
        Update update = new Update().set("key", ontology.getName());
        //更新查询返回结果集的第⼀条
        WriteResult result = mongoTemplate.updateFirst(query, update, KnowledgeGraph.class);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,Ontology.class);
        if (result != null)
            return result.getN();
        else
            return 0;
    }

    /**
     * 删除对象
     *
     * @param name
     */
    @Override
    public void deleteOntologyByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        mongoTemplate.remove(query, KnowledgeGraph.class);
    }
}
