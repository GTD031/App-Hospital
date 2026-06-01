package Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import Controlador.AppController;

public class Vista_sanidad extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTable table;
	public DefaultTableModel dtm;
	public TableRowSorter<TableModel> tablasort; //no deberia hacer falta si esta en PacienteTabla.
	public JButton //btnActualizar
	btnNombre, btnLimpiar, btnDNI, btnEliminar, btnInsertar;
	public JTextField textField_DNI, textField_Nombre, textField_NExpediente, textField_Edad, textField_Especialidad, textField_1,textField_2,textField_3;
	public JComboBox<String> comboBox;
	public JLabel lbl_InsDNI, lbl_Nombre, lblNExpediente, lblEdad, lblExpecialidad, lblIngresado;
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

	public void cambiarBorde(JComponent objeto, int R, int G, int B) {
		objeto.setBorder(new LineBorder(new Color(R,G,B),3,true));
	}
	
	public boolean Validar(boolean bool, JComponent comp) {
		if (!bool) {
			this.cambiarBorde(comp, 255, 0, 0);
		} else {
			comp.setBorder(null);
		}
		return bool;
	}
	
	public boolean validarInsercion() {
		Pattern[] patrones = {AppController.PLetras, AppController.PNumero};
		JTextField[] campos = {this.textField_Nombre, this.textField_NExpediente,this.textField_Edad, this.textField_Especialidad};
		JLabel[] etiquetas = {this.lbl_Nombre, this.lblNExpediente, this.lblEdad,this.lblExpecialidad,this.lblIngresado};
		boolean bool2, bool = AppController.Vacio_o_cumplePatron(AppController.PDNI, this.textField_DNI, "Debe rellenar el DNI o NIE del paciente.", "Formato inválido para DNI o NIE español.");
		int i = 0;
		
		bool2 = this.Validar(bool, this.textField_DNI);
		while (i<4) {
			bool = AppController.Vacio_o_cumplePatron(patrones[(int)(i*(3-i)*0.5)], campos[i], "Debe rellenar el campo "+etiquetas[i].getText(), "Formato inválido para "+etiquetas[i].getText());
			bool2 = this.Validar(bool,campos[i]) && bool2;
			i++;
		}
		bool = comboBox.getSelectedIndex()!=0;
		if (!bool) {
			JOptionPane.showMessageDialog(null, "Debe seleccionar si el paciente esta ingresado o no.", "Selecciona una opcion", JOptionPane.ERROR_MESSAGE);
		}
		bool2 = this.Validar(bool, this.comboBox) && bool2;
		return (bool2);
	}
	
	/**
	 * Create the frame.
	 */
	public Vista_sanidad() {
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

		table = new JTable(this.dtm);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.setShowHorizontalLines(false);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);
		
//		btnActualizar = new JButton("ACTUALIZAR");
//		btnActualizar.setFont(new Font("Tahoma", Font.BOLD, 13));
//		btnActualizar.setBounds(666, 111, 190, 43);
//		contentPane.add(btnActualizar);
		
		textField_DNI = new JTextField();
		textField_DNI.setBounds(31, 358, 99, 30);
		contentPane.add(textField_DNI);
		textField_DNI.setColumns(10);
		
		lbl_InsDNI = new JLabel("DNI");
		lbl_InsDNI.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_InsDNI.setFont(new Font("Tahoma", Font.BOLD, 12));
		lbl_InsDNI.setBounds(42, 326, 67, 23);
		contentPane.add(lbl_InsDNI);
		
		textField_Nombre = new JTextField();
		textField_Nombre.setBounds(147, 358, 99, 30);
		contentPane.add(textField_Nombre);
		textField_Nombre.setColumns(10);
		
		lbl_Nombre = new JLabel("Nombre");
		lbl_Nombre.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_Nombre.setFont(new Font("Tahoma", Font.BOLD, 12));
		lbl_Nombre.setBounds(147, 331, 90, 23);
		contentPane.add(lbl_Nombre);
		
		textField_NExpediente = new JTextField();
		textField_NExpediente.setColumns(10);
		textField_NExpediente.setBounds(272, 358, 113, 30);
		contentPane.add(textField_NExpediente);
		
		lblNExpediente = new JLabel("Nº de expediente");
		lblNExpediente.setHorizontalAlignment(SwingConstants.CENTER);
		lblNExpediente.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNExpediente.setBounds(262, 331, 123, 23);
		contentPane.add(lblNExpediente);
		
		textField_Edad = new JTextField();
		textField_Edad.setColumns(10);
		textField_Edad.setBounds(408, 358, 99, 30);
		contentPane.add(textField_Edad);
		
		lblEdad = new JLabel("Edad");
		lblEdad.setHorizontalAlignment(SwingConstants.CENTER);
		lblEdad.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblEdad.setBounds(408, 331, 90, 23);
		contentPane.add(lblEdad);
		
		textField_Especialidad = new JTextField();
		textField_Especialidad.setColumns(10);
		textField_Especialidad.setBounds(517, 358, 99, 30);
		contentPane.add(textField_Especialidad);
		
		lblExpecialidad = new JLabel("Especialidad");
		lblExpecialidad.setHorizontalAlignment(SwingConstants.CENTER);
		lblExpecialidad.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblExpecialidad.setBounds(517, 331, 90, 23);
		contentPane.add(lblExpecialidad);
		
		lblIngresado = new JLabel("Ingresado");
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
		
		btnNombre = new JButton("Buscar por Nombre");
		btnNombre.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNombre.setBounds(164, 267, 144, 23);
		contentPane.add(btnNombre);
		
		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnLimpiar.setBounds(329, 219, 89, 42);
		contentPane.add(btnLimpiar);
		
		btnDNI = new JButton("Buscar por DNI");
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
		
		btnEliminar = new JButton("ELIMINAR");
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
		
		btnInsertar = new JButton("INSERTAR");
		btnInsertar.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnInsertar.setBounds(736, 358, 105, 27);
		contentPane.add(btnInsertar);
		
		JLabel cbIngresado = new JLabel("");
		cbIngresado.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "<html><b>Inserci\u00F3n</b></html>", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		cbIngresado.setBounds(10, 301, 846, 109);
		contentPane.add(cbIngresado);
		
		lblImagen = new JLabel("");
		lblImagen.setIcon(
			    new ImageIcon(Vista_sanidad.class.getResource("/hospital.png"))
			);
		lblImagen.setBounds(654, 25, 202, 75);
		contentPane.add(lblImagen);

	}
}