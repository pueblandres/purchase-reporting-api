package com.example.purchasereportingapi.reporting.service.impl;

import com.example.purchasereportingapi.exception.ReportGenerationException;
import com.example.purchasereportingapi.reporting.service.JasperReportExportService;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JasperReportExportServiceImpl implements JasperReportExportService {

    @Override
    public byte[] exportPdf(String templatePath, Map<String, Object> parameters, Collection<?> rows) {
        try (InputStream templateStream = new ClassPathResource(templatePath).getInputStream()) {
            JasperReport report = JasperCompileManager.compileReport(templateStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rows);

            if (!parameters.containsKey(JRParameter.REPORT_LOCALE)) {
                parameters.put(JRParameter.REPORT_LOCALE, java.util.Locale.forLanguageTag("es-AR"));
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException | IOException ex) {
            log.error("No se pudo generar el PDF del reporte usando la plantilla {}", templatePath, ex);
            throw new ReportGenerationException("No se pudo generar el PDF del reporte");
        }
    }
}
