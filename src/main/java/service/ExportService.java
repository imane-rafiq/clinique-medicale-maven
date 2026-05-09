package service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.FileOutputStream;

public class ExportService {

    public static void exportExcel(JTable table) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter en Excel");

        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();

            if (!path.endsWith(".xlsx")) {
                path += ".xlsx";
            }

            try {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Rendez-vous");

                TableModel model = table.getModel();

                Row header = sheet.createRow(0);

                for (int col = 0; col < model.getColumnCount(); col++) {
                    header.createCell(col).setCellValue(model.getColumnName(col));
                }

                for (int row = 0; row < model.getRowCount(); row++) {
                    Row excelRow = sheet.createRow(row + 1);

                    for (int col = 0; col < model.getColumnCount(); col++) {
                        Object value = model.getValueAt(row, col);
                        excelRow.createCell(col).setCellValue(value == null ? "" : value.toString());
                    }
                }

                for (int col = 0; col < model.getColumnCount(); col++) {
                    sheet.autoSizeColumn(col);
                }

                FileOutputStream out = new FileOutputStream(path);
                workbook.write(out);

                out.close();
                workbook.close();

                JOptionPane.showMessageDialog(null, "Export Excel réussi !");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erreur export Excel : " + e.getMessage());
            }
        }
    }

    public static void exportPDF(JTable table) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter en PDF");

        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();

            if (!path.endsWith(".pdf")) {
                path += ".pdf";
            }

            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(path));

                document.open();

                document.add(new Paragraph("Liste des rendez-vous"));
                document.add(new Paragraph(" "));

                TableModel model = table.getModel();
                PdfPTable pdfTable = new PdfPTable(model.getColumnCount());

                for (int col = 0; col < model.getColumnCount(); col++) {
                    pdfTable.addCell(model.getColumnName(col));
                }

                for (int row = 0; row < model.getRowCount(); row++) {
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        Object value = model.getValueAt(row, col);
                        pdfTable.addCell(value == null ? "" : value.toString());
                    }
                }

                document.add(pdfTable);
                document.close();

                JOptionPane.showMessageDialog(null, "Export PDF réussi !");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erreur export PDF : " + e.getMessage());
            }
        }
    }
}