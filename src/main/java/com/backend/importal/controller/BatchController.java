package com.backend.importal.controller;

import com.backend.importal.entity.Batch;
import com.backend.importal.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
@CrossOrigin
public class BatchController {

    @Autowired
    private BatchRepository batchRepository;

    //Get all batches through batch repository
    @GetMapping
    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }
}