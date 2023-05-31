package com.example.datacentermonitoringapi.domain.data;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/data")
@RequiredArgsConstructor
public class DataController {
    private final DataService dataService;

    @GetMapping("/{sensorId}")
    public ResponseEntity<List<DataResponse>> getDataBySensorId(@PathVariable("sensorId") Long sensorId) {
        return ResponseEntity.ok(dataService.findBySensorId(sensorId));
    }

    @PostMapping("/{sensorId}")
    public ResponseEntity<Void> postData(@PathVariable("sensorId") Long sensorId,
                                         @RequestBody double value) {
        dataService.addValue(sensorId, value);
        return ResponseEntity.ok().build();
    }

}
