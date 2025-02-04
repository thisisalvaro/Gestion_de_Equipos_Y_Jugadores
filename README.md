# Gestion_de_Equipos_Y_Jugadores
Sistema de Gestión de Equipos de Fútbol

Descripción

Este proyecto es una aplicación de escritorio en Java utilizando Swing y JDBC para gestionar equipos de fútbol. Implementa las operaciones CRUD para administrar información sobre los equipos y jugadores.

Características

CRUD de equipos y jugadores ⚽

Visualización de gráficos de barras con la cantidad de jugadores por equipo 📊

Integración con Power BI para gráficos interactivos 📈

Exportación de datos a PDF y Excel 📄📊

Interfaz gráfica intuitiva y accesible 🎨

Filtros avanzados en tablas por ciudad o posición 🔍

Imágenes de escudos de equipos y fotos de jugadores 🏆📷

Tecnologías Utilizadas

Lenguaje: Java (Swing, JDBC)

Base de Datos: MySQL

Bibliotecas: JasperReports (PDF), Apache POI (Excel)

Gráficos: JFreeChart / Power BI

ORM: JDBC puro

Estructura de la Base de Datos

Tabla Equipos

CREATE TABLE Equipos (
    id INT PRIMARY KEY,
    nombre NVARCHAR(50) NOT NULL,
    ciudad NVARCHAR(50),
    estadio NVARCHAR(50)
);

Tabla Jugadores

CREATE TABLE Jugadores (
    id INT PRIMARY KEY,
    nombre NVARCHAR(50) NOT NULL,
    posicion NVARCHAR(20),
    equipo_id INT,
    FOREIGN KEY (equipo_id) REFERENCES Equipos(id) ON DELETE CASCADE
);

Instalación

Clona el repositorio:

git clone https://github.com/tu_usuario/Sistema_Gestion_Equipos_Futbol.git
cd Sistema_Gestion_Equipos_Futbol

Configura la base de datos en MySQL e importa las tablas anteriores.

Compila y ejecuta la aplicación:

mvn clean package
java -jar target/Sistema_Gestion_Equipos_Futbol.jar

Uso de la Aplicación

Filtra jugadores por equipo utilizando un ComboBox.

Visualiza la información detallada de cada jugador.

Genera informes en PDF y Excel.

Explora gráficos interactivos con Power BI.

Accesos rápidos de teclado para una mejor usabilidad.

Contribuciones

Las contribuciones son bienvenidas. Para colaborar:

Haz un fork del repositorio.

Crea una nueva rama (git checkout -b feature-nueva).

Realiza tus cambios y haz commit (git commit -m 'Añadida nueva funcionalidad').

Envía un Pull Request.

Licencia

Este proyecto está bajo la licencia MIT. Puedes ver más detalles en el archivo LICENSE.

Contacto

Si tienes dudas o sugerencias, contáctame en [tu email] o crea un issue en el repositorio.

