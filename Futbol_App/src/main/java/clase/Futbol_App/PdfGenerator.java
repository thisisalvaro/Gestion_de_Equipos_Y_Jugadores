package clase.Futbol_App;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfGenerator {

    // Paleta de colores con tonos azules
    private static final BaseColor COLOR_ENCABEZADO = new BaseColor(0, 51, 102);      // Azul navy oscuro
    private static final BaseColor COLOR_TABLA_HEADER = new BaseColor(0, 102, 153);     // Azul intermedio
    private static final BaseColor COLOR_TABLA_ROW = BaseColor.WHITE;                 // Blanco
    private static final BaseColor COLOR_FONDO_SECCION = new BaseColor(236, 240, 241);  // Gris muy claro

    // Fuentes
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 26, Font.BOLD, BaseColor.WHITE);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    private static final Font SECTION_TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);

    public void generatePdf(List<Jugador> playerData, List<Equipo> teamData) {
        File reportsFolder = new File("Reports");
        if (!reportsFolder.exists() && !reportsFolder.mkdirs()) {
            System.err.println("No se pudo crear la carpeta Reports.");
            return;
        }

        File pdfFile = new File(reportsFolder, "Informe.pdf");
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(document, fos);
            document.open();

            // Encabezado principal
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            PdfPCell headerCell = new PdfPCell(new Phrase("Informe de Equipos y Jugadores", TITLE_FONT));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell.setBackgroundColor(COLOR_ENCABEZADO);
            headerCell.setPadding(15);
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(headerCell);
            document.add(headerTable);
            document.add(Chunk.NEWLINE);

            // Por cada equipo, se muestra su información y la lista de jugadores asociados
            if (teamData != null && !teamData.isEmpty()) {
                for (Equipo equipo : teamData) {
                    // Tabla de información del equipo
                    PdfPTable teamTable = new PdfPTable(2);
                    teamTable.setWidthPercentage(100);
                    teamTable.setSpacingBefore(10);
                    teamTable.setSpacingAfter(5);
                    teamTable.setWidths(new float[]{1, 3});

                    PdfPCell cell;

                    // Fila: ID
                    cell = new PdfPCell(new Phrase(String.valueOf(equipo.getId()), NORMAL_FONT));
                    cell.setBackgroundColor(COLOR_TABLA_ROW);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(8);
                    teamTable.addCell(cell);

                    // Fila: Nombre
                    cell = new PdfPCell(new Phrase("Nombre", HEADER_FONT));
                    cell.setBackgroundColor(COLOR_TABLA_HEADER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(8);
                    teamTable.addCell(cell);
                    cell = new PdfPCell(new Phrase(equipo.getNombre(), NORMAL_FONT));
                    cell.setBackgroundColor(COLOR_TABLA_ROW);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(8);
                    teamTable.addCell(cell);

                    // Fila: Ciudad
                    cell = new PdfPCell(new Phrase("Ciudad", HEADER_FONT));
                    cell.setBackgroundColor(COLOR_TABLA_HEADER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(8);
                    teamTable.addCell(cell);
                    cell = new PdfPCell(new Phrase(equipo.getCiudad(), NORMAL_FONT));
                    cell.setBackgroundColor(COLOR_TABLA_ROW);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(8);
                    teamTable.addCell(cell);

                    // Fila: Estadio
                    cell = new PdfPCell(new Phrase("Estadio", HEADER_FONT));
                    cell.setBackgroundColor(COLOR_TABLA_HEADER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(8);
                    teamTable.addCell(cell);
                    cell = new PdfPCell(new Phrase(equipo.getEstadio(), NORMAL_FONT));
                    cell.setBackgroundColor(COLOR_TABLA_ROW);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(8);
                    teamTable.addCell(cell);

                    document.add(teamTable);

                    // Título de la sección de jugadores
                    Paragraph playersTitle = new Paragraph("Jugadores:", SECTION_TITLE_FONT);
                    playersTitle.setSpacingBefore(5);
                    playersTitle.setSpacingAfter(5);
                    document.add(playersTitle);

                    // Tabla de jugadores para el equipo actual
                    PdfPTable playersTable = new PdfPTable(2);
                    playersTable.setWidthPercentage(100);
                    playersTable.setWidths(new float[]{3, 2});
                    playersTable.setSpacingAfter(10);

                    // Encabezado de la tabla de jugadores
                    cell = new PdfPCell(new Phrase("Nombre", HEADER_FONT));
                    cell.setBackgroundColor(COLOR_TABLA_HEADER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(8);
                    playersTable.addCell(cell);
                    cell = new PdfPCell(new Phrase("Posición", HEADER_FONT));
                    cell.setBackgroundColor(COLOR_TABLA_HEADER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(8);
                    playersTable.addCell(cell);

                    boolean hasPlayers = false;
                    for (Jugador jugador : playerData) {
                        if (jugador.getEquipoId() == equipo.getId()) {
                            hasPlayers = true;
                            cell = new PdfPCell(new Phrase(jugador.getNombre(), NORMAL_FONT));
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setPadding(8);
                            playersTable.addCell(cell);
                            cell = new PdfPCell(new Phrase(jugador.getPosicion(), NORMAL_FONT));
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setPadding(8);
                            playersTable.addCell(cell);
                        }
                    }
                    if (!hasPlayers) {
                        PdfPCell noPlayerCell = new PdfPCell(new Phrase("No hay jugadores asignados", NORMAL_FONT));
                        noPlayerCell.setColspan(2);
                        noPlayerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        noPlayerCell.setPadding(8);
                        playersTable.addCell(noPlayerCell);
                    }

                    document.add(playersTable);

                    // Línea separadora entre equipos
                    LineSeparator ls = new LineSeparator();
                    ls.setLineColor(BaseColor.LIGHT_GRAY);
                    document.add(new Chunk(ls));
                }
            } else {
                Paragraph noTeams = new Paragraph("No hay datos de equipos.", NORMAL_FONT);
                noTeams.setAlignment(Element.ALIGN_CENTER);
                document.add(noTeams);
            }

            // Pie de página
            Paragraph footer = new Paragraph("Generado correctamente.", NORMAL_FONT);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(20);
            document.add(footer);

            System.out.println("PDF generado exitosamente en: " + pdfFile.getAbsolutePath());
        } catch (DocumentException | IOException e) {
            System.err.println("Error generando el PDF: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    System.err.println("Error cerrando FileOutputStream: " + e.getMessage());
                }
            }
        }
    }
}

