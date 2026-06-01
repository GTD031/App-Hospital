package Controlador;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import Modelo.DatosDTM;
import Modelo.Paciente;
import Vista.Vista_sanidad;

public class AppController {

	private Vista_sanidad vista;	
	//private DatosDTM datos;
	public static Pattern PLetras = Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚñÑ]+"), PNumero = Pattern.compile("[0-9]+"), PDNI = Pattern.compile("([XYZ]|[0-9])[0-9]{7}[XYZ]");	//ultimo no usado por ahora
	public static boolean bool;
	
	public AppController() {
		
		vista = new Vista_sanidad();	//Instancio la clase
		vista.dtm = DatosDTM.generarDTM();
		vista.table.setModel(vista.dtm); //seteo el modelo (revisar el rowsorter)
		vista.tablasort = new TableRowSorter<TableModel>(vista.dtm);
		vista.table.setRowSorter(vista.tablasort);

		//Añadir los eventos
//		vista.btnActualizar.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				vista.table.setModel(DatosDTM.generarDTM());	//revisar
//			}
//		});

		vista.btnNombre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = AppController.this.vista.textField_2.getText();
				boolean bool = Vacio_o_cumplePatron(PLetras, AppController.this.vista.textField_2, "Debe rellenar el nombre a buscar", "El campo solo debe estar compuesto de carácteres alfabéticos.");
				int n;
				
				if (bool) {
					DatosDTM.buscarPorNombre(str, AppController.this.vista.tablasort);
					n = AppController.this.vista.tablasort.getViewRowCount();
					if (n == 0)
						JOptionPane.showMessageDialog(AppController.this.vista, "No se han encontrado registros", "Aviso", JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(AppController.this.vista, "Encontrado"+(n==1?"":"s")+" "+n+" paciente"+(n==1?"":"s"), "Aviso", JOptionPane.INFORMATION_MESSAGE);
					
				}
			}
		});
		
		vista.btnDNI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean bool = Vacio_o_cumplePatron(PDNI, AppController.this.vista.textField_1, "Debe rellenar el DNI a buscar", "El formato es invalido, no corresponde a un DNI o NIE español.");
				int n;
				
				if (bool) {
					String dni = AppController.this.vista.textField_1.getText(); //convierto a entero el contenido del campo de dni.
					DatosDTM.buscarPorDni(dni, AppController.this.vista.tablasort);
					n = AppController.this.vista.tablasort.getViewRowCount();
					if (n == 0)
						JOptionPane.showMessageDialog(AppController.this.vista, "No se han encontrado registros", "Aviso", JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(AppController.this.vista, "Encontrado"+(n==1?"":"s")+" "+n+" paciente"+(n==1?"":"s"), "Aviso", JOptionPane.INFORMATION_MESSAGE);
					// OJO:si la tabla tendrá registrado un solo DNI, entonces no sería necesario considerar el caso que arroje > 1 registro. Pero si no, esto debe revisarse.
				}
		}});
		
		vista.btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean bool = Vacio_o_cumplePatron(PDNI, AppController.this.vista.textField_3, "Debe rellenar el campo", "Formato inválido para un DNI.");
				if (bool) {
					String Edni = AppController.this.vista.textField_3.getText();
					DatosDTM.eliminarPaciente(Edni, AppController.this.vista.dtm);  //lo elimino de la tabla de la vista
				}
			}
		});
		
		vista.btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					bool = AppController.this.vista.validarInsercion();	//REVISAR ESTO, no se nombran los errores en el orden correcto (se salto nombre y lo retomo después).
					if (bool) {
						String DNI = AppController.this.vista.textField_DNI.getText(), Nombre = AppController.this.vista.textField_Nombre.getText();
						int NExpediente = Integer.parseInt(AppController.this.vista.textField_NExpediente.getText()), edad = Integer.parseInt(AppController.this.vista.textField_Edad.getText());
						String especialidad = AppController.this.vista.textField_Especialidad.getText(), ingresado = AppController.this.vista.comboBox.getSelectedItem().toString();
						
						Paciente p = new Paciente(DNI,Nombre,NExpediente,edad,especialidad,ingresado);
					
						//despues de declarar el objeto de tipo Paciente, hago la insercion
						DatosDTM.insertarPaciente(p, AppController.this.vista.dtm);
					}
				}
			});
		
		vista.btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AppController.this.vista.textField_2.setText("");
				AppController.this.vista.textField_1.setText("");
				AppController.this.vista.tablasort.setRowFilter(null);
			}
		});

	}

 	public static boolean Vacio_o_cumplePatron(Pattern Ptron, JTextField campo, String strVacio, String strPatron) {

		String str = campo.getText();
 		Matcher Mtron = Ptron.matcher(str);	
 		
		if (str.equals("")) 
			JOptionPane.showMessageDialog(null, strVacio, "Campo Vacío", JOptionPane.ERROR_MESSAGE);
		else if (!Mtron.matches())
			JOptionPane.showMessageDialog(null, strPatron, "Carácteres ó formato inválidos", JOptionPane.ERROR_MESSAGE);
		return Mtron.matches() && !str.equals("");
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
					AppController App = new AppController();
					App.vista.setVisible(true);
					} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
