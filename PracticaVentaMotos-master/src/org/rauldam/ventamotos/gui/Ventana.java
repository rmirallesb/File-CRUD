package org.rauldam.ventamotos.gui;

import com.toedter.calendar.JDateChooser;
import org.rauldam.ventamotos.base.Cliente;
import org.rauldam.ventamotos.base.Moto;
import org.rauldam.ventamotos.base.Vendedor;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Aplicación para un concesionario de motocicletas en el que se puede introducir el Cliente,
 * la Moto comprada y el Vendedor que hace la venta.
 *
 * @author Raul Miralles
 * @version 1.0
 *
 */

public class Ventana extends Component {
    private JPanel panel1;
    private JLabel lbEstado;
    private JTabbedPane tpPestanas;

    private JTextField tfNombreCliente;
    private JTextField tfApellidoCliente;
    private JTextField tfDniCliente;
    private JTextField tfDireccionCliente;
    private JTextField tfProvinciaCliente;
    private JTextField tfTelefonoCliente;
    private JList jListClientes;
    private JButton btNuevoCliente;
    private JButton btModificarCliente;
    private JButton btGuardarCliente;
    private JButton btEliminarCliente;
    private JButton btCancelarCliente;
    private JList jListMotoCliente;
    private JList jListVendedorCliente;

    private JComboBox cbClienteMoto;
    private JTextField tfMarcaMoto;
    private JTextField tfModeloMoto;
    private JTextField tfNumChasisMoto;
    private JTextField tfMatriculaMoto;
    private JTextField tfPrecioMoto;
    private JDateChooser dcFechaMoto;
    private JList jListMotos;
    private JList jListVendedorMoto;
    private JButton btNuevoMoto;
    private JButton btModificarMoto;
    private JButton btGuardarMoto;
    private JButton btEliminarMoto;
    private JButton btCancelarMoto;

    private JComboBox cbClienteVendedor;
    private JComboBox cbMotoVendedor;
    private JTextField tfNombreVendedor;
    private JTextField tfApellidoVendedor;
    private JTextField tfDniVendedor;
    private JTextField tfIdVendedor;
    private JTextField tfSalarioVendedor;
    private JList jListVendedores;
    private JButton btNuevoVendedor;
    private JButton btModificarVendedor;
    private JButton btGuardarVendedor;
    private JButton btEliminarVendedor;
    private JButton btCancelarVendedor;
    private JTextField tfBuscarCliente;
    private JTextField tfBuscarMoto;
    private JTextField tfBuscarVendedor;
    private JButton btExportarXML;
    private JButton btGuardarComoCliente;

    private List<Cliente> listaClientes;
    private List<Moto> listaMotos;
    private List<Vendedor> listaVendedores;

    private int posicionClientes;
    private int posicionMotos;
    private int posicionVendedores;

    private boolean nuevoCliente;
    private boolean nuevaMoto;
    private boolean nuevoVendedor;

    private DefaultListModel<Cliente> modeloListaClientes;
    private DefaultListModel<Moto> modeloListaMotos;
    private DefaultListModel<Vendedor> modeloListaVendedores;

    private DefaultListModel<Moto> modeloListaMotosCliente;
    private DefaultListModel<Vendedor> modeloListaVendedoresCliente;
    private DefaultListModel<Vendedor> modeloListaVendedoresMoto;

    private final String FICHERO_CLIENTES = "clientes.dat";
    private final String FICHERO_MOTOS = "motos.dat";
    private final String FICHERO_VENDEDORES = "vendedores.dat";

    private enum Pestana {
        CLIENTE, MOTO, VENDEDOR
    }

