package clase.Futbol_App;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseConnection {

    private static final String CONNECTION_STRING = "jdbc:sqlserver://localhost:1433;"
            + "database=futbol;"
            + "encrypt=false;"
            + "user=sa;"
            + "password=admin";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING);
    }

    // CRUD Operations for Teams
    public void addEquipo(Equipo equipo) throws SQLException {
        String query = "INSERT INTO Equipos (id, nombre, ciudad, estadio, url) VALUES (?, ?, ?, ?, ?)";
        try (Connection cn = getConnection();
             PreparedStatement stmt = cn.prepareStatement(query)) {
            stmt.setInt(1, equipo.getId());
            stmt.setString(2, equipo.getNombre());
            stmt.setString(3, equipo.getCiudad());
            stmt.setString(4, equipo.getEstadio());
            stmt.setString(5, equipo.getUrl());
            stmt.executeUpdate();
        }
    }
    
    public int getMaxJugadorId() throws SQLException {
        int maxId = 0; // Valor inicial para el caso en que no haya jugadores
        String query = "SELECT MAX(id) AS maxId FROM jugadores";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                maxId = resultSet.getInt("maxId"); // Obtén el máximo ID de la tabla
            }
        }
        return maxId;
    }
    
    public int getMaxEquipoId() throws SQLException {
        int maxId = 0; // Valor inicial para el caso en que no haya jugadores
        String query = "SELECT MAX(id) AS maxId FROM Equipos";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                maxId = resultSet.getInt("maxId"); // Obtén el máximo ID de la tabla
            }
        }
        return maxId;
    }

    public List<Equipo> getListaEquipos() throws SQLException {
        String query = "SELECT * FROM Equipos";
        List<Equipo> listaEquipos = new ArrayList<>();

        try (Connection cn = getConnection();
             PreparedStatement stmt = cn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String ciudad = rs.getString("ciudad");
                String estadio = rs.getString("estadio");
                String url = rs.getString("url");

                listaEquipos.add(new Equipo(id, nombre, ciudad, estadio, url));
            }
        }

        return listaEquipos;
    }

    public void updateEquipo(Equipo equipo) throws SQLException {
        String query = "UPDATE Equipos SET nombre = ?, ciudad = ?, estadio = ?, url = ? WHERE id = ?";
        try (Connection cn = getConnection();
             PreparedStatement stmt = cn.prepareStatement(query)) {
            stmt.setString(1, equipo.getNombre());
            stmt.setString(2, equipo.getCiudad());
            stmt.setString(3, equipo.getEstadio());
            stmt.setString(4, equipo.getUrl());
            stmt.setInt(5, equipo.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteEquipo(int id) throws SQLException {
        String query = "DELETE FROM Equipos WHERE id = ?";
        try (Connection cn = getConnection();
             PreparedStatement stmt = cn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // CRUD Operations for Players
    public void addJugador(Jugador jugador) throws SQLException {
        String query = "INSERT INTO Jugadores (id, nombre, posicion, equipo_id, url) VALUES (?, ?, ?, ?, ?)";
        try (Connection cn = getConnection();
             PreparedStatement stmt = cn.prepareStatement(query)) {
            stmt.setInt(1, jugador.getId());
            stmt.setString(2, jugador.getNombre());
            stmt.setString(3, jugador.getPosicion());
            stmt.setInt(4, jugador.getEquipoId());
            stmt.setString(5, jugador.getUrl());
            stmt.executeUpdate();
        }
    }

    public List<Jugador> getListaJugadores() throws SQLException {
        String query = "SELECT * FROM Jugadores";
        List<Jugador> listaJugadores = new ArrayList<>();

        try (Connection cn = getConnection();
             PreparedStatement stmt = cn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String posicion = rs.getString("posicion");
                int equipoId = rs.getInt("equipo_id");
                String url = rs.getString("url");

                listaJugadores.add(new Jugador(id, nombre, posicion, equipoId, url));
            }
        }

        return listaJugadores;
    }

    public void updateJugador(Jugador jugador) throws SQLException {
        String query = "UPDATE Jugadores SET nombre = ?, posicion = ?, equipo_id = ?, url = ? WHERE id = ?";
        try (Connection cn = getConnection();
             PreparedStatement stmt = cn.prepareStatement(query)) {
            stmt.setString(1, jugador.getNombre());
            stmt.setString(2, jugador.getPosicion());
            stmt.setInt(3, jugador.getEquipoId());
            stmt.setString(4, jugador.getUrl());
            stmt.setInt(5, jugador.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteJugador(int id) throws SQLException {
        String query = "DELETE FROM Jugadores WHERE id = ?";
        try (Connection cn = getConnection();
             PreparedStatement stmt = cn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
