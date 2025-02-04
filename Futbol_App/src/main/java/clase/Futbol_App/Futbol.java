package clase.Futbol_App;

public class Futbol {
	int id;
	String nombre;
	String posicion;
	int equipo_id ;
	String url_img;
	
	public Futbol(int id, String nombre, String posicion, int equipo_id, String url_img) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.posicion = posicion;
		this.equipo_id = equipo_id;
		this.url_img = url_img;
	}
	public int getId() {
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
	public int getEquipo_id() {
		return equipo_id;
	}
	public void setEquipo_id(int equipo_id) {
		this.equipo_id = equipo_id;
	}
	public String getUrl_img() {
		return url_img;
	}
	public void setUrl_img(String url_img) {
		this.url_img = url_img;
	}
	
	
	
}
