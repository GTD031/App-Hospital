package Ejercicios_Sanidad;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class Ejercicios_Sanidad extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static JTable table;
	private static DefaultTableModel dtm;
	private static TableRowSorter<TableModel> tablasort;
	private static JTextField textField_DNI;
	private static JTextField textField_Nombre;
	private static JTextField textField_NExpediente;
	private static JTextField textField_Edad;
	private static JTextField textField_Especialidad;
	private static JTextField textField_1;
	private static JTextField textField_2;
	private static JTextField textField_3;
	private static Connection cn;
	private static PreparedStatement pstmt;
	private static ResultSet rs;
	private static JComboBox<String> comboBox;
	private static Pattern PLetras = Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚñÑ]+"), PNumero = Pattern.compile("[0-9]+");
	private static JLabel lblImagen;
	private int posy = 75;
	private static int T = 3;
	private Timer timer = new Timer(T, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			lblImagen.setVisible(true);
			lblImagen.setSize(202, posy);
			posy -= 5;
			if (posy == 0) {
				timer.stop();
				lblImagen.setSize(202, 75);
				lblImagen.setVisible(true);
	           }
	       }
	   });

	private static void cerrarConexionDML(String str){
		try {
			cn.close();
			pstmt.close();
		} catch(SQLException clo) {
			System.out.println(str);
		}
	}

	public static void conectar() {
		try {
			cn = DriverManager.getConnection("jdbc:ucanaccess://src/main/resources/Sanidad.accdb");
		} catch (SQLException e) {
			System.out.println("Error al abrir la base de datos");
		}
	}
	
	private static boolean Vacio_o_cumplePatron(Pattern Ptron, JTextField campo, String strVacio, String strPatron) {
		Matcher Mtron = Ptron.matcher(campo.getText());
		if (campo.getText().equals("")) 
			JOptionPane.showMessageDialog(null, strVacio, "Campo Vacío", JOptionPane.ERROR_MESSAGE);
		else if (!Mtron.matches())
			JOptionPane.showMessageDialog(null, strPatron, "Carácteres inválidos", JOptionPane.ERROR_MESSAGE);
		return Mtron.matches();
	}
	
	private static void InsertarDatos() {
		try {
			conectar();
			pstmt=cn.prepareStatement("INSERT INTO Sanidad (DNI, Nombre, [Nº de expediente], Edad, Especialidad, Ingresado) Values (?,?,?,?,?,?)");
			pstmt.setInt(1, Integer.parseInt(textField_DNI.getText()));
			pstmt.setString(2, textField_Nombre.getText());
			pstmt.setInt(3, Integer.parseInt(textField_NExpediente.getText()));		//Numero de expediente
			pstmt.setInt(4, Integer.parseInt(textField_Edad.getText()));
			pstmt.setString(5, textField_Especialidad.getText());
			pstmt.setBoolean(6, comboBox.getSelectedItem().equals("Sí"));
			pstmt.executeUpdate();
			dtm.addRow(new Object[] {
					Integer.parseInt(textField_DNI.getText()),
					textField_Nombre.getText(),
					Integer.parseInt(textField_NExpediente.getText()),
					Integer.parseInt(textField_Edad.getText()),
					textField_Especialidad.getText(),
					comboBox.getSelectedItem().toString()
			});
		} catch (SQLException in) {
			JOptionPane.showMessageDialog(null, "Error en la conexión ó ejecución con la base de datos.");
		} finally {
			cerrarConexionDML("Error al cerrar la conexion e insercion en la BD.");
		}
		
	}
	
	private static void EliminarDatos() {
		int i,op = JOptionPane.showConfirmDialog(null,"¿Esta seguro de que desea eliminar al usuario con esta marca en la base de datos? Esta acción no se puede revertir");
		if (op == 0) {
			conectar();
			try {
				pstmt = cn.prepareStatement("DELETE FROM Sanidad WHERE DNI = ?");
				pstmt.setInt(1, Integer.parseInt(textField_3.getText()));
				i = pstmt.executeUpdate();
				if (i == 0) {
					JOptionPane.showMessageDialog(null, "No hay ningún registro con esa marca.");
				} else if (i == 1){
					JOptionPane.showMessageDialog(null, "Se ha eliminado 1 registro.");
				} else {
					JOptionPane.showMessageDialog(null, "Se han eliminado "+i+" registros.");
				}
			} catch(SQLException el) {
				System.out.println("Error al eliminar datos de la BD.");
			} finally {
				cerrarConexionDML("Error al cerrar la conexion y eliminación en la BD.");
			}
		}
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
					Ejercicios_Sanidad frame = new Ejercicios_Sanidad();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private Connection getConexion() { // Siempre abierta sin try()
		try {
			return DriverManager.getConnection("jdbc:ucanaccess://src/main/resources/Sanidad.accdb");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Conexión incorrecta");
			return null; // Retorna null si no puede establecer la conexión
		}
	}

	public DefaultTableModel generarDTM() { // La creación del DTM en un método
		dtm = new DefaultTableModel(getDatos(), getNombresColumnas()) {
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

	public int getNumeroColumnas() {
		int conta = 0;
		String query = "SELECT * FROM Sanidad";
		try (Connection cn = this.getConexion();
				Statement stmt = cn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			ResultSetMetaData rsmd = rs.getMetaData();
			conta = rsmd.getColumnCount();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error al extraer el número de columnas");
		}
		return conta;
	}

	public int getNumeroRegistros() {
		int total = 0;
		String query = "select count(*) from Sanidad";
		try (Connection cn = this.getConexion();
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

	public Object[] getNombresColumnas() {
		Object[] col = new Object[getNumeroColumnas()];
		// int contador = 1;
		int conta = 0;
		String query = "select * from Sanidad";
		try (Connection cn = this.getConexion();
				Statement stmt = cn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			ResultSetMetaData rsmd = rs.getMetaData();
			int N = getNumeroColumnas();
			for (int i = 1; i<=N; i++) {
				rs.next();
				col[conta] = rsmd.getColumnName(i);
				
				conta++;
			}
			// while (rs.next()) { //Si usamos un while itera una vez más
			// //y necesitamos la igualación conta=getNumeroColumnas para salir
			// if (conta == getNumeroColumnas()) {
			// break;
			// }
			// col[conta] = rsmd.getColumnName(contador);
			// conta++;
			// contador++;
			// }
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al extraer los nombres de las columnas");
		}
		return col;
	}

	public Object[][] getDatos() {
		Object[][] datos = new Object[getNumeroRegistros()][getNumeroColumnas()];
		String query = "select * from Sanidad";
		try (Connection cn = this.getConexion();
				Statement stmt = cn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			int contador = 0;
			while (rs.next()) { // Extraemos los datos de la consulta
				datos[contador][0] = rs.getInt(1);
				datos[contador][1] = rs.getString(2);
				datos[contador][2] = rs.getInt(3);
				datos[contador][3] = rs.getInt(4);
				datos[contador][4] = rs.getString(5);
				datos[contador][5] = rs.getBoolean(6)?"Sí":"No";
				//dtm.addRow(datos[contador]);
				contador++;
				
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al ejecutar la Select");
			e.printStackTrace();
		}
		return datos;
	}

	/**
	 * Create the frame.
	 */
	public Ejercicios_Sanidad() {
		setTitle("Pacientes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 882, 460);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		contentPane.setLayout(null);
		timer.start();
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 24, 613, 150);
		contentPane.add(scrollPane);
		
		table = new JTable(generarDTM());
		tablasort = new TableRowSorter<TableModel>(dtm);
		table.setRowSorter(tablasort);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getColumnModel().getColumn(4).setPreferredWidth(87);
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.setShowHorizontalLines(false);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);
		
		JButton btnActualizar = new JButton("ACTUALIZAR");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
			}
		});
		btnActualizar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnActualizar.setBounds(666, 111, 190, 43);
		contentPane.add(btnActualizar);
		
		textField_DNI = new JTextField();
		textField_DNI.setBounds(31, 358, 99, 30);
		contentPane.add(textField_DNI);
		textField_DNI.setColumns(10);
		
		JLabel lbl_InsDNI = new JLabel("DNI");
		lbl_InsDNI.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_InsDNI.setFont(new Font("Tahoma", Font.BOLD, 12));
		lbl_InsDNI.setBounds(42, 326, 67, 23);
		contentPane.add(lbl_InsDNI);
		
		textField_Nombre = new JTextField();
		textField_Nombre.setBounds(147, 358, 99, 30);
		contentPane.add(textField_Nombre);
		textField_Nombre.setColumns(10);
		
		JLabel lbl_Nombre = new JLabel("Nombre");
		lbl_Nombre.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_Nombre.setFont(new Font("Tahoma", Font.BOLD, 12));
		lbl_Nombre.setBounds(147, 331, 90, 23);
		contentPane.add(lbl_Nombre);
		
		textField_NExpediente = new JTextField();
		textField_NExpediente.setColumns(10);
		textField_NExpediente.setBounds(272, 358, 113, 30);
		contentPane.add(textField_NExpediente);
		
		JLabel lblNExpediente = new JLabel("Nº de expediente");
		lblNExpediente.setHorizontalAlignment(SwingConstants.CENTER);
		lblNExpediente.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNExpediente.setBounds(262, 331, 123, 23);
		contentPane.add(lblNExpediente);
		
		textField_Edad = new JTextField();
		textField_Edad.setColumns(10);
		textField_Edad.setBounds(408, 358, 99, 30);
		contentPane.add(textField_Edad);
		
		JLabel lblEdad = new JLabel("Edad");
		lblEdad.setHorizontalAlignment(SwingConstants.CENTER);
		lblEdad.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblEdad.setBounds(408, 331, 90, 23);
		contentPane.add(lblEdad);
		
		textField_Especialidad = new JTextField();
		textField_Especialidad.setColumns(10);
		textField_Especialidad.setBounds(517, 358, 99, 30);
		contentPane.add(textField_Especialidad);
		
		JLabel lblExpecialidad = new JLabel("Especialidad");
		lblExpecialidad.setHorizontalAlignment(SwingConstants.CENTER);
		lblExpecialidad.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblExpecialidad.setBounds(517, 331, 90, 23);
		contentPane.add(lblExpecialidad);
		
		JLabel lblIngresado = new JLabel("Ingresado");
		lblIngresado.setHorizontalAlignment(SwingConstants.CENTER);
		lblIngresado.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblIngresado.setBounds(626, 331, 90, 23);
		contentPane.add(lblIngresado);
		
		comboBox = new JComboBox<String>();
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 12));
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Seleccione", "Si", "No"}));
		comboBox.setBounds(636, 362, 90, 22);
		contentPane.add(comboBox);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(191, 231, 99, 30);
		contentPane.add(textField_2);
		
		JLabel lblNewLabel_1 = new JLabel("Nombre");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1.setBounds(191, 204, 90, 23);
		contentPane.add(lblNewLabel_1);
		
		JButton btnNombre = new JButton("Buscar por Nombre");
		btnNombre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = textField_2.getText();
				boolean bool = Vacio_o_cumplePatron(PLetras, textField_2, "Debe rellenar el nombre a buscar", "El campo solo debe estar compuesto de carácteres alfabéticos.");
				if (bool) {
					tablasort.setRowFilter(RowFilter.regexFilter("(?i)^"+str, 1));
					int fila = tablasort.getViewRowCount();
					if (fila != 0) {
						JOptionPane.showMessageDialog(null, "Encontrados " + fila + " registros");
					} else {
						JOptionPane.showMessageDialog(null,"No se han encontrados registros");
					}		
				}
			}
		});
		btnNombre.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNombre.setBounds(164, 267, 144, 23);
		contentPane.add(btnNombre);
		
		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_1.setText("");
				textField_2.setText("");
				tablasort.setRowFilter(null);
			}
		});
		btnLimpiar.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnLimpiar.setBounds(329, 219, 89, 42);
		contentPane.add(btnLimpiar);
		
		JButton btnDNI = new JButton("Buscar por DNI");
		btnDNI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean bool = Vacio_o_cumplePatron(PNumero, textField_1, "Debe rellenar el DNI a buscar", "El campo debe estar compuesto solo de números.");
				
				if (bool) {
					tablasort.setRowFilter(RowFilter.numberFilter(ComparisonType.EQUAL,Integer.parseInt(textField_1.getText()), 0));
					int fila = tablasort.getViewRowCount();
					if (fila != 0) {
						JOptionPane.showMessageDialog(null, "Encontrados " + fila + " registros");
					} else {
						JOptionPane.showMessageDialog(null,"No se han encontrados registros");
					}
				}
			}});
		btnDNI.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnDNI.setBounds(24, 267, 130, 23);
		contentPane.add(btnDNI);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(31, 231, 113, 30);
		contentPane.add(textField_1);
		
		JLabel lbl_InsDNI_1 = new JLabel("DNI");
		lbl_InsDNI_1.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_InsDNI_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lbl_InsDNI_1.setBounds(52, 204, 67, 23);
		contentPane.add(lbl_InsDNI_1);
		
		JLabel lblBuscar = new JLabel("");
		lblBuscar.setBorder(new TitledBorder(null, "<html><b>B\u00FAsqueda", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		lblBuscar.setBounds(10, 185, 420, 116);
		contentPane.add(lblBuscar);
		
		JButton btnEliminar = new JButton("ELIMINAR");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean bool = Vacio_o_cumplePatron(PNumero, textField_3, "Debe rellenar el campo", "El campo debe contener únicamente carácteres numéricos.");
				if (bool) {
					EliminarDatos();
				}
			}
		});
		btnEliminar.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnEliminar.setBounds(672, 219, 130, 42);
		contentPane.add(btnEliminar);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(482, 231, 113, 30);
		contentPane.add(textField_3);
		
		JLabel lbl_InsDNI_1_1 = new JLabel("DNI");
		lbl_InsDNI_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_InsDNI_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lbl_InsDNI_1_1.setBounds(503, 204, 67, 23);
		contentPane.add(lbl_InsDNI_1_1);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBorder(new TitledBorder(null, "<html><b>Eliminaci\u00F3n", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		lblNewLabel_2.setBounds(452, 184, 404, 117);
		contentPane.add(lblNewLabel_2);
		
		JButton btnInsertar = new JButton("INSERTAR");
		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField[] camposNum = {textField_DNI, textField_NExpediente,textField_Edad};
				JTextField[] camposLet = {textField_Nombre,textField_Especialidad};
				ArrayList<JTextField> campos_NoCumplen = new ArrayList<JTextField>();
				JLabel[] etiquetas = {lbl_InsDNI, lblNExpediente, lblEdad,lbl_Nombre,lblExpecialidad,lblIngresado};
				boolean bool = true;
				int i = 0;
				while (i<3) {
					if (!Vacio_o_cumplePatron(PNumero, camposNum[i], "Debe rellenar el campo "+etiquetas[i].getText(), "El campo "+etiquetas[i].getText()+" tiene caracteres inválidos")) {
						bool = false;
						campos_NoCumplen.add(camposNum[i]);
					}
					i++;
				}
				i = 0;
				while (i<2) {
					if (!Vacio_o_cumplePatron(PLetras, camposLet[i], "Debe rellenar el campo "+etiquetas[i+3].getText(), "El campo "+etiquetas[i+3].getText()+" tiene caracteres inválidos")) {
						bool = false;
						campos_NoCumplen.add(camposLet[i]);
					}
					i++;
				}
				if (comboBox.getSelectedIndex()==0) {
					JOptionPane.showMessageDialog(null, "Debe indicar si el paciente esta ingresado o no.");
					bool = false;
					comboBox.setBorder(new LineBorder(new Color(255, 0, 0), 3, true));
				}
				int l = campos_NoCumplen.size();
				if (l>0) {
					i = 0;
					while(i<l) {
						campos_NoCumplen.get(i).setBorder(new LineBorder(new Color(255, 0, 0), 3, true));
						i++;
					}
				}else if (bool) {
					comboBox.setBorder(null);
					InsertarDatos();
					i = 0;
					while (i<3) {
						camposNum[i].setBorder(null);
						i++;
					}
					i = 0;
					while (i<2) {
						camposLet[i].setBorder(null);
						i++;
					}
				}
			}
		});
		btnInsertar.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnInsertar.setBounds(736, 358, 105, 27);
		contentPane.add(btnInsertar);
		
		JLabel cbIngresado = new JLabel("");
		cbIngresado.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "<html><b>Inserci\u00F3n</b></html>", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		cbIngresado.setBounds(10, 301, 846, 109);
		contentPane.add(cbIngresado);
		
		lblImagen = new JLabel("");
		lblImagen.setIcon(
			    new ImageIcon(Ejercicios_Sanidad.class.getResource("/hospital.png"))
			);
		lblImagen.setBounds(654, 25, 202, 75);
		contentPane.add(lblImagen);

	}
}
