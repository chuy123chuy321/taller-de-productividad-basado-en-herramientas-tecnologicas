import java.io.*;
import java.util.*;

/**
 *
 * @author chuy_
 */
public class main {
    
    public static Scanner tecleado = new Scanner(System.in);
    public static PrintStream out = System.out;
    
    
    public static void pause_screen(String mensaje) {
        out.print(mensaje + " presione una tecla para continuar  ");
        tecleado.nextLine();
        out.println();
        
    }

    public static String read_str(String mensaje1){
        out.print(mensaje1 + " : ");
        return tecleado.nextLine();
    }
    
    public static int read_integer(String mensaje1){
        try {
            return Integer.parseInt(read_str(mensaje1));
        } catch (NumberFormatException e) {
            out.print( "El numero es incorrecto");
            return read_integer(mensaje1);
        }
    }

    public static float read_decimal(String mensaje1){
        try {
            return Float.parseFloat(read_str(mensaje1));
        } catch (NumberFormatException e) {
            out.print( "numero incorrecto ingrese de nuevo ");
            return read_decimal(mensaje1);
        }
    }
    
    public static String path = "estudiantes .txt";
    
    
    
    public static void main(String[] args) {
      
        ForEachFunction<Estudiante> print = new ForEachFunction<Estudiante>() {
            @Override
            public void call(Estudiante estudiante, Object params) {
                out.println(estudiante);
                int[] counter = (int[]) params;
                counter[0]++;
            }
        };
        ForEachFunction<Estudiante> printOnFile = new ForEachFunction<Estudiante>() {
            @Override
            public void call(Estudiante estudiante, Object params) {
                PrintStream file_stream = (PrintStream) params;
                file_stream.print(estudiante.getmatricula() + "\t");
                file_stream.print(estudiante.getNombre() + "\t");
                file_stream.print(estudiante.getcalificacion1() + "\t");
                file_stream.print(estudiante.getcalificacion2() + "\t");
                file_stream.print(estudiante.getcalificacion3() + "\n");
            }
        };
        if(!System.getProperties().get("os.name").equals("Linux") && System.console()!=null)
            try {
                out = new PrintStream(System.out, true, "CP850");
                tecleado = new Scanner(System.in, "CP850");
            } catch (UnsupportedEncodingException e) {}
        Vector<Estudiante> vector = new Vector<Estudiante>();
        int n;
        Estudiante datum = null, estudiante;
        int[] counter = {0};
        int i, main_option, suboption;
        String[] fields;
        try {
            Scanner file_stream = new Scanner(new FileReader(path));
            while (file_stream.hasNextLine()) {
                fields = file_stream.nextLine().split("\t");
                estudiante = new Estudiante();
                estudiante.setmatricula(fields[0]);
                estudiante.setNombre(fields[1]);
                estudiante.setcalificacion1(Float.parseFloat(fields[2]));
                estudiante.setcalificacion2(Float.parseFloat(fields[3]));
                estudiante.setcalificacion3(Float.parseFloat(fields[4]));
                vector.add(estudiante);
            }
            file_stream.close();
        } catch (FileNotFoundException e) {}
        estudiante = new Estudiante();
        do {
            out.println("MENU DE ACCIONES");
            out.println("1.- Altas");
            out.println("2.- Consultas");
            out.println("3.- Actualizaciones");
            out.println("4.- Bajas");
            out.println("5.- Ordenar registros");
            out.println("6.- Listar registros");
            out.println("7.- Salir");
            do {
                main_option = read_integer ("Seleccione una opcion que desea");
                if(main_option<1 || main_option>7)
                    out.println("opcion incorrecta");
            } while (main_option<1 || main_option>7);
            out.println();
            if (vector.isEmpty() && main_option!=1 && main_option!=7) {
                pause_screen("No hay registros");
                continue;
            }
            if (main_option<5) {
                estudiante.setmatricula(read_str ("Ingrese la matricula  del estudiante"));
                i = vector.indexOf(estudiante);
                datum = i<0 ? null : vector.get(i);
                if (datum!=null) {
                    out.println();
                    print.call(datum, counter);
                }
            }
            if (main_option==1 && datum!=null)
                out.println("El registro ya existe.");
            else if (main_option>=2 && main_option<=4 && datum==null)
                out.println("registro no encontrado o no existe");
            else switch (main_option) {
            case 1:
                estudiante.setNombre(read_str ("Ingrese el nombre"));
                estudiante.setcalificacion1(read_decimal ("Ingrese la calificacion 1"));
                estudiante.setcalificacion2(read_decimal ("Ingrese la calificacion 2"));
                estudiante.setcalificacion3(read_decimal ("Ingrese la calificacion 3"));
                vector.add(estudiante);
                estudiante = new Estudiante();
                out.println("registro nuevo correctamente");
                break;
            case 3:
                out.println("menu actualizado");
                out.println("1.- nombre");
                out.println("2.- calificacion 1");
                out.println("3.- calificacion 2");
                out.println("4.- calificacion 3");
                do {
                    suboption = read_integer ("seleccione el campo que modificara");
                    if (suboption<1 || suboption>4)
                        out.println("opcion incorrecta");
                } while (suboption<1 || suboption>4);
                switch (suboption) {
                    case 1:
                        datum.setNombre(read_str ("Ingrese el nuevo nombre"));
                        break;
                    case 2:
                        datum.setcalificacion1(read_decimal ("Ingrese la calificacion"));
                        break;
                    case 3:
                        datum.setcalificacion2(read_decimal ("Ingrese la calificacion"));
                        break;
                    case 4:
                        datum.setcalificacion3(read_decimal ("Ingrese la calificacion"));
                        break;
                }
                out.println("Registro modificado");
                break;
            case 4:
                vector.remove(datum);
                out.println("Registro eliminado ");
                break;
            case 5:
                Collections.sort(vector);
                out.println("Registros ordenados ");
                break;
            case 6:
                n = vector.size();
                counter[0] = 0;
                for (i=0; i<n; i++)
                    print.call(vector.get(i), counter);
                out.println("Total de registros: " + counter[0] + ".");
                break;
            }
            if (main_option<7 && main_option>=1)
                pause_screen("");
        } while (main_option!=7);
        try {
            PrintStream output_stream = new PrintStream(path);
            n = vector.size();
            for (i=0; i<n; i++)
                printOnFile.call(vector.get(i), output_stream);
            output_stream.close();
        } catch (FileNotFoundException e) {}
    }
}

