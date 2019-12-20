/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ferreteria;

import dao.CategoriaImp;
import dao.ClienteImp;
import dao.DaoComunImp;
import dao.DetalleFacturaImp;
import dao.FacturaImp;
import dao.Factura_C_VImp;
import dao.ProductoImp;
import dao.ProveedorImp;
import java.awt.HeadlessException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import modelos.Categoria;
import modelos.Cliente;
import modelos.DetalleFactura;
import modelos.Factura;
import modelos.Factura_C_V;
import modelos.Producto;
import modelos.Proveedor;
import recursos.Comun;

/**
 *
 * @author ***GKSoftware***
 */
public class VistaPrincipal extends javax.swing.JFrame {

    private CategoriaImp daoCategoria;
    private ProveedorImp daoProveedor;
    private FacturaImp daoFactura;
    private DetalleFacturaImp daoDetalleFactura;
    private Factura_C_VImp daoFacturac_f;
    private ProductoImp daoProducto;
    private ClienteImp daoCliente;
    private DaoComunImp daoComunImp;
    private List<Producto> productos;
    private List<Producto> productosCarrito;
    private Factura factura;
    private Factura facturaCarrito;
    private Producto productoSeleccionado;

    /**
     * Creates new form VistaPrincipal
     */
    public VistaPrincipal() {
        initComponents();
        DecimalFormat df = new DecimalFormat("###,###.###");
        initVista();
        tabPane.addChangeListener((ChangeEvent e) -> {
            initVista();
        });
    }
    
    private void initVista() {
        productos = new ArrayList<>();
        productosCarrito = new ArrayList<>();
        daoCategoria = new CategoriaImp();
        daoProveedor = new ProveedorImp();
        daoFactura = new FacturaImp();
        daoProducto = new ProductoImp();
        daoDetalleFactura = new DetalleFacturaImp();
        daoFacturac_f = new Factura_C_VImp();
        daoComunImp = new DaoComunImp();
        daoCliente = new ClienteImp();
        cargarTablaCategoria();
        cargarTablaProveedor();
        cargarTablaProductos();
        cargarTablaFacturas();
        cargarTablaFacturasVentas();
        buscarCompras.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }

