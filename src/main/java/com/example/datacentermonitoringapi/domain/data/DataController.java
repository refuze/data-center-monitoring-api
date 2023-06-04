package com.example.datacentermonitoringapi.domain.data;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/data")
@RequiredArgsConstructor
public class DataController {
    private final DataService dataService;

    @GetMapping("/all/{sensorId}")
    public ResponseEntity<List<DataResponse>> getDataBySensorId(@PathVariable("sensorId") long sensorId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "" + Integer.MAX_VALUE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(dataService.findBySensorId(sensorId, pageable));
    }

    @GetMapping("/error/{sensorId}")
    public ResponseEntity<List<DataResponse>> getErrorDataBySensorId(@PathVariable("sensorId") long sensorId,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "" + Integer.MAX_VALUE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(dataService.findErrorBySensorId(sensorId, pageable));
    }

    @GetMapping("/actual/{sensorId}")
    public ResponseEntity<List<DataResponse>> getActualDataBySensorId(@PathVariable("sensorId") long sensorId) {
        return ResponseEntity.ok(dataService.findActualBySensorId(sensorId));
    }

    @PostMapping
    public ResponseEntity<Void> postData(@RequestParam("value") double value) {
        dataService.addValueAndNotify(value);
        return ResponseEntity.ok().build();
    }

}
