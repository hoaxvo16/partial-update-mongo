package com.example.demo.repository.custom;

import com.example.demo.models.RiskEntity;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;


@Repository
@RequiredArgsConstructor
@Slf4j
public class RiskEntityCustomRepositoryImpl implements RiskEntityCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void partialUpdate(String processId, String fieldName, Object data) {

        RiskEntity.validatePartialUpdate(fieldName, data);
        Query query = new Query(Criteria.where(RiskEntity.FieldNames.processId).is(processId));
        Update update = new Update()
                .set(fieldName, data)
                .set(RiskEntity.FieldNames.updateDate, new Date());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, RiskEntity.class);
        log.info("Partial update process {} with fieldName {}, data {}, result {}", processId, fieldName, data, updateResult);
    }


}
