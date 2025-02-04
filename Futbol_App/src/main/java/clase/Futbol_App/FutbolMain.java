package clase.Futbol_App;

import java.awt.GridLayout;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;



public class FutbolMain extends JFrame {

    public FutbolMain() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10)); // 4 filas, 1 columna, espacio de 10px entre botones
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Margen alrededor del panel
        DataBaseConnection dbConn = new DataBaseConnection();

        String[] listaColumnaJugador = new String[]{"ID", "Nombre", "Posición", "Equipo ID", "Imagen"};
        String[] listaColumnaEquipo = new String[]{"ID", "Nombre", "Ciudad", "Estadio", "Imagen"};

        // Crear los botones
        JButton boton1 = new JButton("Jugador");
        JButton boton2 = new JButton("Equipo");
        JButton boton3 = new JButton("Excel");
        JButton boton4 = new JButton("Power Point");

        // Añadir los botones al panel
        panel.add(boton1);
        panel.add(boton2);
        panel.add(boton3);
        panel.add(boton4);

        JMenuBar bar = new JMenuBar();
        JMenu MENU = new JMenu("Acciones");
        bar.add(MENU);

        JMenuItem boton11 = new JMenuItem("Lista de jugadores");
        JMenuItem boton22 = new JMenuItem("Lista de Equipo");
        JMenuItem boton33 = new JMenuItem("Excel");
        JMenuItem boton44 = new JMenuItem("Pdf");
        MENU.add(boton11);
        MENU.add(boton22);
        MENU.add(boton33);
        MENU.add(boton44);

        setJMenuBar(bar);
        setContentPane(panel);

        boton11.addActionListener(e -> cambiarPanel(new JugadorPanel()));
        boton1.addActionListener(e -> cambiarPanel(new JugadorPanel()));

        boton2.addActionListener(e -> cambiarPanel(new EquipoPanel()));
        boton22.addActionListener(e -> cambiarPanel(new EquipoPanel()));

        boton3.addActionListener(e -> generarExcel(dbConn, listaColumnaJugador, listaColumnaEquipo));
        boton33.addActionListener(e -> generarExcel(dbConn, listaColumnaJugador, listaColumnaEquipo));

        boton4.addActionListener(e -> generarPdf(dbConn, listaColumnaJugador, listaColumnaEquipo));
        boton44.addActionListener(e -> generarPdf(dbConn, listaColumnaJugador, listaColumnaEquipo));

        setTitle("Aplicación Principal");
        setLocationRelativeTo(null);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void cambiarPanel(JPanel panel) {
        setContentPane(panel);
        repintar();
    }

    private void generarExcel(DataBaseConnection dbConn, String[] listaColumnaJugador, String[] listaColumnaEquipo) {
        try {
            new ExcelGenerator().generateEquiposReport(dbConn.getListaEquipos(), dbConn.getListaJugadores());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        repintar();
    }

    private void generarPdf(DataBaseConnection dbConn, String[] listaColumnaJugador, String[] listaColumnaEquipo) {
        try {
            new PdfGenerator().generatePdf( dbConn.getListaJugadores(), dbConn.getListaEquipos());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        repintar();
    }

    private void repintar() {
        setSize(800, 601);
        repaint();
        setSize(800, 600);
    }

    public static void main(String[] args) {
    	  SplashScreen screen = new SplashScreen();
          screen.setVisible(true);
          for (int i = 0; i <= 100; i++) {
              try {
                  Thread.sleep(50);
                  screen.updateProgress(i);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
          screen.dispose();

          new FutbolMain();
    }
}