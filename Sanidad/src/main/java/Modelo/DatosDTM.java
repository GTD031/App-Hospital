package Modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DatosDTM {
	
	public static int N = getNumeroRegistros();
	
	public DatosDTM() {}

	public static DefaultTableModel generarDTM() { // La creación del DTM en un método
		DefaultTableModel dtm = new DefaultTableModel(getDatos(), getNombresColumnas()) {
			private static final long serialVersionUID = 1L;

			@Override
			// Método para indicar que la estructura ha cambiado
			public void fireTableStructureChanged() {
				super.fireTableStructureChanged();
			}

			@Override
			// Método para hacer las celdas no editables con FALSE
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override
			// Indicamos que ha cambiado para que se actualize
			public void fireTableDataChanged() {
				super.fireTableDataChanged();
			}
		};
		return dtm;
	}

	public static int getNumeroColumnas() {
		int conta = 0;
		String query = "SELECT Id, DNI, Nombre, [Nº de expediente], Edad, Especialidad, Ingresado FROM Sanidad";
		try (Connection cn = ConexionBD.getConexion();
				Statement stmt = cn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			ResultSetMetaData rsmd = rs.getMetaData();
			conta = rsmd.getColumnCount();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al extraer el número de columnas");
		}
		return conta;
	}

	public static int getNumeroRegistros() {
		int total = 0;
		String query = "select count(*) from Sanidad";
		try (Connection cn = ConexionBD.getConexion();
				Statement stmt = cn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				total = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al extraer el número de registros");
		}
		return total;
	}

	public static Object[] getNombresColumnas() {
		Object[] col = new Object[getNumeroColumnas()];
		// int contador = 1;
		int conta = 0;
		String query = "select Id, DNI, Nombre, [Nº de expediente], Edad, Especialidad, Ingresado from Sanidad";
		try (Connection cn = ConexionBD.getConexion();
				Statement stmt = cn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			ResultSetMetaData rsmd = rs.getMetaData();
			int N = getNumeroColumnas();
			for (int i = 1; i<=N; i++) {
				rs.next();
				col[conta] = rsmd.getColumnName(i);
				conta++;
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al extraer los nombres de las columnas");
		}
		return col;
	}

	public static Object[][] getDatos() {
		Object[][] datos = new Object[N][getNumeroColumnas()];
		String query = "select Id, DNI, Nombre, [Nº de expediente], Edad, Especialidad, Ingresado from Sanidad";
		try (Connection cn = ConexionBD.getConexion();
				Statement stmt = cn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			int contador = 0;
			while (rs.next()) { // Extraemos los datos de la consulta
				datos[contador][0] = rs.getInt(1);
				datos[contador][1] = rs.getString(2);
				datos[contador][2] = rs.getString(3);
				datos[contador][3] = rs.getInt(4);
				datos[contador][4] = rs.getInt(5);
				datos[contador][5] = rs.getString(6);
				datos[contador][6] = rs.getBoolean(7)?"Sí":"No";
				//dtm.addRow(datos[contador]);
				contador++;
				
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al ejecutar la Select");
			e.printStackTrace();
		}
		return datos;
	}
	
	public static void insertarPaciente(Paciente p, DefaultTableModel dtm) {
		
		int Id = ConexionBD.InsertarDatos(p);
		dtm.addRow(new Object[] {
				Id,
				p.dni,
				p.nombre,
				p.expediente,
				p.edad,
				p.especialidad,
				p.ingresado?"Si":"No"
		});
	}

	public static void eliminarPaciente(String dni, DefaultTableModel dtm) {
		ConexionBD.EliminarDatos(dni);
		int i = 0;
		while (i<N) {
			if (dtm.getValueAt(i,1).equals(dni)) {
				dtm.removeRow(i);
				JOptionPane.showMessageDialog(null, "Se ha eliminado 1 registro", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
				break;
			}
			i++;
		}
//		if (i==N) {
//			JOptionPane.showMessageDialog(vista, "No se ha encontrado ningún registro con este DNI", "No encontrado", JOptionPane.INFORMATION_MESSAGE);
//		}
	}
	
	public static void buscarPorNombre(String name, TableRowSorter<TableModel> tablasort) {
		tablasort.setRowFilter(RowFilter.regexFilter("(?i)^"+name, 2));	//busca name sin importar mayusc o minusc
	}
	
	public static void buscarPorDni(String dni, TableRowSorter<TableModel> tablasort) {
		tablasort.setRowFilter(RowFilter.regexFilter(dni,1));
	}
}
