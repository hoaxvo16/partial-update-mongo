package com.example.demo.controller;

import com.example.demo.models.RiskEntity;
import com.example.demo.repository.RiskEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ThreadUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/high-modified-entity")
@RequiredArgsConstructor
@Slf4j
public class HighModifiedEntityController {
    private final RiskEntityRepository riskEntityRepository;

    @PostMapping
    public void gen() {
        IntStream.range(1, 10).forEach(x -> {
            RiskEntity riskEntity = new RiskEntity();
            riskEntity.setProcessId(UUID.randomUUID().toString());
            riskEntity.setTaskId(UUID.randomUUID().toString());
            riskEntity.setCreatedDate(new Date());
            riskEntity.setUpdateDate(new Date());
            riskEntityRepository.save(riskEntity);
        });
    }

    @DeleteMapping
    public void delete() {
        riskEntityRepository.deleteAll();
    }

    @PostMapping("/update")
    public void update(@RequestParam("processId") String processId) {
        riskEntityRepository.partialUpdate(processId, RiskEntity.FieldNames.taskId, UUID.randomUUID().toString());
    }

    @PostMapping("/concurrent-update-with-jpa-save")
    public void jpaSave(@RequestParam String processId) throws InterruptedException {

        RiskEntity riskEntity = riskEntityRepository.findFirstByProcessId(processId);
        String oldTaskId = riskEntity.getTaskId();
        AtomicReference<String> newTaskId = new AtomicReference<>();
        riskEntity.setUpdateDate(new Date());
//Run async save entity
        CompletableFuture.runAsync(() -> {
            RiskEntity riskEntity1 = riskEntityRepository.findFirstByProcessId(processId);
            newTaskId.getAndSet(UUID.randomUUID().toString());
            log.info("New task id {}", newTaskId.get());
            riskEntity1.setTaskId(newTaskId.get());
            riskEntityRepository.save(riskEntity1);
        });

        //Sleep 4 s before save
        ThreadUtils.sleep(Duration.ofSeconds(4));
        riskEntityRepository.save(riskEntity);
        Assert.isTrue(oldTaskId.equals(newTaskId.get()), "Task id not equal");
    }

    @PostMapping("/concurrent-update-with-partial-update")
    public void partialUpdate(@RequestParam String processId) throws InterruptedException {
        AtomicReference<String> newTaskId = new AtomicReference<>();
//Run async save entity
        CompletableFuture.runAsync(() -> {
            newTaskId.getAndSet(UUID.randomUUID().toString());
            log.info("New task id {}", newTaskId.get());
            riskEntityRepository.partialUpdate(processId, RiskEntity.FieldNames.taskId, newTaskId.get());
        });
        //Sleep 4 s before save
        ThreadUtils.sleep(Duration.ofSeconds(4));
        riskEntityRepository.partialUpdate(processId, RiskEntity.FieldNames.createdDate, new Date());
    }
}
