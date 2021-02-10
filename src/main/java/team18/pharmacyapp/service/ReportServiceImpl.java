package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Report;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.ReportCreateDTO;
import team18.pharmacyapp.repository.CheckupRepository;
import team18.pharmacyapp.repository.ReportRepository;
import team18.pharmacyapp.service.interfaces.CheckupService;
import team18.pharmacyapp.service.interfaces.ReportService;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final CheckupService checkupService;
    private final CheckupRepository checkupRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository, CheckupService checkupService, CheckupRepository checkupRepository) {
        this.reportRepository = reportRepository;
        this.checkupService = checkupService;
        this.checkupRepository = checkupRepository;
    }

    @Override
    public Report save(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public Report createNew(ReportCreateDTO report) {
        Report r = new Report();
        //Term term = checkupService.findOne(report.getTermId());
        Term term = checkupRepository.findById(report.getTermId()).orElseGet(null);
        r.setTerm(term);
        r.setText(report.getText());
        Report saved = save(r);
        term.setReport(saved);
        checkupService.save(term);
        return saved;
    }
}
