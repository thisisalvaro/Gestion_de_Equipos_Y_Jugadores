package clase.Futbol_App;

public class Jugador {
    private Integer id;
    private String nombre;
    private String posicion;
    private int equipoId;
    private String url;

    public Jugador(int id, String nombre, String posicion, int equipoId, String url) {
        this.id = id;
        this.nombre = nombre;
        this.posicion = posicion;
        this.equipoId = equipoId;
        this.url = url;
    }

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public Integer getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(int equipoId) {
        this.equipoId = equipoId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}