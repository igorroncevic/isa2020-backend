package team18.pharmacyapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.Report;
import team18.pharmacyapp.model.dtos.ReportCreateDTO;
import team18.pharmacyapp.model.medicine.ReportMedicines;
import team18.pharmacyapp.service.interfaces.ReportMedicinesService;
import team18.pharmacyapp.service.interfaces.ReportService;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "api/reports")
public class ReportController {
    private final ReportService reportService;
    private final ReportMedicinesService reportMedicinesService;

    @Autowired
    public ReportController(ReportService reportService, ReportMedicinesService reportMedicinesService) {
        this.reportService = reportService;
        this.reportMedicinesService = reportMedicinesService;
    }

    @PostMapping
    public ResponseEntity<Report> save(@RequestBody ReportCreateDTO report){
        return  new ResponseEntity<>(reportService.createNew(report), HttpStatus.CREATED);
    }

    @PostMapping("reportMedicine")
    public ResponseEntity<ReportMedicines> saveReportMedicine(@RequestBody ReportMedicines reportMedicines){
        return  new ResponseEntity<>(reportMedicinesService.save(reportMedicines), HttpStatus.CREATED);
    }
}
