package com.ting.R3S.Projectedsales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost")
@RequestMapping("/projectedsales")
public class ProjectedsalesController {

    @Autowired
    private ProjectedsalesService projectedsalesService;

    @PostMapping("/import-csv")
    public ResponseEntity<String> importCsvData(@RequestParam("file") MultipartFile file) {
        boolean importSuccess = projectedsalesService.importCsvData(file);
        if (importSuccess) {
            return ResponseEntity.ok("CSV data imported successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to import CSV data");
        }
    }

}
