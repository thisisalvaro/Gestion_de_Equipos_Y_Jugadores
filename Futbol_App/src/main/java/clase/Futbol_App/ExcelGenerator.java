package clase.Futbol_App;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelGenerator {

    public static void generateEquiposReport(List<Equipo> equipos, List<Jugador> jugadores) {
        // Crear el directorio Reports si no existe
        File directorio = new File("Reports");
        if (!directorio.exists()){
            directorio.mkdir();
        }
        String dest = "Reports/equipos_report.xlsx";

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte Equipos");
            sheet.setColumnWidth(0, 20 * 256);
            sheet.setColumnWidth(1, 30 * 256);
            sheet.setColumnWidth(2, 25 * 256);

            CellStyle headerStyle = createHeaderCellStyle(workbook);
            CellStyle textStyle = createTextCellStyle(workbook);

            int rowNum = 0;
            for (Equipo e : equipos) {
                // Filtrar los jugadores que pertenecen al equipo actual
                List<Jugador> jugadoresEquipo = jugadores.stream()
                        .filter(j -> j.getEquipoId() == e.getId())
                        .collect(Collectors.toList());

                // Datos del equipo
                Row equipoRow = sheet.createRow(rowNum++);
                equipoRow.createCell(0).setCellValue("Equipo:");
                equipoRow.getCell(0).setCellStyle(textStyle);
                equipoRow.createCell(1).setCellValue(e.getNombre());
                equipoRow.getCell(1).setCellStyle(textStyle);

                Row ciudadRow = sheet.createRow(rowNum++);
                ciudadRow.createCell(0).setCellValue("Ciudad:");
                ciudadRow.getCell(0).setCellStyle(textStyle);
                ciudadRow.createCell(1).setCellValue(e.getCiudad());
                ciudadRow.getCell(1).setCellStyle(textStyle);

                Row estadioRow = sheet.createRow(rowNum++);
                estadioRow.createCell(0).setCellValue("Estadio:");
                estadioRow.getCell(0).setCellStyle(textStyle);
                estadioRow.createCell(1).setCellValue(e.getEstadio());
                estadioRow.getCell(1).setCellStyle(textStyle);

                // Encabezado de la tabla de jugadores
                Row headerRow = sheet.createRow(rowNum++);
                headerRow.createCell(0).setCellValue("#");
                headerRow.createCell(1).setCellValue("Nombre");
                headerRow.createCell(2).setCellValue("Posición");

                headerRow.getCell(0).setCellStyle(headerStyle);
                headerRow.getCell(1).setCellStyle(headerStyle);
                headerRow.getCell(2).setCellStyle(headerStyle);

                // Si no hay jugadores para el equipo, se muestra un mensaje
                if (jugadoresEquipo.isEmpty()) {
                    Row sinJugadoresRow = sheet.createRow(rowNum++);
                    sinJugadoresRow.createCell(0).setCellValue("No hay jugadores aún");
                    sinJugadoresRow.getCell(0).setCellStyle(textStyle);
                } else {
                    int count = 1;
                    for (Jugador j : jugadoresEquipo) {
                        Row jugadorRow = sheet.createRow(rowNum++);
                        jugadorRow.createCell(0).setCellValue(count++);
                        jugadorRow.getCell(0).setCellStyle(textStyle);
                        jugadorRow.createCell(1).setCellValue(j.getNombre());
                        jugadorRow.getCell(1).setCellStyle(textStyle);
                        jugadorRow.createCell(2).setCellValue(j.getPosicion());
                        jugadorRow.getCell(2).setCellStyle(textStyle);
                    }
                }
                // Espacio entre equipos
                rowNum++;
            }

            // Guardar el archivo Excel
            try (FileOutputStream fileOut = new FileOutputStream(dest)) {
                workbook.write(fileOut);
            }
            System.out.println("Excel generado en: " + new File(dest).getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private static CellStyle createTextCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}