interface ForEachFunction<T extends Comparable<T>> {
    void call(T datum, Object params);
}

class Estudiante implements Comparable<Estudiante> {

    private String matricula;
    private String nombre;
    private float calificacion1;
    private float calificacion2;
    private float calificacion3;

    @Override
    public boolean equals(Object estudiante) {
        return this==estudiante || (estudiante instanceof Estudiante && matricula.equals(((Estudiante)estudiante).matricula));
    }

    @Override
    public int compareTo(Estudiante estudiante) {
        return matricula.compareTo(estudiante.matricula);
    }

    @Override
    public String toString() {
        return
            "matricula: " + matricula + "\n" +
            "nombre: " + nombre + "\n" +
            "calificacion: " + calificacion1 + "\n" +
            "calificacion: " + calificacion2 + "\n" +
            "calificacion : " + calificacion3 + "\n";
    }

    public String getmatricula() {
        return matricula;
    }

    public void setmatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getcalificacion1() {
        return calificacion1;
    }

    public void setcalificacion1(float calificacion1) {
        this.calificacion1 = calificacion1;
    }

    public float getcalificacion2() {
        return calificacion2;
    }

    public void setcalificacion2(float calificacion2) {
        this.calificacion2 = calificacion2;
    }

    public float getcalificacion3() {
        return calificacion3;
    }

    public void setcalificacion3(float calificacion3) {
        this.calificacion3 = calificacion3;
    }
}  
        
        
        
    

        
        
        
  
    
    
    
    
    

