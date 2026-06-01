package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ConexionBD {

	public static Connection cn;
	public static PreparedStatement pstmt;
	public static ResultSet rs;
	private static final String URL = "jdbc:ucanaccess://src/main/resources/Sanidad.accdb";

	
	public ConexionBD() {
		// TODO Auto-generated constructor stub
	}
	

	protected static void cerrarConexionDML(String str){
		try {
			cn.close();
			pstmt.close();
		} catch(SQLException clo) {
			System.out.println(str);
		}
	}

	public static void conectar() {
		try {
			cn = DriverManager.getConnection(URL);
		} catch (SQLException e) {
			System.out.println("Error al abrir la base de datos");
		}
	}
	
	public static Connection getConexion() { // Siempre abierta sin try()
		try {
			return DriverManager.getConnection(URL);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Conexión incorrecta");
			return null; // Retorna null si no puede establecer la conexión
		}
	}

	public static DefaultTableModel actualizarTabla(DefaultTableModel dtm) {
		try {
			conectar();
			pstmt = cn.prepareStatement("SELECT * FROM Sanidad");
			rs = pstmt.executeQuery();
			if (!rs.isBeforeFirst())
				JOptionPane.showMessageDialog(null, "No se encontraron datos con esa marca", "Sin datos",
						JOptionPane.INFORMATION_MESSAGE);
			else {
				dtm.setRowCount(0);
				while (rs.next()) {
					 dtm.addRow(new Object[] {
					 rs.getInt(1),
					 rs.getString(2),
					 rs.getInt(3),
					 rs.getInt(4),
					 rs.getString(5),
					 rs.getBoolean(6)?"Sí":"No"
					 });
				}
			}
		} catch(SQLException sl) {
			JOptionPane.showMessageDialog(null, "Error al conectarse a la Base de datos.");
		} finally {
			try {
				cn.close();
				pstmt.close();
				rs.close();
			} catch(SQLException clo) {
				System.out.println("Error al cerrar la base de datos.");
			}
		}
		return dtm;
	}
		
	public static int InsertarDatos(Paciente pc) {
		ResultSet rs;
		int i = -1;
		try {
			conectar();
			pstmt=cn.prepareStatement("INSERT INTO Sanidad (DNI, Nombre, [Nº de expediente], Edad, Especialidad, Ingresado) Values (?,?,?,?,?,?)");
			pstmt.setString(1, pc.dni);	//inserto datos del paciente
			pstmt.setString(2, pc.nombre);
			pstmt.setInt(3, pc.expediente);		
			pstmt.setInt(4, pc.edad);
			pstmt.setString(5, pc.especialidad);
			pstmt.setBoolean(6, pc.ingresado);
			pstmt.executeUpdate();		//ejecuto la instruccion en la BBDD.
			
			pstmt=cn.prepareStatement("SELECT Id FROM Sanidad WHERE DNI = ?"); 	//Ahora, busco el ID para insertarlo al DTM
			pstmt.setString(1, pc.dni);
			rs = pstmt.executeQuery();
			rs.next(); 	//para apuntar al resultado de la consulta.
			i = rs.getInt(1);
		} catch (SQLException in) {
			JOptionPane.showMessageDialog(null, "Error en la conexión ó ejecución con la base de datos.");
		} finally {
			cerrarConexionDML("Error al cerrar la conexion e insercion en la BD.");
		}
		return i;
	}
	
	public static void EliminarDatos(String dni) {
		conectar();
		try {
			pstmt = cn.prepareStatement("DELETE FROM Sanidad WHERE DNI = ?");
			pstmt.setString(1, dni);
			pstmt.executeUpdate();
			
		} catch(SQLException el) {
			System.out.println("Error al eliminar datos de la BD.");
		} finally {
			cerrarConexionDML("Error al cerrar la conexion y eliminación en la BD.");
		}
		
	}

}