    public Ventana() {

        /**
         * Estas condiciones permiten que si el fichero existe lo cargue
         * y si no inicializa la lista
         */
        if (new File(FICHERO_CLIENTES).exists())
            listaClientes = (ArrayList<Cliente>) cargarFichero(FICHERO_CLIENTES);
        else
            listaClientes = new ArrayList<>();

        if (new File(FICHERO_MOTOS).exists())
            listaMotos = (ArrayList<Moto>) cargarFichero(FICHERO_MOTOS);
        else
            listaMotos = new ArrayList<>();

        if (new File(FICHERO_VENDEDORES).exists())
            listaVendedores = (ArrayList<Vendedor>) cargarFichero(FICHERO_VENDEDORES);
        else
            listaVendedores = new ArrayList<>();

        posicionClientes = 0;
        posicionMotos = 0;
        posicionVendedores = 0;

        modeloListaClientes = new DefaultListModel<>();
        jListClientes.setModel(modeloListaClientes);
        modeloListaMotos = new DefaultListModel<>();
        jListMotos.setModel(modeloListaMotos);
        modeloListaVendedores = new DefaultListModel<>();
        jListVendedores.setModel(modeloListaVendedores);

        modeloListaMotosCliente = new DefaultListModel<>();
        jListMotoCliente.setModel(modeloListaMotosCliente);
        modeloListaVendedoresCliente = new DefaultListModel<>();
        jListVendedorCliente.setModel(modeloListaVendedoresCliente);
        modeloListaVendedoresMoto = new DefaultListModel<>();
        jListVendedorMoto.setModel(modeloListaVendedoresMoto);

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Listeners

        tpPestanas.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cargarPestanaActual();
            }
        });

        btGuardarComoCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarComo();
            }
        });
        btExportarXML.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarXML();
            }
        });

        jListClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionar();
            }
        });
        jListMotos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionar();
            }
        });
        jListVendedores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionar();
            }
        });

        tfBuscarCliente.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarCliente();
            }
        });
        tfBuscarMoto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarMoto();
            }
        });
        tfBuscarVendedor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarVendedor();
            }
        });

        btNuevoCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoCliente();
            }
        });
        btModificarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarCliente();
            }
        });
        btGuardarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCliente();
            }
        });
        btEliminarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCliente();
            }
        });
        btCancelarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });

        btNuevoMoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevaMoto();
            }
        });
        btModificarMoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarMoto();
            }
        });
        btGuardarMoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarMoto();
            }
        });
        btEliminarMoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarMoto();
            }
        });
        btCancelarMoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });

        btNuevoVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoVendedor();
            }
        });
        btModificarVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarVendedor();
            }
        });
        btGuardarVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarVendedor();
            }
        });
        btEliminarVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarVendedor();
            }
        });
        btCancelarVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });

        cargarPestanaActual();

    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Pestañas

    /**
     * Get para identificar en la pestaña que se encuentra
     */
    private Pestana getPestanaActual() {
        int indice = tpPestanas.getSelectedIndex();
        return Pestana.values()[indice];
    }

    /**
     * Carga los datos de la pestaña en la que se encuentra
     */
    private void cargarPestanaActual() {
        Cliente cliente = null;
        Moto moto = null;
        Vendedor vendedor = null;
        Pestana pestana = getPestanaActual();

        refrescarCombos();
        refrescarLista();

        switch (pestana) {
            case CLIENTE:
                if (listaClientes.size() == 0)
                    break;
                jListClientes.setSelectedIndex(0);
                cliente = listaClientes.get(posicionClientes);
                cargar(cliente);
                break;
            case MOTO:
                if (listaMotos.size() == 0)
                    break;
                jListMotos.setSelectedIndex(0);
                moto = listaMotos.get(posicionMotos);
                cargar(moto);
                break;
            case VENDEDOR:
                if (listaVendedores.size() == 0)
                    break;
                jListVendedores.setSelectedIndex(0);
                vendedor = listaVendedores.get(posicionVendedores);
                cargar(vendedor);
                break;
        }
    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Cargar

    /**
     * Carga el objeto en pantalla
     * @param objeto
     */
    private void cargar(Object objeto) {
        switch (getPestanaActual()) {
            case CLIENTE:
                posicionClientes = listaClientes.indexOf(objeto);
                lbEstado.setText((posicionClientes + 1) + " de " + listaClientes.size());

                Cliente cliente = (Cliente) objeto;
                tfNombreCliente.setText(cliente.getNombre());
                tfApellidoCliente.setText(cliente.getApellido());
                tfDniCliente.setText(cliente.getDni());
                tfDireccionCliente.setText(cliente.getDireccion());
                tfProvinciaCliente.setText(cliente.getProvincia());
                tfTelefonoCliente.setText(String.valueOf(cliente.getTelefono()));

                List<Moto> motosCliente = cliente.getMotos();
                modeloListaMotosCliente.removeAllElements();
                for (Moto m : motosCliente)
                    modeloListaMotosCliente.addElement(m);

                List<Vendedor> vendedoresCliente = cliente.getVendedores();
                modeloListaVendedoresCliente.removeAllElements();
                for (Vendedor v : vendedoresCliente)
                    modeloListaVendedoresCliente.addElement(v);

                btModificarCliente.setEnabled(true);
                btEliminarCliente.setEnabled(true);
                break;

            case MOTO:
                posicionMotos = listaMotos.indexOf(objeto);
                lbEstado.setText((posicionMotos + 1) + " de " + listaMotos.size());

                Moto moto = (Moto) objeto;
                tfMarcaMoto.setText(moto.getMarca());
                tfModeloMoto.setText(moto.getModelo());
                tfMatriculaMoto.setText(moto.getMatricula());
                tfNumChasisMoto.setText(moto.getNumeroChasis());
                tfPrecioMoto.setText(String.valueOf(moto.getPrecioFinal()));
                dcFechaMoto.setDate(moto.getFechaVenta());
                cbClienteMoto.setSelectedItem(moto.getCliente());

                List<Vendedor> vendedoresMoto = moto.getVendedores();
                modeloListaVendedoresMoto.removeAllElements();
                for (Vendedor v : vendedoresMoto)
                    modeloListaVendedoresMoto.addElement(v);

                btModificarMoto.setEnabled(true);
                btEliminarMoto.setEnabled(true);
                break;

            case VENDEDOR:
                posicionVendedores = listaVendedores.indexOf(objeto);
                lbEstado.setText((posicionVendedores + 1) + " de " + listaVendedores.size());

                Vendedor vendedor = (Vendedor) objeto;
                tfNombreVendedor.setText(vendedor.getNombre());
                tfApellidoVendedor.setText(vendedor.getApellido());
                tfDniVendedor.setText(vendedor.getDni());
                tfIdVendedor.setText(vendedor.getIdVendedor());
                tfSalarioVendedor.setText(String.valueOf(vendedor.getSalario()));
                cbClienteVendedor.setSelectedItem(vendedor.getCliente());
                cbMotoVendedor.setSelectedItem(vendedor.getMoto());

                btModificarVendedor.setEnabled(true);
                btEliminarVendedor.setEnabled(true);
                break;
        }
    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Seleccionar

    /**
     * Selecciona un objecto de la pestaña en la que estas
     */
    private void seleccionar() {
        int posicionSeleccionada;

        switch (getPestanaActual()) {
            case CLIENTE:
                posicionSeleccionada = jListClientes.getSelectedIndex();
                if (posicionSeleccionada == -1)
                    return;
                Cliente cliente = modeloListaClientes.getElementAt(posicionSeleccionada);
                cargar(cliente);
                break;
            case MOTO:
                posicionSeleccionada = jListMotos.getSelectedIndex();
                if (posicionSeleccionada == -1)
                    return;
                Moto moto = modeloListaMotos.getElementAt(posicionSeleccionada);
                cargar(moto);
                break;
            case VENDEDOR:
                posicionSeleccionada = jListVendedores.getSelectedIndex();
                if (posicionSeleccionada == -1)
                    return;
                Vendedor vendedor = modeloListaVendedores.getElementAt(posicionSeleccionada);
                cargar(vendedor);
                break;
        }
    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Refrescar

    /**
     * Refresca los combos de la pestaña en la que estas
     */
    private void refrescarCombos() {
        switch (getPestanaActual()) {
            case CLIENTE:
                // No
                break;
            case MOTO:
                cbClienteMoto.removeAllItems();
                for (Cliente cliente : listaClientes)
                    cbClienteMoto.addItem(cliente);
                break;
            case VENDEDOR:
                cbClienteVendedor.removeAllItems();
                for (Cliente cliente : listaClientes)
                    cbClienteVendedor.addItem(cliente);

                cbMotoVendedor.removeAllItems();
                for (Moto moto : listaMotos)
                    cbMotoVendedor.addItem(moto);
                break;
        }
    }

    /**
     * Refresca las listas de la pestaña en la que estas
     */
    private void refrescarLista() {
        switch (getPestanaActual()) {
            case CLIENTE:
                modeloListaClientes.removeAllElements();
                for (Cliente cliente : listaClientes)

                    modeloListaClientes.addElement(cliente);
                break;
            case MOTO:
                modeloListaMotos.removeAllElements();
                for (Moto moto : listaMotos)
                    modeloListaMotos.addElement(moto);
                break;
            case VENDEDOR:
                modeloListaVendedores.removeAllElements();
                for (Vendedor vendedor : listaVendedores)
                    modeloListaVendedores.addElement(vendedor);
                break;
        }
    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Nuevo

    /**
     * Permite introducir un objecto nuevo
     */
    private void nuevoCliente() {
        nuevoCliente = true;
        modoEdicion(true, true);
    }

    private void nuevaMoto() {
        nuevaMoto = true;
        modoEdicion(true, true);
    }

    private void nuevoVendedor() {
        nuevoVendedor = true;
        modoEdicion(true, true);
    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Modificar

    /**
     * Modifica un objeto
     */
    private void modificarCliente(){
        nuevoCliente = false;
        modoEdicion(true, false);
    }

    private void modificarMoto(){
        nuevaMoto = false;
        modoEdicion(true, false);
    }

    private void modificarVendedor(){
        nuevoVendedor = false;
        modoEdicion(true, false);
    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Guardar

    /**
     *  Guarda un objecto cumpliendo un par de condiciendones como campos obligatorios
     */
    private void guardarCliente(){
        Cliente cliente = null;

        if (nuevoCliente)
            cliente = new Cliente();
        else {
            if (JOptionPane.showConfirmDialog(null,"¿Está seguro?","Guardar",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
                return;
            cliente = listaClientes.get(posicionClientes);
        }

        if (tfNombreCliente.getText().equals("")){
            JOptionPane.showMessageDialog(null,"El nombre es obligatorio","Guardar",JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (tfApellidoCliente.getText().equals("")){
            JOptionPane.showMessageDialog(null,"El apellido es obligatorio","Guardar",JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (tfTelefonoCliente.getText().equals(""))
            tfTelefonoCliente.setText("0");

        cliente.setNombre(tfNombreCliente.getText());
        cliente.setApellido(tfApellidoCliente.getText());
        cliente.setDni(tfDniCliente.getText());
        cliente.setDireccion(tfDireccionCliente.getText());
        cliente.setProvincia(tfProvinciaCliente.getText());
        cliente.setTelefono(Integer.parseInt(tfTelefonoCliente.getText()));

        if (nuevoCliente)
            listaClientes.add(cliente);

        guardarFichero(listaClientes, FICHERO_CLIENTES);
        refrescarLista();
        modoNuevo();
    }

    private void guardarMoto(){
        Cliente cliente = null;
        Moto moto = null;

        if (nuevaMoto)
            moto = new Moto();
        else {
            if (JOptionPane.showConfirmDialog(null,"¿Está seguro?","Guardar",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
                return;
            moto = listaMotos.get(posicionMotos);
        }

        if (tfMarcaMoto.getText().equals("")){
            JOptionPane.showMessageDialog(null,"La marca es obligatoria","Guardar",JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (tfPrecioMoto.getText().equals(""))
            tfPrecioMoto.setText("0");

        if (tfModeloMoto.getText().equals("")){
            JOptionPane.showMessageDialog(null,"El modelo es obligatorio","Guardar",JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cbClienteMoto.getSelectedItem() == null){
            JOptionPane.showMessageDialog(null,"Elige Cliente","Guardar", JOptionPane.WARNING_MESSAGE);
            return;

        }
        moto.setMarca(tfMarcaMoto.getText());
        moto.setModelo(tfModeloMoto.getText());
        moto.setNumeroChasis(tfNumChasisMoto.getText());
        moto.setMatricula(tfMatriculaMoto.getText());
        moto.setPrecioFinal(Float.parseFloat(tfPrecioMoto.getText()));
        moto.setFechaVenta(dcFechaMoto.getDate());

        cliente = (Cliente) cbClienteMoto.getSelectedItem();
        moto.setCliente(cliente);
        cliente.getMotos().add(moto);

        if (nuevaMoto)
            listaMotos.add(moto);

        guardarFichero(listaMotos, FICHERO_MOTOS);
        refrescarLista();
        modoNuevo();
    }

    private void guardarVendedor(){
        Cliente cliente = null;
        Moto moto = null;
        Vendedor vendedor = null;

        if (nuevoVendedor)
            vendedor = new Vendedor();
        else {
            if (JOptionPane.showConfirmDialog(null,"¿Está seguro?","Guardar",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
                return;
            vendedor = listaVendedores.get(posicionVendedores);
        }

        if (tfNombreVendedor.getText().equals("")){
            JOptionPane.showMessageDialog(null,"El nombre es obligatorio","Guardar",JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (tfApellidoVendedor.getText().equals("")){
            JOptionPane.showMessageDialog(null,"El apellido es obligatorio","Guardar",JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (tfSalarioVendedor.getText().equals(""))
            tfSalarioVendedor.setText("0");

        if (cbClienteVendedor.getSelectedItem() == null){
            JOptionPane.showMessageDialog(null,"Elige un cliente","Guardar",JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cbMotoVendedor.getSelectedItem() == null){
            JOptionPane.showMessageDialog(null,"Elige una moto","Guardar",JOptionPane.WARNING_MESSAGE);
            return;
        }

        vendedor.setNombre(tfNombreVendedor.getText());
        vendedor.setApellido(tfApellidoVendedor.getText());
        vendedor.setDni(tfDniVendedor.getText());
        vendedor.setIdVendedor(tfIdVendedor.getText());
        vendedor.setSalario(Integer.parseInt(tfSalarioVendedor.getText()));

        cliente = (Cliente) cbClienteVendedor.getSelectedItem();
        vendedor.setCliente(cliente);
        cliente.getVendedores().add(vendedor);

        moto = (Moto) cbMotoVendedor.getSelectedItem();
        vendedor.setMoto(moto);
        moto.getVendedores().add(vendedor);

        if (nuevoVendedor)
            listaVendedores.add(vendedor);
        guardarFichero(listaVendedores, FICHERO_VENDEDORES);
        refrescarLista();
        modoNuevo();
    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Buscar

    /**
     *  Filtra las palabras que introduces y con ello haces una busqueda
     */
    private void buscarCliente() {

        if (tfBuscarCliente.getText().equals("")) {
            refrescarLista();
            return;
        }
        if (tfBuscarCliente.getText().length() > 2)
            modeloListaClientes.removeAllElements();
        modeloListaClientes.removeAllElements();
        for (Cliente cliente : listaClientes) {
            if (cliente.getNombre().contains(tfBuscarCliente.getText()) || cliente.getApellido().contains(tfBuscarCliente.getText()))
                modeloListaClientes.addElement(cliente);
        }
    }

    private void buscarMoto() {
        if (tfBuscarMoto.getText().equals("")) {
            refrescarLista();
            return;
        }
        if (tfBuscarMoto.getText().length() > 2)
            modeloListaMotos.removeAllElements();

        modeloListaMotos.removeAllElements();
        for (Moto moto : listaMotos) {
            if (moto.getMarca().contains(tfBuscarMoto.getText()) || moto.getModelo().contains(tfBuscarMoto.getText()))
                modeloListaMotos.addElement(moto);
        }
    }

    private void buscarVendedor() {
         if (tfBuscarVendedor.getText().equals("")) {
            refrescarLista();
            return;
         }
         if (tfBuscarVendedor.getText().length() > 2)
            modeloListaVendedores.removeAllElements();

        modeloListaVendedores.removeAllElements();
        for (Vendedor vendedor : listaVendedores) {
            if (vendedor.getNombre().contains(tfBuscarVendedor.getText()) || vendedor.getApellido().contains(tfBuscarVendedor.getText()))
                modeloListaVendedores.addElement(vendedor);
        }
    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Eliminar

    /**
     * Elimina un objeto
     */
    private void eliminarCliente(){
        if (JOptionPane.showConfirmDialog(null,"¿Desea eliminar?","Eliminar",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
            return;
        listaClientes.remove(posicionClientes);
        if (listaClientes.size()>0)
            if (posicionClientes == listaClientes.size())
                posicionClientes--;
        guardarFichero(listaClientes, FICHERO_CLIENTES);
        cargarPestanaActual();
        lbEstado.setText("Eliminado correctamente");
    }

    private void eliminarMoto(){
        if (JOptionPane.showConfirmDialog(null,"¿Desea eliminar?","Eliminar",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
            return;
        listaMotos.remove(posicionMotos);
        if (listaMotos.size()>0)
            if (posicionMotos == listaMotos.size())
                posicionMotos--;
        guardarFichero(listaMotos, FICHERO_MOTOS);
        cargarPestanaActual();
        lbEstado.setText("Eliminado correctamente");
    }

    private void eliminarVendedor(){
        if (JOptionPane.showConfirmDialog(null,"¿Desea eliminar?","Eliminar",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
            return;
        listaVendedores.remove(posicionVendedores);
        if (listaVendedores.size()>0)
            if (posicionVendedores == listaVendedores.size())
                posicionVendedores--;
        guardarFichero(listaVendedores, FICHERO_VENDEDORES);
        cargarPestanaActual();
        lbEstado.setText("Eliminado correctamente");
    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Cancelar

    /**
     * Deja los textfield en blanco del modoEdicion
     */
    private void cancelar() {
        if (JOptionPane.showConfirmDialog(null,"¿Desea cancelar?","Cancelar",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
            return;
        modoEdicion(false, false);
        cargarPestanaActual();
    }
//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Cargar y guardar fichero

    /**
     * Guardas los datos en un fichero
     * @param nombreFichero
     * @return
     */
    private void guardarFichero(Object objeto, String nombreFichero) {

        ObjectOutputStream serializador = null;

        try {
            serializador = new ObjectOutputStream(new FileOutputStream(nombreFichero));
            serializador.writeObject(objeto);
        } catch (IOException ioex) {
            JOptionPane.showMessageDialog(null, "Error al guardar el fichero", "Guardar", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cargas los datos del fichero cada vez que abras el programa
     * @param nombreFichero
     * @return
     */
    private Object cargarFichero(String nombreFichero) {

        ObjectInputStream deserializador = null;

        try {
            deserializador = new ObjectInputStream(new FileInputStream(nombreFichero));
            Object objeto = deserializador.readObject();
            return objeto;
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(null, "Error al cargar el fichero", "Cargar", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, "Error al cargar el fichero", "Cargar", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioex) {
            JOptionPane.showMessageDialog(null, "Error al cargar el fichero", "Cargar", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Guardar como

    /**
     * Exportas los datos y los guardas en la ruta deseada
     */
    private void guardarComo() {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Guardar como...");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setFileFilter(new FileNameExtensionFilter("Text files (*.txt *.bat)", ".bat"));
        if(jfc.showSaveDialog(this) == JFileChooser.CANCEL_OPTION){
            return;
        }
        ObjectOutputStream out = null;
        try{
            out = new ObjectOutputStream(new FileOutputStream(jfc.getSelectedFile().getAbsolutePath()));
            out.writeObject(listaClientes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Exportar fichero a XML

    /**
     * Exportas los datos del objecto cliente en un fichero XML
     */
    private void exportarXML(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document documento = null;

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation dom = builder.getDOMImplementation();
            documento = dom.createDocument(null,  "xml", null);

            Element raiz = documento.createElement("Clientes");
            documento.getDocumentElement().appendChild(raiz);

            Element nodo = null, nodoDatos = null;
            Text texto = null;


            for (Cliente cliente : listaClientes) {
                nodo = documento.createElement("Cliente");
                raiz.appendChild(nodo);

                nodoDatos = documento.createElement("Nombre");
                nodo.appendChild(nodoDatos);

                texto = documento.createTextNode(cliente.getNombre());
                nodoDatos.appendChild(texto);

                nodoDatos = documento.createElement("Apellidos");
                nodo.appendChild(nodoDatos);

                texto = documento.createTextNode(cliente.getApellido());
                nodoDatos.appendChild(texto);

                nodoDatos = documento.createElement("DNI");
                nodo.appendChild(nodoDatos);

                texto = documento.createTextNode(cliente.getDni());
                nodoDatos.appendChild(texto);

                nodoDatos = documento.createElement("Provincia");
                nodo.appendChild(nodoDatos);

                texto = documento.createTextNode(cliente.getProvincia());
                nodoDatos.appendChild(texto);

                nodoDatos = documento.createElement("Telefono");
                nodo.appendChild(nodoDatos);

                texto = documento.createTextNode(String.valueOf(cliente.getTelefono()));
                nodoDatos.appendChild(texto);
            }

            Source source = new DOMSource(documento);
            Result resultado = new StreamResult(new File("clientes.xml"));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, resultado);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Modos de cajas y botones

    /**
     * Modo que deja activo solo el boton nuevo dejando el resto desactivados
     */
    private void modoNuevo(){
        btNuevoCliente.setEnabled(true);
        btModificarCliente.setEnabled(false);
        btGuardarCliente.setEnabled(false);
        btEliminarCliente.setEnabled(false);
        btCancelarCliente.setEnabled(false);

        btNuevoMoto.setEnabled(true);
        btModificarMoto.setEnabled(false);
        btGuardarMoto.setEnabled(false);
        btEliminarMoto.setEnabled(false);
        btCancelarMoto.setEnabled(false);

        btNuevoVendedor.setEnabled(true);
        btModificarVendedor.setEnabled(false);
        btGuardarVendedor.setEnabled(false);
        btEliminarVendedor.setEnabled(false);
        btCancelarVendedor.setEnabled(false);
    }


    /**
     * Modo para activar/desactivar los botones y los textfields segun convenga
     * interpretandolo con boleanos
     * @param editable Activa o desactiva botones y textfields
     * @param limpiar Limpia el contenido y deja las cajas vacias
     */
    private void modoEdicion(boolean editable, boolean limpiar){
        switch (getPestanaActual()) {
            case CLIENTE:
                if (limpiar) {
                    tfNombreCliente.setText("");
                    tfApellidoCliente.setText("");
                    tfDniCliente.setText("");
                    tfDireccionCliente.setText("");
                    tfProvinciaCliente.setText("");
                    tfTelefonoCliente.setText("");
                }
                tfNombreCliente.setEditable(editable);
                tfApellidoCliente.setEditable(editable);
                tfDniCliente.setEditable(editable);
                tfDireccionCliente.setEditable(editable);
                tfProvinciaCliente.setEditable(editable);
                tfTelefonoCliente.setEditable(editable);

                btNuevoCliente.setEnabled(!editable);
                btModificarCliente.setEnabled(!editable);
                btGuardarCliente.setEnabled(editable);
                btEliminarCliente.setEnabled(!editable);
                btCancelarCliente.setEnabled(editable);
                break;
            case MOTO:
                if (limpiar) {
                    tfMarcaMoto.setText("");
                    tfModeloMoto.setText("");
                    tfNumChasisMoto.setText("");
                    tfMatriculaMoto.setText("");
                    tfPrecioMoto.setText("");
                    dcFechaMoto.setDate(null);
                    cbClienteMoto.setSelectedItem(null);
                }
                tfMarcaMoto.setEditable(editable);
                tfModeloMoto.setEditable(editable);
                tfNumChasisMoto.setEditable(editable);
                tfMatriculaMoto.setEditable(editable);
                tfPrecioMoto.setEditable(editable);
                dcFechaMoto.setEnabled(editable);
                cbClienteMoto.setEnabled(editable);

                btNuevoMoto.setEnabled(!editable);
                btModificarMoto.setEnabled(!editable);
                btGuardarMoto.setEnabled(editable);
                btEliminarMoto.setEnabled(!editable);
                btCancelarMoto.setEnabled(editable);
                break;
            case VENDEDOR:
                if (limpiar) {
                    tfNombreVendedor.setText("");
                    tfApellidoVendedor.setText("");
                    tfDniVendedor.setText("");
                    tfIdVendedor.setText("");
                    tfSalarioVendedor.setText("");
                    cbClienteVendedor.setSelectedItem(null);
                    cbMotoVendedor.setSelectedItem(null);
                }

                tfNombreVendedor.setEditable(editable);
                tfApellidoVendedor.setEditable(editable);
                tfDniVendedor.setEditable(editable);
                tfIdVendedor.setEditable(editable);
                tfSalarioVendedor.setEditable(editable);
                cbClienteVendedor.setEnabled(editable);
                cbMotoVendedor.setEnabled(editable);

                btNuevoVendedor.setEnabled(!editable);
                btModificarVendedor.setEnabled(!editable);
                btGuardarVendedor.setEnabled(editable);
                btEliminarVendedor.setEnabled(!editable);
                btCancelarVendedor.setEnabled(editable);
                break;
        }
    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   Main

    /**
     * En el main hay un atributo que centra la localizacion del programa en el centro de la pantalla
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Ventana");
        frame.setContentPane(new Ventana().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
