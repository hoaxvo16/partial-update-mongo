package com.example.demo.repository.custom;

public interface RiskEntityCustomRepository {
    void partialUpdate(String processId, String fieldName, Object data);
//
//    <S extends RiskEntity> List<S> saveAll(Iterable<S> entities);
//
//    <S extends RiskEntity> S save(S entity);
}
