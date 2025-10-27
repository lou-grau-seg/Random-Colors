import java.io.*; // BufferedReader - BufferedWriter - FileReader - FileWriter - IOException

public class Archivo {

    /*En esta clase esta todo lo relacionado con el manejo de usuarios y sus puntuaciones previas*/

    private boolean pertenece=false; //marca si un usuario ya habia jugado previamente o no

    /*Como Java no me permite modificar solamente una linea de un archivo, tengo que hacer uso de
    dos archivos para escribir los usuarios y otro archivo que tenga la ruta hasta al archivo que
    se esta utilizando actualmente*/
    private static final String ARCHIVO_ORIGINAL="/archivoOriginal.txt";
    private static final String ARCHIVO_AUXILIAR ="/archivoAuxiliar.txt";

    /*el archivo getRutas debe tener dentro alguna de las dos rutas de los dos archivos anteriores para que
     el programa pueda funcionar, si cambian las rutas de los archivos de arriba asegurense de tambien cambiar
     el contenido del archivo Rutas si no, NO VA A FUNCIONAR*/
    private static final String ARCHIVO_RUTAS="/Rutas.txt";
    private String archivoActual=getRutas();


    public boolean isPertenece() { //devuelve si el usuario ya estaba en la lista de jugadores
        return pertenece;
    }

    private String getRutas(){ //Devuelve el archivo del que se esta haciendo uso al momento
        String archivo="";
        BufferedReader entrada=null;
        try{
            entrada=new BufferedReader(new FileReader(ARCHIVO_RUTAS));
            archivo=entrada.readLine();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try {
                if (entrada != null) {
                    entrada.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el archivo: " + e.getMessage());
            }
        }
        return archivo;
    }


    private void setRutas() { //Este metodo se usa para cambiar el archivo que se esta usando actualmente
        BufferedWriter salida = null;
        try {
            salida = new BufferedWriter(new FileWriter(ARCHIVO_RUTAS));
            if (archivoActual.equals(ARCHIVO_ORIGINAL)){
                salida.write(ARCHIVO_AUXILIAR);
            }
            else{
                salida.write(ARCHIVO_ORIGINAL);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (salida != null) {
                    salida.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el archivo: " + e.getMessage());
            }
        }
    }


    /*Este metodo busca al usuario y, si lo encuentra, devuelve su puntuacion anterior. Si no lo encuentra
    devuelve un 0*/
    public int buscarPuntosUsuario(String nombreUsuario) {
        BufferedReader entrada = null;
        String linea;
        int puntuacion = 0;
        try {
            entrada = new BufferedReader(new FileReader(this.archivoActual));

            while ((linea = entrada.readLine()) != null && !this.pertenece) { // Leer cada línea del archivo
                if (linea.equals(nombreUsuario)) { //encuentra el usuario
                    this.pertenece = true; //cambia la variable de clase para marcar que el usuario si habia jugado antes
                    linea = entrada.readLine(); //salta una linea para llegar a la que tiene el puntaje
                    puntuacion = Integer.parseInt(linea); //obtiene su puntuacion
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } finally {
            try {
                if (entrada != null) {
                    entrada.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el archivo: " + e.getMessage());
            }
        }
        return puntuacion;
    }


    /*Este metodo se usa si el usuario no habia jugado anteriormente, por lo que solo se
    necesita escribir al final del archivo actual y no presenta mas complicaciones*/
    public void escribirNuevoContenido(String contenido) {
        BufferedWriter escritura = null;
        try {
            escritura = new BufferedWriter(new FileWriter(this.archivoActual,true));
            escritura.write(contenido);
            escritura.write("\n");

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        } finally {
            try {
                if (escritura != null) {
                    escritura.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el archivo: " + e.getMessage());
            }
        }
    }


    /*Este metodo se usa si el usuario ya existia, por lo que es necesario el
    actualizar la puntuacion que ya tenia previamente. Lo que se hace es leer
    el archivo actual e ir escribiendo cada linea en el archivo auxiliar hasta que encontramos
    la linea que queremos actualizar. En ese caso se escribe la linea actualizada en el archivo
    auxiliar, y se termina de copiar el archivo original al auxiliar habiendo cambiado solo esa linea.
    Al final establecemos que el archivo auxiliar (que es el mas actualizado) es el nuevo
    archivo actual*/
    public void actualizarPuntajes(String nombreUsuario,String puntaje) {
        BufferedReader original = null;
        BufferedWriter auxiliar = null;

        try {
            original = new BufferedReader(new FileReader(this.archivoActual)); //archivo a actualizar
            if (this.archivoActual.equals(ARCHIVO_ORIGINAL)){ //se establece el archivo auxiliar
                auxiliar = new BufferedWriter(new FileWriter(ARCHIVO_AUXILIAR));
            } else{
                auxiliar = new BufferedWriter(new FileWriter(ARCHIVO_ORIGINAL));
            }
            String linea;

            while ((linea = original.readLine()) != null) { //se escribe la actualizacion en el archivo auxiliar
                if(linea.equals(nombreUsuario)) {
                    auxiliar.write(linea);
                    auxiliar.newLine();
                    linea=original.readLine();
                    auxiliar.write(puntaje);
                    }else{
                        auxiliar.write(linea);
                    }
                    auxiliar.newLine(); // Aseguramos un salto de línea
                }

            setRutas(); //se establece el archvivo auxiliar como el nuevo archivo actual

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        finally {
            try {
                if (original != null) {
                    original.close();
                }
                if (auxiliar != null) {
                    auxiliar.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el archivo: " + e.getMessage());
            }
        }
    }
}


