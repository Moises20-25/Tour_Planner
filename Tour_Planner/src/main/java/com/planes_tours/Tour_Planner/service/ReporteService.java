package com.planes_tours.Tour_Planner.service;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Locale;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
@RequiredArgsConstructor
public class ReporteService {

	private final JdbcTemplate jdbcTemplate;

	public JasperPrint getJasperPrint(Map<String, Object> params, String reportPath) throws Exception {
		Connection conn = jdbcTemplate.getDataSource().getConnection();

		try {
			params.put(JRParameter.REPORT_LOCALE, Locale.forLanguageTag("es-PE"));

			// Compilar jrxml a jasper
			InputStream reportStream = this.getClass().getResourceAsStream(reportPath);
			JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

			// Llenar reporte
			return JasperFillManager.fillReport(jasperReport, params, conn);
		} finally {
			conn.close();
		}
	}
}
