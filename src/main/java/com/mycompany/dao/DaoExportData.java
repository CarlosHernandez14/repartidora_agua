/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dao;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.domain.EstadoPedido;
import com.mycompany.domain.Pedido;
import com.mycompany.domain.Repartidor;
import com.mycompany.domain.Zona;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DaoExportData {

    public static void generarReporteExcelZonas(List<Zona> zonas, List<Pedido> pedidos) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            String folderPath = fileChooser.getSelectedFile().getAbsolutePath();
            String filePath = Paths.get(folderPath, "ZonasConMasPedidos.xlsx").toString();

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Zonas con Más Pedidos");

                // Encabezados de las columnas
                String[] encabezados = {"Zona", "Coordenadas X", "Coordenadas Y", "Total de Pedidos"};
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < encabezados.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(encabezados[i]);
                }

                // Contar pedidos por zona
                Map<Integer, Long> pedidosPorZona = pedidos.stream()
                        .collect(Collectors.groupingBy(Pedido::getIdZona, Collectors.counting()));

                // Agregar los datos de las zonas
                int rowIdx = 1;
                for (Zona zona : zonas) {
                    long totalPedidos = pedidosPorZona.getOrDefault(zona.getIdZona(), 0L);

                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(zona.getNombre());
                    row.createCell(1).setCellValue(zona.getCoordenadas_x());
                    row.createCell(2).setCellValue(zona.getCoordenadas_y());
                    row.createCell(3).setCellValue(totalPedidos);
                }

                // Escribir el archivo Excel
                try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                    workbook.write(fileOut);
                }

                JOptionPane.showMessageDialog(null, "El reporte Excel se ha generado correctamente en: " + filePath);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se seleccionó un directorio válido.");
        }
    }

    public static void generarReportePDFRepartidores(List<Repartidor> repartidores, List<Pedido> pedidos) {
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = folderChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = folderChooser.getSelectedFile();
            String filePath = selectedFolder.getAbsolutePath() + "/RepartidoresConPedidosPendientes.pdf";

            try {
                // Crear el documento PDF
                Document pdfDoc = new Document(PageSize.A4);
                PdfWriter.getInstance(pdfDoc, new FileOutputStream(filePath));

                pdfDoc.open();

                // Agregar un título al documento
                pdfDoc.add(new Paragraph("Reporte de Repartidores con Pedidos Pendientes"));
                pdfDoc.add(new Paragraph("Fecha: " + java.time.LocalDate.now().toString()));
                pdfDoc.add(new Paragraph("\n\n"));

                // Contar pedidos pendientes por repartidor
                Map<Integer, Long> pedidosPendientesPorRepartidor = pedidos.stream()
                        .filter(p -> p.getEstado() == EstadoPedido.PENDIENTE)
                        .collect(Collectors.groupingBy(Pedido::getIdRepartidor, Collectors.counting()));

                // Agregar datos de los repartidores al documento
                for (Repartidor repartidor : repartidores) {
                    long pedidosPendientes = pedidosPendientesPorRepartidor.getOrDefault(repartidor.getIdRepartidor(), 0L);

                    if (pedidosPendientes > 0) {
                        pdfDoc.add(new Paragraph("Nombre: " + repartidor.getNombre_completo()));
                        pdfDoc.add(new Paragraph("Teléfono: " + repartidor.getTelefono()));
                        pdfDoc.add(new Paragraph("ID del Camión: " + repartidor.getIdCamion()));
                        pdfDoc.add(new Paragraph("Pedidos Pendientes: " + pedidosPendientes));
                        pdfDoc.add(new Paragraph("\n"));
                    }
                }

                pdfDoc.close();
                JOptionPane.showMessageDialog(null, "El reporte PDF se ha generado correctamente en: " + filePath);

            } catch (FileNotFoundException | DocumentException ex) {
                JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se seleccionó un directorio válido.");
        }
    }

}
