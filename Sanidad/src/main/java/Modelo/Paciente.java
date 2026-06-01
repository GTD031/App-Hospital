package Modelo;

public class Paciente {
	
	protected int edad, expediente;
	protected String dni, nombre, especialidad;
	protected boolean ingresado;
	
	public Paciente(String dni, String nombre, int expediente, int edad, String especialidad, String ingresado){
		this.dni = dni;
		this.nombre = nombre;
		this.expediente = expediente;
		this.edad = edad;
		this.especialidad = especialidad;
		this.ingresado = ingresado.equals("Si");
	}

    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public int getExpediente() { return expediente; }
    public int getEdad() { return edad; }
    public String getEspecialidad() { return especialidad; }
    public boolean isIngresado() { return ingresado; }
}
