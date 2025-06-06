package servicios;

import java.io.BufferedReader;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import entidades.Documento;

public class ServicioDocumento {
    
    private static List<Documento> documentos = new ArrayList();
    private static String[] encabezados;


    public static int getTamaño(){
        return documentos.size();
    }

    

    public static String[] getEncabezados() {
        return encabezados;
    }

    public static void setEncabezados(String[] encabezados) {
        ServicioDocumento.encabezados = encabezados;
    }



    public static void desdeArchivo(String nombreArchivo) {
        documentos.clear();
        BufferedReader br = Archivo.abrirArchivo(nombreArchivo);
        if (br != null) {
            try {
                String linea = br.readLine();
                encabezados = linea.split(";");
                linea = br.readLine();
                while (linea != null) {
                    String[] textos = linea.split(";");
                    if (textos.length >= encabezados.length) {
                        Documento documento = new Documento(textos[0],
                                textos[1],
                                textos[2],
                                textos[3]);
                        documentos.add(documento);
                    }
                    linea = br.readLine();
                }

            } catch (Exception ex) {

            }
        }
    }

    public static void mostrar(JTable tbl) {
        String[][] datos = new String[documentos.size()][encabezados.length];
        int fila = 0;
        for (Documento d : documentos) {
            datos[fila][0] = d.getApellido1();
            datos[fila][1] = d.getApellido2();
            datos[fila][2] = d.getNombre();
            datos[fila][3] = d.getDocumento();
            fila++;
        }
        DefaultTableModel dtm = new DefaultTableModel(datos, encabezados);
        tbl.setModel(dtm);
    }

    /* 
    public static boolean esMayor(Documento d1, Documento d2, int criterio) {
        if (criterio == 0) {
            // ordenar primero por Nombre Completo y luego por Tipo de Documento
            return (d1.getNombreCompleto().compareTo(d2.getNombreCompleto()) > 0) ||
                    (d1.getNombreCompleto().equals(d2.getNombreCompleto())
                            && d1.getDocumento().compareTo(d2.getDocumento()) > 0);
        } else {
            // ordenar primero por Tipo de Documento y luego por Nombre Completo
            return (d1.getDocumento().compareTo(d2.getDocumento()) > 0) ||
                    (d1.getDocumento().equals(d2.getDocumento())
                            && d1.getNombreCompleto().compareTo(d2.getNombreCompleto()) > 0);
        }
    }
    */


    private static final Collator collator = Collator.getInstance(new Locale("es", "ES"));

    public static boolean esMayor(Documento d1, Documento d2, int criterio) {
        // fuerza de comparación: ignora mayúsculas y tildes si se desea
        collator.setStrength(Collator.PRIMARY);

        int cmpNombre = collator.compare(d1.getNombreCompleto(), d2.getNombreCompleto());
        int cmpDocumento = collator.compare(d1.getDocumento(), d2.getDocumento());

        if (criterio == 0) {
            return (cmpNombre > 0) || (cmpNombre == 0 && cmpDocumento > 0);
        } else {
            return (cmpDocumento > 0) || (cmpDocumento == 0 && cmpNombre > 0);
        }
    }

    private static void intercambiar(int origen, int destino) {
        Documento temporal = documentos.get(origen);
        documentos.set(origen, documentos.get(destino));
        documentos.set(destino, temporal);
    }

    public static void ordenarBurbuja(int criterio) {
        for (int i = 0; i < documentos.size() - 1; i++) {
            for (int j = i + 1; j < documentos.size(); j++) {
                if (esMayor(documentos.get(i), documentos.get(j), criterio)) {
                    intercambiar(i, j);
                }
            }
        }
    }

    private static int localizarPivote(int inicio, int fin, int criterio) {
        int pivote = inicio;
        Documento dPivote = documentos.get(pivote);

        for (int i = inicio + 1; i <= fin; i++) {
            if (esMayor(dPivote, documentos.get(i), criterio)) {
                pivote++;
                // if (i != pivote) {
                intercambiar(i, pivote);
                // }
            }
        }
        if (inicio != pivote) {
            intercambiar(inicio, pivote);
        }
        return pivote;
    }

    public static void ordenarRapido(int inicio, int fin, int criterio) {
        // punto de finalizacion
        if (inicio >= fin) {
            return;
        }

        // casos recursivos
        int pivote = localizarPivote(inicio, fin, criterio);
        ordenarRapido(inicio, pivote - 1, criterio); // ordenar los menores a la posicion PIVOTE
        ordenarRapido(pivote + 1, fin, criterio); // ordenar los mayores a la posicion PIVOTE
    }

    public static Arbol getArbol(){
        Arbol arbol=new Arbol();
        for(var documento:documentos){
            arbol.agregar(new Nodo(documento));
        }
        return arbol;
    }



    public static void setDocumentos(List<Documento> documentos) {
        ServicioDocumento.documentos = documentos;
    }

}
