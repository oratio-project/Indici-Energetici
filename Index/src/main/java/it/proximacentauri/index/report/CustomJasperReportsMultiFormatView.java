package it.proximacentauri.index.report;

import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

import net.sf.jasperreports.engine.JasperPrint;

public class CustomJasperReportsMultiFormatView extends JasperReportsMultiFormatView {
	
	@Override
	protected void renderReport(JasperPrint populatedReport, Map<String, Object> model, HttpServletResponse response) throws Exception {

		Properties contentDispositions = this.getContentDispositionMappings();

		contentDispositions.clear();

		String fileNameString = "attachment; filename=" + model.get("filename");
		contentDispositions.setProperty("csv", fileNameString + ".csv");
		contentDispositions.setProperty("xls", fileNameString + ".xls");
		contentDispositions.setProperty("html", fileNameString + ".html");
		contentDispositions.setProperty("pdf", fileNameString + ".pdf");

		super.renderReport(populatedReport, model, response);
	}

}
