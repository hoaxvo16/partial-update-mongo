package com.example.demo.repository;

import com.example.demo.models.RiskEntity;
import com.example.demo.repository.custom.RiskEntityCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskEntityRepository extends MongoRepository<RiskEntity, String>, RiskEntityCustomRepository {
    @Override
    <S extends RiskEntity> List<S> saveAll(Iterable<S> entities);

    @Override
    <S extends RiskEntity> S save(S entity);

    RiskEntity findFirstByProcessId(String processId);
}
