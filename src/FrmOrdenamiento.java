import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.WindowConstants;

import entidades.Documento;
import servicios.Arbol;
import servicios.ServicioDocumento;
import servicios.Util;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrmOrdenamiento extends JFrame {

    private Arbol arbol; // variable
    private JButton btnOrdenarBurbuja;
    private JButton btnOrdenarRapido;
    private JButton btnOrdenarInsercion;
    private JToolBar tbOrdenamiento;
    private JComboBox cmbCriterio;
    private JTextField txtTiempo;
    private JButton btnBuscar;
    private JTextField txtBuscar;

    private JTable tblDocumentos;

    public FrmOrdenamiento() {

        arbol = new Arbol(); // agregado
        tbOrdenamiento = new JToolBar();
        btnOrdenarBurbuja = new JButton();
        btnOrdenarInsercion = new JButton();
        btnOrdenarRapido = new JButton();
        cmbCriterio = new JComboBox();
        txtTiempo = new JTextField();

        btnBuscar = new JButton();
        txtBuscar = new JTextField();

        tblDocumentos = new JTable();

        setSize(600, 400);
        setTitle("Ordenamiento Documentos");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        btnOrdenarBurbuja.setIcon(new ImageIcon(getClass().getResource("/iconos/Ordenar.png")));
        btnOrdenarBurbuja.setToolTipText("Ordenar Burbuja");
        btnOrdenarBurbuja.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarBurbujaClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarBurbuja);

        btnOrdenarRapido.setIcon(new ImageIcon(getClass().getResource("/iconos/OrdenarRapido.png")));
        btnOrdenarRapido.setToolTipText("Ordenar Rapido");
        btnOrdenarRapido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarRapidoClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarRapido);

        btnOrdenarInsercion.setIcon(new ImageIcon(getClass().getResource("/iconos/OrdenarInsercion.png")));
        btnOrdenarInsercion.setToolTipText("Ordenar Inserción");
        btnOrdenarInsercion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarInsercionClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarInsercion);

        cmbCriterio.setModel(new DefaultComboBoxModel(
                new String[] { "Nombre Completo, Tipo de Documento", "Tipo de Documento, Nombre Completo" }));
        tbOrdenamiento.add(cmbCriterio);
        tbOrdenamiento.add(txtTiempo);

        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/iconos/Buscar.png")));
        btnBuscar.setToolTipText("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnBuscar(evt);
            }
        });
        tbOrdenamiento.add(btnBuscar);
        tbOrdenamiento.add(txtBuscar);

        JScrollPane spDocumentos = new JScrollPane(tblDocumentos);

        getContentPane().add(tbOrdenamiento, BorderLayout.NORTH);
        getContentPane().add(spDocumentos, BorderLayout.CENTER);

        String nombreArchivo = System.getProperty("user.dir")
                + "/src/datos/Datos.csv";

        //ServicioDocumento.desdeArchivo(nombreArchivo);
        //ServicioDocumento.mostrar(tblDocumentos);

        Arbol arbol = new Arbol();
        arbol.desdeArchivo(nombreArchivo);
        //arbol.recorrerInOrden();
        arbol.mostrar(tblDocumentos);

        
    }

    private void btnOrdenarBurbujaClick(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() >= 0) {
            Util.iniciarCronometro();
            ServicioDocumento.ordenarBurbuja(cmbCriterio.getSelectedIndex());
            txtTiempo.setText(Util.getTextoTiempoCronometro());
            ServicioDocumento.mostrar(tblDocumentos);
        } else {
            JOptionPane.showMessageDialog(null, "Elija el criterio de ordenamiento");
        }
    }

    private void btnOrdenarRapidoClick(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() >= 0) {
            Util.iniciarCronometro();
            ServicioDocumento.ordenarRapido(0, ServicioDocumento.getTamaño() - 1, cmbCriterio.getSelectedIndex());
            txtTiempo.setText(Util.getTextoTiempoCronometro());
            ServicioDocumento.mostrar(tblDocumentos);
        } else {
            JOptionPane.showMessageDialog(null, "Elija el criterio de ordenamiento");
        }
    }

    private void btnOrdenarInsercionClick(ActionEvent evt) {

    }

    private void btnBuscar(ActionEvent evt) {
    String nombreBuscado = txtBuscar.getText().trim();

    if (nombreBuscado.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor ingrese un nombre para buscar.");
        return;
    }

    Util.iniciarCronometro();
    Documento resultado = arbol.buscarPorNombreCompleto(nombreBuscado);
    txtTiempo.setText(Util.getTextoTiempoCronometro());

    if (resultado != null) {
        // Mostrar en la tabla solo el resultado encontrado
        Object[][] fila = {
            { resultado.getNombreCompleto(), resultado.getTipoDocumento() }
        };

        String[] columnas = { "Nombre Completo", "Tipo Documento" };
        tblDocumentos.setModel(new javax.swing.table.DefaultTableModel(fila, columnas));
    } else {
        JOptionPane.showMessageDialog(this, "No se encontró un documento con ese nombre.");
    }


    }

}