            private void filtrar() {
                if (buscarCompras.getText().isEmpty()) {
                    cargarTablaFacturas();
                } else {
                    cargarTablaFacturasFiltrada(daoComunImp.buscar(buscarCompras.getText()));
                }
            }
        });
        buscarVentas.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }

            private void filtrar() {
                if (buscarVentas.getText().isEmpty()) {
                    cargarTablaFacturas();
                } else {
                    cargarTablaFacturasVentasFiltrada(daoComunImp.buscarVentas(buscarVentas.getText()));
                }
            }
        });
        
        buscarProducto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }

            private void filtrar() {
                if (buscarProducto.getText().isEmpty()) {
                    cargarTablaProductos();
                } else {
                    cargarTablaProductosFiltrada(daoComunImp.buscarProducto(buscarProducto.getText()));
                }
            }
        });
    }

    private void cargarTablaProductos() {
        DefaultTableModel infotick = (DefaultTableModel) tblProductos.getModel();
        infotick.setRowCount(0);
        List<Producto> productoDao = daoProducto.lista();
        if (productoDao.size() > 0) {
            for (int i = 0; i < productoDao.size(); i++) {
                Object[] datos = {productoDao.get(i).getIdproducto(), productoDao.get(i).getNombre(), productoDao.get(i).getPrecioVenta(), productoDao.get(i).getDisponibilidad(), productoDao.get(i).getCategoria()};
                infotick.addRow(datos);
            }
        }
    }
  
    private void cargarTablaProductosFiltrada(List<Producto> productoDao) {
        DefaultTableModel infotick = (DefaultTableModel) tblProductos.getModel();
        infotick.setRowCount(0);
        if (productoDao.size() > 0) {
            for (int i = 0; i < productoDao.size(); i++) {
                Object[] datos = {productoDao.get(i).getIdproducto(), productoDao.get(i).getNombre(), productoDao.get(i).getPrecioVenta(), productoDao.get(i).getDisponibilidad(), productoDao.get(i).getCategoria()};
                infotick.addRow(datos);
            }
        }
    }
    
    private void cargarTablaCategoria() {
        DefaultTableModel infotick = (DefaultTableModel) categoriaTbl.getModel();
        infotick.setRowCount(0);
        List<Categoria> categorias = daoCategoria.lista();
        comboCategoria.removeAllItems();
        comboCategoria.addItem("Categoria*");
        if (categorias.size() > 0) {
            for (int i = 0; i < categorias.size(); i++) {
                Object[] datos = {categorias.get(i).getCategoria(), categorias.get(i).getNombre(), categorias.get(i).getCantidadDisponible()};
                infotick.addRow(datos);
                comboCategoria.addItem(categorias.get(i).getCategoria() + "-" + categorias.get(i).getNombre());
            }
        }
    }

    private void cargarTablaFacturasFiltrada(List<DaoComunImp.F_CompraNested> filtro) {
        DefaultTableModel infotick = (DefaultTableModel) tblCompras.getModel();
        infotick.setRowCount(0);
        double totalC = 0;
        if (filtro.size() > 0) {
            for (int i = 0; i < filtro.size(); i++) {
                totalC += filtro.get(i).getTotalCompra();
                Object[] datos = {filtro.get(i).getCodigoFactura(), filtro.get(i).getCodigoProveedor(), filtro.get(i).getCantidadProductos(), filtro.get(i).getTotalCompra(), filtro.get(i).getFecha()};
                infotick.addRow(datos);
            }
        }
        System.out.println(totalC);
        totalComprasLbl.setText(String.format("Total compras: $%s", totalC));
    }

    private void cargarTablaFacturasVentasFiltrada(List<DaoComunImp.V_CompraNested> filtro) {
        DefaultTableModel infotick = (DefaultTableModel) tblVentas.getModel();
        infotick.setRowCount(0);
        double totalC = 0;
        if (filtro.size() > 0) {
            for (int i = 0; i < filtro.size(); i++) {
                totalC += filtro.get(i).getTotalCompra();
                Object[] datos = {filtro.get(i).getCodigoFactura(), filtro.get(i).getCodigoCliente(), filtro.get(i).getCantidadProductos(), filtro.get(i).getTotalCompra()};
                infotick.addRow(datos);
            }
        }
        System.out.println(totalC);
        totalVentas.setText(String.format("Total ventas: $%s", totalC));
    }

    private void cargarTablaFacturas() {
        DefaultTableModel infotick = (DefaultTableModel) tblCompras.getModel();
        infotick.setRowCount(0);
        List<DaoComunImp.F_CompraNested> f_co = daoComunImp.listarCompras();
        double totalC = 0;
        if (f_co.size() > 0) {
            for (int i = 0; i < f_co.size(); i++) {
                totalC += f_co.get(i).getTotalCompra();
                Object[] datos = {f_co.get(i).getCodigoFactura(), f_co.get(i).getCodigoProveedor(), f_co.get(i).getCantidadProductos(), f_co.get(i).getTotalCompra(), f_co.get(i).getFecha()};
                infotick.addRow(datos);
            }
        }
        System.out.println(totalC);
        totalComprasLbl.setText(String.format("Total compras: $%s", totalC));
    }

    private void cargarTablaFacturasVentas() {
        DefaultTableModel infotick = (DefaultTableModel) tblVentas.getModel();
        infotick.setRowCount(0);
        List<DaoComunImp.V_CompraNested> f_co = daoComunImp.listarVentas();
        double totalC = 0;
        if (f_co.size() > 0) {
            for (int i = 0; i < f_co.size(); i++) {
                totalC += f_co.get(i).getTotalCompra();
                Object[] datos = {f_co.get(i).getCodigoFactura(), f_co.get(i).getCodigoCliente(), f_co.get(i).getCantidadProductos(), f_co.get(i).getTotalCompra()};
                infotick.addRow(datos);
            }
        }
        System.out.println(totalC);
        totalVentas.setText(String.format("Total ventas: $%s", totalC));
    }

    private void cargarTablaFactProductos() {
        DefaultTableModel infotick = (DefaultTableModel) productosFacTbl.getModel();
        infotick.setRowCount(0);
        if (productos.size() > 0) {
            for (int i = 0; i < productos.size(); i++) {
                Object[] datos = {productos.get(i).getIdproducto(), productos.get(i).getNombre(), productos.get(i).getPrecioVenta(), productos.get(i).getDisponibilidad(), productos.get(i).getCategoria()};
                infotick.addRow(datos);
            }
        }
    }

    private void cargarTablaProveedor() {
        DefaultTableModel infotick = (DefaultTableModel) proveedoresTbl.getModel();
        infotick.setRowCount(0);
        List<Proveedor> proveedores = daoProveedor.lista();
        comboProveedor.removeAllItems();
        comboProveedor.addItem("Proveedor*");
        if (proveedores.size() > 0) {
            for (int i = 0; i < proveedores.size(); i++) {
                Object[] datos = {proveedores.get(i).getIdproveedor(), proveedores.get(i).getNombre(), proveedores.get(i).getCiudad(), proveedores.get(i).getTelefono(), proveedores.get(i).getNombreContacto()};
                infotick.addRow(datos);
                comboProveedor.addItem(proveedores.get(i).getIdproveedor() + "-" + proveedores.get(i).getNombre());
            }
        }
    }

    private void cargarACarrito() {
        DefaultTableModel infotick = (DefaultTableModel) tblCarrito.getModel();
        infotick.setRowCount(0);
        if (productosCarrito.size() > 0) {
            for (int i = 0; i < productosCarrito.size(); i++) {
                Object[] datos = {productosCarrito.get(i).getIdproducto(), productosCarrito.get(i).getNombre(), productosCarrito.get(i).getPrecioVenta(), productosCarrito.get(i).getDisponibilidad()};
                infotick.addRow(datos);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        tabPane = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        codigoProducto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        nombreProducto = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        precioVenta = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        precioCompra = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        cantidadDisponible = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        comboCategoria = new javax.swing.JComboBox<>();
        btnRegistrarProducto = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        facturaCompra = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        cantidadProductos = new javax.swing.JTextField();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        totalProductos = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        comboProveedor = new javax.swing.JComboBox<>();
        btnFinalizar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        productosFacTbl = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        buscarVentas = new javax.swing.JTextField();
        jSeparator18 = new javax.swing.JSeparator();
        jLabel26 = new javax.swing.JLabel();
        totalVentas = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblVentas = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblCompras = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        buscarCompras = new javax.swing.JTextField();
        jSeparator19 = new javax.swing.JSeparator();
        jLabel27 = new javax.swing.JLabel();
        totalComprasLbl = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        productoBuscar = new javax.swing.JTextField();
        jSeparator16 = new javax.swing.JSeparator();
        nombreProductolbl = new javax.swing.JLabel();
        btnBuscarProducto = new javax.swing.JButton();
        precioProductoLbl = new javax.swing.JLabel();
        disponibilidadLbl = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        cantidadAgregar = new javax.swing.JTextField();
        jSeparator17 = new javax.swing.JSeparator();
        btnAgregarCarrito = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        clienteVentas = new javax.swing.JTextField();
        btnBuscarCliente = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblCarrito = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        totalLbl = new javax.swing.JLabel();
        btnFacturar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        codigoProveedor = new javax.swing.JTextField();
        jSeparator11 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        nombreProveedor = new javax.swing.JTextField();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel14 = new javax.swing.JLabel();
        ciudadProveedor = new javax.swing.JTextField();
        jSeparator13 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        telefonoProveedor = new javax.swing.JTextField();
        jSeparator14 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        contactoProveedor = new javax.swing.JTextField();
        jSeparator15 = new javax.swing.JSeparator();
        btnRegistrarProveedor = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        proveedoresTbl = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        idcategoria = new javax.swing.JTextField();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        nombreCategoria = new javax.swing.JTextField();
        jSeparator10 = new javax.swing.JSeparator();
        btnRegistrar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        categoriaTbl = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        buscarProducto = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tabPane.setBackground(new java.awt.Color(255, 255, 255));
        tabPane.setForeground(new java.awt.Color(0, 0, 0));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel7MouseClicked(evt);
            }
        });

        codigoProducto.setBackground(new java.awt.Color(255, 255, 255));
        codigoProducto.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        codigoProducto.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        codigoProducto.setBorder(null);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Id producto:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Nombre:");

        nombreProducto.setBackground(new java.awt.Color(255, 255, 255));
        nombreProducto.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        nombreProducto.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nombreProducto.setBorder(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Precio venta:");

        precioVenta.setBackground(new java.awt.Color(255, 255, 255));
        precioVenta.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        precioVenta.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        precioVenta.setBorder(null);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Precio compra:");

        precioCompra.setBackground(new java.awt.Color(255, 255, 255));
        precioCompra.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        precioCompra.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        precioCompra.setBorder(null);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Cantidad disponible:");

        cantidadDisponible.setBackground(new java.awt.Color(255, 255, 255));
        cantidadDisponible.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cantidadDisponible.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        cantidadDisponible.setBorder(null);

        comboCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Categoria*" }));

        btnRegistrarProducto.setText("Registrar");
        btnRegistrarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarProductoActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("N° Factura:");

        facturaCompra.setBackground(new java.awt.Color(255, 255, 255));
        facturaCompra.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        facturaCompra.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        facturaCompra.setBorder(null);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Cantidad productos:");

        cantidadProductos.setEditable(false);
        cantidadProductos.setBackground(new java.awt.Color(255, 255, 255));
        cantidadProductos.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cantidadProductos.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        cantidadProductos.setBorder(null);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Total productos:");

        totalProductos.setEditable(false);
        totalProductos.setBackground(new java.awt.Color(255, 255, 255));
        totalProductos.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        totalProductos.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        totalProductos.setBorder(null);

        comboProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Proveedor*" }));

        btnFinalizar.setText("Finalizar");
        btnFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSeparator6)
                            .addComponent(facturaCompra, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboCategoria, javax.swing.GroupLayout.Alignment.LEADING, 0, 160, Short.MAX_VALUE)
                            .addComponent(codigoProducto, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(529, 529, 529)
                        .addComponent(btnFinalizar, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(nombreProducto, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator7)
                                .addComponent(cantidadProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8))
                        .addGap(56, 56, 56)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(precioVenta, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(totalProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addGap(46, 46, 46)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(precioCompra)
                                        .addComponent(jSeparator4, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                        .addComponent(jLabel5)))))
                        .addGap(47, 47, 47)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRegistrarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cantidadDisponible)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jLabel6))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(facturaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cantidadProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(comboProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator8, javax.swing.GroupLayout.DEFAULT_SIZE, 10, Short.MAX_VALUE)
                            .addComponent(jSeparator7))))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addGap(4, 4, 4)
                            .addComponent(cantidadDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(4, 4, 4)
                            .addComponent(precioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addGap(4, 4, 4)
                            .addComponent(precioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(4, 4, 4)
                            .addComponent(codigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(4, 4, 4)
                        .addComponent(nombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistrarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(btnFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        productosFacTbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        productosFacTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Precio", "Disponibles", "Categoria"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        productosFacTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productosFacTblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(productosFacTbl);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPane.addTab("Compras", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setText("Buscar:");

        buscarVentas.setBackground(new java.awt.Color(255, 255, 255));
        buscarVentas.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        buscarVentas.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        buscarVentas.setBorder(null);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setText("VENTAS");

        totalVentas.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalVentas.setForeground(new java.awt.Color(0, 0, 0));
        totalVentas.setText("Total ventas: $0");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(buscarVentas, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .addComponent(jLabel24)
                            .addComponent(jSeparator18))
                        .addGap(0, 863, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(totalVentas)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(totalVentas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator18, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tblVentas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod Factura", "Cod Cliente", "Cantidad Productos", "Total venta"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tblVentas);

        tblCompras.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod Factura", "Cod Proveedor", "Cantidad Productos", "Total Compra", "Fecha"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tblCompras);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 0));
        jLabel25.setText("COMPRAS");

        buscarCompras.setBackground(new java.awt.Color(255, 255, 255));
        buscarCompras.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        buscarCompras.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        buscarCompras.setBorder(null);

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 0, 0));
        jLabel27.setText("Buscar:");

        totalComprasLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalComprasLbl.setForeground(new java.awt.Color(0, 0, 0));
        totalComprasLbl.setText("Total compras: $0");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(buscarCompras, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .addComponent(jLabel25)
                            .addComponent(jSeparator19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(totalComprasLbl))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(totalComprasLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(buscarCompras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1037, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane5)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPane.addTab("Facturas", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("ID Producto:");

        productoBuscar.setBackground(new java.awt.Color(255, 255, 255));
        productoBuscar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        productoBuscar.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        productoBuscar.setBorder(null);

        nombreProductolbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nombreProductolbl.setForeground(new java.awt.Color(0, 0, 0));
        nombreProductolbl.setText("Nombre:");

        btnBuscarProducto.setText("Buscar producto");
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });

        precioProductoLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        precioProductoLbl.setForeground(new java.awt.Color(0, 0, 0));
        precioProductoLbl.setText("Precio:");

        disponibilidadLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        disponibilidadLbl.setForeground(new java.awt.Color(0, 0, 0));
        disponibilidadLbl.setText("Disponibles:");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setText("Cantidad a agregar:");

        cantidadAgregar.setBackground(new java.awt.Color(255, 255, 255));
        cantidadAgregar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cantidadAgregar.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        cantidadAgregar.setBorder(null);

        btnAgregarCarrito.setText("Agregar al carrito");
        btnAgregarCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCarritoActionPerformed(evt);
            }
        });

        jButton7.setText("Agregar cliente");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 0, 0));
        jLabel28.setText("Cliente: ");

        clienteVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clienteVentasActionPerformed(evt);
            }
        });

        btnBuscarCliente.setText("Buscar cliente");
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel17))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(productoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 735, Short.MAX_VALUE)
                        .addComponent(btnBuscarProducto))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSeparator17, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cantidadAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addComponent(nombreProductolbl)
                                        .addGap(378, 378, 378)
                                        .addComponent(precioProductoLbl)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(disponibilidadLbl)
                                    .addComponent(btnAgregarCarrito)))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(clienteVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addGap(1, 1, 1)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(productoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clienteVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreProductolbl)
                    .addComponent(precioProductoLbl)
                    .addComponent(disponibilidadLbl))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(4, 4, 4)
                        .addComponent(cantidadAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator17, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAgregarCarrito, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        tblCarrito.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblCarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre", "Precio", "Cantidad"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblCarrito);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("Valor total:");

        totalLbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalLbl.setForeground(new java.awt.Color(0, 0, 0));
        totalLbl.setText("$0");

        btnFacturar.setText("Facturar");
        btnFacturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnFacturar))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(totalLbl)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(totalLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(btnFacturar)
                .addContainerGap())
        );

        tabPane.addTab("Ventas", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel9MouseClicked(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Codigo proveedor:");

        codigoProveedor.setBackground(new java.awt.Color(255, 255, 255));
        codigoProveedor.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        codigoProveedor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        codigoProveedor.setText(" ");
        codigoProveedor.setBorder(null);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Nombre:");

        nombreProveedor.setBackground(new java.awt.Color(255, 255, 255));
        nombreProveedor.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        nombreProveedor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nombreProveedor.setText(" ");
        nombreProveedor.setBorder(null);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Ciudad:");

        ciudadProveedor.setBackground(new java.awt.Color(255, 255, 255));
        ciudadProveedor.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        ciudadProveedor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        ciudadProveedor.setText(" ");
        ciudadProveedor.setBorder(null);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("Telefono:");

        telefonoProveedor.setBackground(new java.awt.Color(255, 255, 255));
        telefonoProveedor.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        telefonoProveedor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        telefonoProveedor.setText(" ");
        telefonoProveedor.setBorder(null);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("Nombre de contacto:");

        contactoProveedor.setBackground(new java.awt.Color(255, 255, 255));
        contactoProveedor.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        contactoProveedor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        contactoProveedor.setText(" ");
        contactoProveedor.setBorder(null);

        btnRegistrarProveedor.setText("Registrar");
        btnRegistrarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel12))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(codigoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(74, 74, 74)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(77, 77, 77)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ciudadProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(telefonoProveedor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(159, 159, 159))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRegistrarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(4, 4, 4)
                        .addComponent(codigoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(4, 4, 4)
                        .addComponent(nombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(4, 4, 4)
                        .addComponent(ciudadProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addGap(4, 4, 4)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contactoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistrarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(telefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        proveedoresTbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        proveedoresTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "nombre", "Ciudad", "Telefono", "Contacto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        proveedoresTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                proveedoresTblMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(proveedoresTbl);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabPane.addTab("Proveedores", jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel8MouseClicked(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("ID Categoria:");

        idcategoria.setBackground(new java.awt.Color(255, 255, 255));
        idcategoria.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        idcategoria.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        idcategoria.setBorder(null);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Nombre:");

        nombreCategoria.setBackground(new java.awt.Color(255, 255, 255));
        nombreCategoria.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        nombreCategoria.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nombreCategoria.setBorder(null);

        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10)
                    .addComponent(idcategoria, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                    .addComponent(jSeparator9))
                .addGap(65, 65, 65)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11)
                    .addComponent(nombreCategoria)
                    .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRegistrar)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idcategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnRegistrar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        categoriaTbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        categoriaTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id categoria", "Nombre", "Cantidad disponible"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        categoriaTbl.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        categoriaTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                categoriaTblMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(categoriaTbl);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1037, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPane.addTab("Categorias", jPanel6);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("Buscar por nombre:");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(buscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        tblProductos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre", "Precio", "Disponibilidad"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(tblProductos);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 1037, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPane.addTab("Productos", jPanel13);

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Ferreteria");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(tabPane)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPane))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        Categoria c = new Categoria();
        try {
            String nombreCat = nombreCategoria.getText();
            if (btnRegistrar.getText().equals("Registrar")) {
                int idcat = Integer.parseInt(idcategoria.getText());
                c.setCategoria(idcat);
                c.setNombre(nombreCat);
                c.setCantidadDisponible(0);
                boolean inserta = daoCategoria.insertar(c);
                if (inserta) {
                    JOptionPane.showMessageDialog(this, "Producto insertado correctamente:\n" + c.toString() + "\nSentencia: " + Comun.SENTENCIA_REALIZADA, "Sentencia OK", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error en la sentencia", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                boolean actualiza = daoCategoria.actualizar(c);
                if (actualiza) {
                    JOptionPane.showMessageDialog(this, "Producto actualizado correctamente:\n" + c.toString() + "\nSentencia: " + Comun.SENTENCIA_REALIZADA, "Sentencia OK", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error en la sentencia", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            limpiarCategoria();
            cargarTablaCategoria();
            btnRegistrar.setText("Registrar");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void categoriaTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_categoriaTblMouseClicked
        int seleccion = categoriaTbl.getSelectedRow();
        String id = String.valueOf(categoriaTbl.getValueAt(seleccion, 0));
        Categoria categoria = daoCategoria.seleccionar(Integer.parseInt(id));
        if (categoria != null) {
            idcategoria.setText(String.valueOf(categoria.getCategoria()));
            nombreCategoria.setText(categoria.getNombre());
        }
        btnRegistrar.setText("Actualizar");
    }//GEN-LAST:event_categoriaTblMouseClicked

    double total = 0;

    private void actualizarCantidadTotal() {
        cantidadProductos.setText(String.valueOf(productos.size()));
        productos.forEach((p) -> {
            total += p.getPrecioCompra();
        });
        totalProductos.setText("" + total);
    }

    private void limpiarCamposFactura() {
        codigoProducto.setText("");
        nombreProducto.setText("");
        precioVenta.setText("");
        precioCompra.setText("");
        cantidadDisponible.setText("");
        comboCategoria.setSelectedIndex(0);
    }

    private void btnRegistrarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarProveedorActionPerformed
        try {
            if (!codigoProveedor.getText().isEmpty() && !nombreProveedor.getText().isEmpty() && !ciudadProveedor.getText().isEmpty() && !telefonoProveedor.getText().isEmpty() && !contactoProveedor.getText().isEmpty()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setIdproveedor(codigoProveedor.getText());
                proveedor.setCiudad(ciudadProveedor.getText());
                proveedor.setNombreContacto(contactoProveedor.getText());
                proveedor.setTelefono(telefonoProveedor.getText());
                proveedor.setNombre(nombreProveedor.getText());
                if (btnRegistrarProveedor.getText().equals("Registrar")) {
                    boolean inserta = daoProveedor.insertar(proveedor);
                    if (inserta) {
                        JOptionPane.showMessageDialog(this, "Se registro el proveedor correctamente\nConsulta usada:\n" + Comun.SENTENCIA_REALIZADA, "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al registrar los datos.", "Error al registrar", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    boolean actualiza = daoProveedor.actualizar(proveedor);
                    if (actualiza) {
                        JOptionPane.showMessageDialog(this, "Se actualizo el proveedor correctamente\nConsulta usada:\n" + Comun.SENTENCIA_REALIZADA, "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al registrar los datos.", "Error al registrar", JOptionPane.WARNING_MESSAGE);
                    }
                }
                limpiarProveedor();
                cargarTablaProveedor();
            } else {
                JOptionPane.showMessageDialog(this, "Hay campos vacios, porfavor\ncomplete los campos.", "Error al registrar", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error al registrar", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistrarProveedorActionPerformed

    private void proveedoresTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proveedoresTblMouseClicked
        int seleccion = proveedoresTbl.getSelectedRow();
        String id = String.valueOf(proveedoresTbl.getValueAt(seleccion, 0));
        Proveedor proveedor = daoProveedor.seleccionar(id);
        if (proveedor != null) {
            codigoProveedor.setText(proveedor.getIdproveedor());
            ciudadProveedor.setText(proveedor.getCiudad());
            contactoProveedor.setText(proveedor.getNombreContacto());
            telefonoProveedor.setText(proveedor.getTelefono());
            nombreProveedor.setText(proveedor.getNombre());
        }
        btnRegistrarProveedor.setText("Actualizar");
    }//GEN-LAST:event_proveedoresTblMouseClicked

    private void jPanel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseClicked
        limpiarProveedor();
        btnRegistrarProveedor.setText("Registrar");
    }//GEN-LAST:event_jPanel9MouseClicked

    private void jPanel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseClicked
        limpiarCategoria();
        btnRegistrar.setText("Registrar");
    }//GEN-LAST:event_jPanel8MouseClicked

    private void btnRegistrarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarProductoActionPerformed
        try {
            Producto producto = new Producto();
            if (!facturaCompra.getText().isEmpty() && !codigoProducto.getText().isEmpty() && !nombreProducto.getText().isEmpty() && !precioVenta.getText().isEmpty() && !precioCompra.getText().isEmpty() && !cantidadDisponible.getText().isEmpty()) {
                producto.setIdproducto(codigoProducto.getText());
                producto.setNombre(nombreProducto.getText());
                producto.setDisponibilidad(Integer.parseInt(cantidadDisponible.getText()));
                producto.setPrecioCompra(Double.parseDouble(precioCompra.getText()));
                producto.setPrecioVenta(Double.parseDouble(precioVenta.getText()));
                if (comboCategoria.getSelectedIndex() > 0) {
                    String splCodigo = (String) comboCategoria.getSelectedItem();
                    String[] codigo = splCodigo.split("-");
                    producto.setCategoria(Integer.parseInt(codigo[0]));
                    facturaCompra.setEditable(false);
                    cantidadProductos.setEditable(false);
                    totalProductos.setEditable(false);
                    if (btnRegistrarProducto.getText().equals("Registrar")) {
                        if (!productoExiste(producto.getIdproducto())) {
                            productos.add(producto);
                        } else {
                            int resp = JOptionPane.showConfirmDialog(this, "El producto " + producto.getNombre() + " ya existe.\n¿Desea actualizarlo?", "Producto existente", JOptionPane.YES_NO_OPTION);
                            System.out.println(resp);
                            if (resp == 0) {
                                List<Producto> temp = new ArrayList<>(productos);
                                temp.forEach((p) -> {
                                    if (p.getIdproducto().equals(producto.getIdproducto())) {
                                        productos.remove(p);
                                        productos.add(producto);
                                    }
                                });

                            } else {

                            }
                        }
                        cargarTablaFactProductos();
                        actualizarCantidadTotal();
                        limpiarCamposFactura();
                    } else {
                        List<Producto> temp = new ArrayList<>(productos);
                        temp.forEach((p) -> {
                            if (p.getIdproducto().equals(producto.getIdproducto())) {
                                productos.remove(p);
                                productos.add(producto);
                                cargarTablaFactProductos();
                                actualizarCantidadTotal();
                                limpiarCamposFactura();
                                btnRegistrarProducto.setText("Registrar");
                            }
                        });
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Seleccione una categoria", "Categoria no valida", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Hay campos vacios en el registro.", "Error al registrar", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error al registrar", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistrarProductoActionPerformed

    private void btnFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalizarActionPerformed
        try {
            if (!facturaCompra.getText().isEmpty() && !cantidadProductos.getText().isEmpty() && !totalProductos.getText().isEmpty()) {
                if (comboProveedor.getSelectedIndex() > 0) {
                    factura = new Factura();
                    factura.setCantidadProductos(Integer.parseInt(cantidadProductos.getText()));
                    factura.setDetalleVenta(productos);
                    factura.setIdfactura(Integer.parseInt(facturaCompra.getText()));
                    factura.setNoventa(UUID.randomUUID().toString());
                    factura.setTotalVenta(total);
                    boolean inserta = daoFactura.insertar(factura);
                    if (inserta) {
                        productos.forEach((pd) -> {
                            boolean pdInserta = daoProducto.insertar(pd);
                            if (pdInserta) {
                                System.out.println("Producto insertado en la base de datos.");
                                System.out.println(Comun.SENTENCIA_REALIZADA);
                            } else {
                                System.out.println("Error al insertar");
                            }
                        });
                        productos.forEach((p) -> {
                            DetalleFactura df = new DetalleFactura(p.getIdproducto(), factura.getIdfactura());
                            boolean dfInserta = daoDetalleFactura.insertar(df);
                            if (dfInserta) {
                                System.out.println("Detalle insertado");
                                System.out.println(Comun.SENTENCIA_REALIZADA);
                            } else {
                                System.out.println("Error al insertar");
                            }
                        });
                        String prov_cod = (String) comboProveedor.getSelectedItem();
                        String[] codigo = prov_cod.split("-");
                        Factura_C_V f_compra = new Factura_C_V(factura.getIdfactura(), Integer.parseInt(codigo[0].trim()));
                        boolean fcInserta = daoFacturac_f.insertar(f_compra);
                        if (fcInserta) {
                            JOptionPane.showMessageDialog(this, "Factura registrada correctamente:\n" + factura.toString() + "\nSentencia: " + Comun.SENTENCIA_REALIZADA, "Sentencia OK", JOptionPane.INFORMATION_MESSAGE);
                            productos = new ArrayList<>();
                            facturaCompra.setText("");
                            cantidadProductos.setText("");
                            totalProductos.setText("");
                            comboProveedor.setSelectedIndex(0);
                            limpiarCamposFactura();
                        } else {
                            JOptionPane.showMessageDialog(this, "Error en la sentencia:\n" + Comun.SENTENCIA_REALIZADA, "Sentencia Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Error en la sentencia", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Seleccione un proveedor valido", "Seleccione un proveedor", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Hay campos vacios", "Campos vacios", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error al registrar", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnFinalizarActionPerformed

    private void productosFacTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productosFacTblMouseClicked
        int seleccion = productosFacTbl.getSelectedRow();
        String id = String.valueOf(productosFacTbl.getValueAt(seleccion, 0));
        Producto p = null;
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getIdproducto().equals(id)) {
                p = new Producto();
                p = productos.get(i);
            }
        }
        if (p != null) {
            codigoProducto.setText(p.getIdproducto());
            nombreProducto.setText(p.getNombre());
            precioVenta.setText(String.valueOf(p.getPrecioVenta()));
            precioCompra.setText(String.valueOf(p.getPrecioCompra()));
            cantidadDisponible.setText(String.valueOf(p.getDisponibilidad()));
            btnRegistrarProducto.setText("Actualizar");
        }
    }//GEN-LAST:event_productosFacTblMouseClicked

    private void jPanel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseClicked
        if (btnRegistrarProducto.getText().equals("Actualizar")) {
            limpiarCamposFactura();
            btnRegistrarProducto.setText("Registrar");
        }
    }//GEN-LAST:event_jPanel7MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        this.setEnabled(false);
        VistaAgregarCliente ac = new VistaAgregarCliente();
        ac.setInstance(this);
        ac.setLocationRelativeTo(null);
        ac.setResizable(false);
        ac.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        try {
            if (!clienteVentas.getText().isEmpty()) {
                int clienteId = Integer.parseInt(clienteVentas.getText());
                Cliente cl = daoCliente.seleccionar(clienteId);
                if (cl.getNombre() != null) {
                    Comun.current = cl;
                    asignarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "El cliente no existe", "Sin resultados", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "El campo esta vacio", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        try {
            String busqueda = productoBuscar.getText();
            if (!busqueda.isEmpty()) {
                Producto producto = daoProducto.seleccionar(Integer.parseInt(busqueda));
                if (producto.getNombre() != null) {
                    productoSeleccionado = producto;
                    nombreProductolbl.setText(String.format("Nombre: %s", producto.getNombre()));
                    precioProductoLbl.setText(String.format("Precio: $%s", producto.getPrecioVenta()));
                    disponibilidadLbl.setText(String.format("Disponibilidad: %s", producto.getDisponibilidad()));
                } else {
                    JOptionPane.showMessageDialog(this, "El producto no existe", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "El campo esta vacio", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (HeadlessException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void clienteVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clienteVentasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clienteVentasActionPerformed

    private boolean productoExiste(String idproducto) {
        boolean existe = false;
        for (Producto p : productos) {
            if (p.getIdproducto().equals(idproducto)) {
                existe = true;
            }
        }
        return existe;
    }

    void asignarDatos() {
        clienteVentas.setText(Comun.current.getNombre());
        clienteVentas.setEditable(false);
        btnBuscarCliente.setEnabled(false);
        facturaCarrito = new Factura();
    }

    private void actualizarProductos() {
        List<Producto> proAc = daoProducto.lista();
        List<Producto> proAcIns = new ArrayList<>();
        for (Producto p : proAc) {
            productosCarrito.stream().filter((actp) -> (p.getIdproducto().equals(actp.getIdproducto()))).map((actp) -> {
                Producto pdo = p;
                pdo.setDisponibilidad(pdo.getDisponibilidad() - actp.getDisponibilidad());
                return pdo;
            }).forEachOrdered((pdo) -> {
                proAcIns.add(pdo);
            });
        }
        proAcIns.forEach((p) -> {
            daoProducto.actualizar(p);
        });
    }

    private void limpiarCamposVenta() {
        productoBuscar.setText("");
        nombreProductolbl.setText(String.format("Nombre: %s", ""));
        precioProductoLbl.setText(String.format("Precio: $%s", 0));
        disponibilidadLbl.setText(String.format("Disponibilidad: %s", 0));
        clienteVentas.setEditable(true);
        clienteVentas.setText("");
        totalLbl.setText("");
        totalCantidad = 0;
        totalPrecio = 0;
        facturaCarrito = new Factura();
        productosCarrito = new ArrayList<>();

    }

    private int totalCantidad;
    private double totalPrecio;
    private void btnAgregarCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCarritoActionPerformed
        try {
            if (!cantidadAgregar.getText().isEmpty()) {
                int cantidad = Integer.parseInt(cantidadAgregar.getText());
                totalCantidad += cantidad;
                totalPrecio += productoSeleccionado.getPrecioVenta() * cantidad;
                productoSeleccionado.setDisponibilidad(cantidad);
                productosCarrito.add(productoSeleccionado);
                totalLbl.setText(String.format("$%s", totalPrecio));
                cargarACarrito();
                cantidadAgregar.setText("");
                nombreProductolbl.setText(String.format("Nombre: %s", ""));
                precioProductoLbl.setText(String.format("Precio: $%s", 0));
                disponibilidadLbl.setText(String.format("Disponibilidad: %s", 0));
            } else {
                JOptionPane.showMessageDialog(this, "Debe ingresar una cantidad", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAgregarCarritoActionPerformed

    private void btnFacturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturarActionPerformed
        try {
            if (!clienteVentas.getText().isEmpty()) {
                facturaCarrito = new Factura();
                facturaCarrito.setIdfactura((int) (Math.random() * 315654));
                facturaCarrito.setNoventa(UUID.randomUUID().toString());
                facturaCarrito.setCantidadProductos(totalCantidad);
                facturaCarrito.setTotalVenta(totalPrecio);
                facturaCarrito.setDetalleVenta(productosCarrito);
                boolean insertaFac = daoFactura.insertar(facturaCarrito);
                if (insertaFac) {
                    productosCarrito.forEach((p) -> {
                        DetalleFactura df = new DetalleFactura(p.getIdproducto(), facturaCarrito.getIdfactura());
                        boolean dfInserta = daoDetalleFactura.insertar(df);
                        if (dfInserta) {
                            System.out.println("Detalle agregado");
                        } else {
                            System.out.println("No se pudo agregar");
                        }
                    });
                    Factura_C_V cv = new Factura_C_V(facturaCarrito.getIdfactura(), Comun.current.getIdentificacion());
                    boolean fvInserta = daoComunImp.insertar(cv);
                    if (fvInserta) {
                        actualizarProductos();
                        limpiarCamposVenta();
                        JOptionPane.showMessageDialog(this, "Se realizo la venta exitosamente.\n" + "Sentencia: \n" + Comun.SENTENCIA_REALIZADA, "Venta exitosa", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo realizar la venta.\n" + Comun.SENTENCIA_REALIZADA, "Error", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo realizar la venta.\n" + Comun.SENTENCIA_REALIZADA, "Error", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe agregar un cliente para realizar la venta", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnFacturarActionPerformed

    private void limpiarProveedor() {
        codigoProveedor.setText("");
        ciudadProveedor.setText("");
        contactoProveedor.setText("");
        telefonoProveedor.setText("");
        nombreProveedor.setText("");
    }

    private void limpiarCategoria() {
        idcategoria.setText("");
        nombreCategoria.setText("");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VistaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new VistaPrincipal().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarCarrito;
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnFacturar;
    private javax.swing.JButton btnFinalizar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnRegistrarProducto;
    private javax.swing.JButton btnRegistrarProveedor;
    private javax.swing.JTextField buscarCompras;
    private javax.swing.JTextField buscarProducto;
    private javax.swing.JTextField buscarVentas;
    private javax.swing.JTextField cantidadAgregar;
    private javax.swing.JTextField cantidadDisponible;
    private javax.swing.JTextField cantidadProductos;
    private javax.swing.JTable categoriaTbl;
    private javax.swing.JTextField ciudadProveedor;
    private javax.swing.JTextField clienteVentas;
    private javax.swing.JTextField codigoProducto;
    private javax.swing.JTextField codigoProveedor;
    private javax.swing.JComboBox<String> comboCategoria;
    private javax.swing.JComboBox<String> comboProveedor;
    private javax.swing.JTextField contactoProveedor;
    private javax.swing.JLabel disponibilidadLbl;
    private javax.swing.JTextField facturaCompra;
    private javax.swing.JTextField idcategoria;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTextField nombreCategoria;
    private javax.swing.JTextField nombreProducto;
    private javax.swing.JLabel nombreProductolbl;
    private javax.swing.JTextField nombreProveedor;
    private javax.swing.JTextField precioCompra;
    private javax.swing.JLabel precioProductoLbl;
    private javax.swing.JTextField precioVenta;
    private javax.swing.JTextField productoBuscar;
    private javax.swing.JTable productosFacTbl;
    private javax.swing.JTable proveedoresTbl;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTable tblCarrito;
    private javax.swing.JTable tblCompras;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTable tblVentas;
    private javax.swing.JTextField telefonoProveedor;
    private javax.swing.JLabel totalComprasLbl;
    private javax.swing.JLabel totalLbl;
    private javax.swing.JTextField totalProductos;
    private javax.swing.JLabel totalVentas;
    // End of variables declaration//GEN-END:variables

}
