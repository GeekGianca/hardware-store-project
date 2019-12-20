/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import dao.CategoriaImp;
import dao.ClienteImp;
import dao.DaoComunImp;
import dao.DetalleFacturaImp;
import dao.FacturaImp;
import dao.Factura_C_VImp;
import dao.ProductoImp;
import dao.ProveedorImp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class ControllerSvt extends HttpServlet {

    private CategoriaImp daoCategoria;
    private ProveedorImp daoProveedor;
    private ProductoImp daoProductoImp;
    private FacturaImp daoFacturaImp;
    private Factura_C_VImp daoC_VImp;
    private DaoComunImp daoComunImp;
    private ClienteImp daoClienteImp;
    private DetalleFacturaImp daoDetalleFacturaImp;
    private List<Producto> productosAdd = new ArrayList<>();
    private List<Producto> productosShow = new ArrayList<>();
    private Factura factura;
    private Factura_C_V factura_C_V;
    private double totalFactura = 0;
    private double totalVenta = 0;
    private int cantidadProductos = 0;
    private Producto productoVenta;
    private Cliente clienteVenta;
    private List<Producto> productosCarrito = new ArrayList<>();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getRequestURL().substring(request.getRequestURL().lastIndexOf("/") + 1);
        switch (accion) {
            case "agregar.do":
                System.out.println("Agregando a la compra---");
                agregarCompra(request, response);
                break;
            case "registrar_compra.do":
                registrarCompra(request, response);
                break;
            case "productos.do":
                productos(request, response);
                break;
            case "categorias.do":
                categorias(request, response);
                break;
            case "registrar_categoria.do":
                registrarCategoria(request, response);
                break;
            case "proveedores.do":
                proveedores(request, response);
                break;
            case "registrar_proveedor.do":
                registrarProveedor(request, response);
                break;
            case "facturas.do":
                facturas(request, response);
                break;
            case "buscar_compra.do":
                buscarCompra(request, response);
                break;
            case "buscar_venta.do":
                buscarVenta(request, response);
                break;
            case "ventas.do":
                ventas(request, response);
                break;
            case "buscar_producto.do":
                buscarProducto(request, response);
                break;
            case "agregar_carrito.do":
                agregarCarrito(request, response);
                break;
            case "buscar_cliente.do":
                buscarCliente(request, response);
                break;
            case "registrar_venta.do":
                registrarVenta(request, response);
                break;
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void agregarCompra(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (factura == null) {
                factura = new Factura();
                factura_C_V = new Factura_C_V();
                int cFactura = Integer.parseInt(request.getParameter("no_fact"));
                String noVenta = UUID.randomUUID().toString();
                int dProveedor = Integer.parseInt(request.getParameter("d_proveedor"));
                factura.setIdfactura(cFactura);
                factura.setNoventa(noVenta);
                factura_C_V.setId(dProveedor);
                factura_C_V.setIdFactura(cFactura);
            }
            Producto productoPut = new Producto();
            String pCodigo = request.getParameter("p_codigo");
            String pNombre = request.getParameter("p_nombre");
            double pVenta = Double.parseDouble(request.getParameter("p_venta"));
            double pCompra = Double.parseDouble(request.getParameter("p_compra"));
            int cComprada = Integer.parseInt(request.getParameter("c_comprada"));
            int pCategoria = Integer.parseInt(request.getParameter("p_categoria"));
            productoPut.setCategoria(pCategoria);
            productoPut.setDisponibilidad(cComprada);
            productoPut.setIdproducto(pCodigo);
            productoPut.setNombre(pNombre);
            productoPut.setPrecioCompra(pCompra);
            productoPut.setPrecioVenta(pVenta);
            System.out.println(productoPut.toString());
            double total = productoPut.getPrecioCompra() * productoPut.getDisponibilidad();
            totalFactura += total;
            productosAdd.add(productoPut);
            String alert = "Se agrego el producto a la factura del proveedor_alert-primary";
            request.getSession().setAttribute("noFactura", String.valueOf(factura.getIdfactura()));
            request.getSession().setAttribute("cProducto", String.valueOf(productosAdd.size()));
            request.getSession().setAttribute("tCompra", String.valueOf(totalFactura));
            request.getSession().setAttribute("alert", alert);
            request.getSession().setAttribute("productosAdd", productosAdd);
            response.sendRedirect("index.jsp");
        } catch (NumberFormatException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        } catch (IOException ex) {
            request.getSession().setAttribute("alert", "Error al agregar el producto a la factura del proveedor_alert-danger");
            Logger.getLogger(ControllerSvt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void registrarCompra(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getSession().removeAttribute("noFactura");
            request.getSession().removeAttribute("cProducto");
            request.getSession().removeAttribute("tCompra");
            request.getSession().removeAttribute("alert");
            request.getSession().removeAttribute("productosAdd");
            daoProductoImp = new ProductoImp();
            daoFacturaImp = new FacturaImp();
            daoDetalleFacturaImp = new DetalleFacturaImp();
            daoC_VImp = new Factura_C_VImp();
            boolean fInserta = daoFacturaImp.insertar(factura);
            if (fInserta) {
                productosAdd.forEach((p) -> {
                    boolean pInserta = daoProductoImp.insertar(p);
                    if (pInserta) {
                        System.out.println("Producto insertado");
                        System.out.println(Comun.SENTENCIA_REALIZADA);
                    } else {
                        System.out.println("Error al agregar producto.");
                    }
                });
                productosAdd.forEach((p) -> {
                    DetalleFactura df = new DetalleFactura(p.getIdproducto(), factura.getIdfactura());
                    boolean dfInserta = daoDetalleFacturaImp.insertar(df);
                    if (dfInserta) {
                        System.out.println("Detalle insertado");
                        System.out.println(Comun.SENTENCIA_REALIZADA);
                    } else {
                        System.out.println("Error al insertar");
                    }
                });
                boolean fcInserta = daoC_VImp.insertar(factura_C_V);
                if (fcInserta) {
                    productosAdd = new ArrayList<>();
                    request.getSession().setAttribute("alertEnd", "Compra a proveedor realizada correctamente!_alert-success");
                    response.sendRedirect("index.jsp");
                } else {
                    request.getSession().setAttribute("alertEnd", "Error al tratar de registrar la factura de compra a proveedor._alert-danger");
                }
            } else {
                request.getSession().setAttribute("alertEnd", "Error al tratar de registrar la factura de compra._alert-danger");
            }
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void productos(HttpServletRequest request, HttpServletResponse response) {
        try {
            daoProductoImp = new ProductoImp();
            productosShow = daoProductoImp.lista();
            request.getSession().setAttribute("productos", productosShow);
            if (productosShow.isEmpty()) {
                request.getSession().setAttribute("alertProductos", "No existen productos registrados._alert-danger");
            }
            response.sendRedirect("productos.jsp");
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void categorias(HttpServletRequest request, HttpServletResponse response) {
        try {
            daoCategoria = new CategoriaImp();
            List<Categoria> categorias = daoCategoria.lista();
            if (categorias.isEmpty()) {
                request.getSession().setAttribute("alertCategoria", "No existen categorias registradas._alert-danger");
            }
            request.getSession().setAttribute("categorias", categorias);
            response.sendRedirect("categorias.jsp");
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void registrarCategoria(HttpServletRequest request, HttpServletResponse response) {
        try {
            daoCategoria = new CategoriaImp();
            List<Categoria> categorias = null;
            int cCategoria = Integer.parseInt(request.getParameter("c_categoria"));
            String nCategoria = String.valueOf(request.getParameter("n_categoria"));
            Categoria categoria = new Categoria();
            categoria.setCategoria(cCategoria);
            categoria.setNombre(nCategoria);
            boolean cInsertar = daoCategoria.insertar(categoria);
            if (cInsertar) {
                request.getSession().setAttribute("alertCategoria", "La categoria se registro correctamente._alert-success");
                categorias = daoCategoria.lista();
                request.getSession().setAttribute("categorias", categorias);
            } else {
                request.getSession().setAttribute("alertCategoria", "Se presento un error al tratar de registrar la categoria._alert-success");
            }
            response.sendRedirect("categorias.jsp");
        } catch (IOException | NumberFormatException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            request.getSession().setAttribute("alertCategoria", "se produjo un error al registrar las categorias" + e.getMessage() + "._alert-success");
            try {
                response.sendRedirect("categorias.jsp");
            } catch (IOException ex) {
                Logger.getLogger(ControllerSvt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void proveedores(HttpServletRequest request, HttpServletResponse response) {
        try {
            daoProveedor = new ProveedorImp();
            List<Proveedor> proveedors = daoProveedor.lista();
            request.getSession().setAttribute("proveedores", proveedors);
            if (proveedors.isEmpty()) {
                request.getSession().setAttribute("alertProveedor", "No existen proveedores registrados._alert-danger");
            }
            response.sendRedirect("proveedores.jsp");
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void registrarProveedor(HttpServletRequest request, HttpServletResponse response) {
        try {
            daoProveedor = new ProveedorImp();
            List<Proveedor> proveedors = null;
            String cProveedor = String.valueOf(request.getParameter("c_proveedor"));
            String nProveedor = String.valueOf(request.getParameter("n_proveedor"));
            String ciProveedor = String.valueOf(request.getParameter("ci_proveedor"));
            String tProveedor = String.valueOf(request.getParameter("t_proveedor"));
            String ncProveedor = String.valueOf(request.getParameter("nc_proveedor"));
            Proveedor proveedor = new Proveedor();
            proveedor.setCiudad(ciProveedor);
            proveedor.setIdproveedor(cProveedor);
            proveedor.setNombre(nProveedor);
            proveedor.setNombreContacto(ncProveedor);
            proveedor.setTelefono(tProveedor);
            boolean pinserta = daoProveedor.insertar(proveedor);
            if (pinserta) {
                System.out.println(Comun.SENTENCIA_REALIZADA);
                proveedors = daoProveedor.lista();
                request.getSession().setAttribute("proveedores", proveedors);
                request.getSession().setAttribute("alertProveedor", "Se registro correctamente un proveedor._alert-success");
            } else {
                request.getSession().setAttribute("alertProveedor", "No se pudo registrar el proveedor._alert-danger");
            }
            response.sendRedirect("proveedores.jsp");
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void facturas(HttpServletRequest request, HttpServletResponse response) {
        try {
            daoComunImp = new DaoComunImp();
            double totalCompra = 0;
            double totalVentas = 0;
            List<DaoComunImp.F_CompraNested> compraNesteds = daoComunImp.listarCompras();
            List<DaoComunImp.V_CompraNested> ventasNesteds = daoComunImp.listarVentas();
            totalCompra = compraNesteds.stream().map((c) -> c.getTotalCompra()).reduce(totalCompra, (accumulator, _item) -> accumulator + _item);
            totalVentas = ventasNesteds.stream().map((v) -> v.getTotalVenta()).reduce(totalVentas, (accumulator, _item) -> accumulator + _item);
            request.getSession().setAttribute("compras", compraNesteds);
            request.getSession().setAttribute("totalCompras", String.valueOf(totalCompra));
            request.getSession().setAttribute("totalVentas", String.valueOf(totalVentas));
            request.getSession().setAttribute("ventas", ventasNesteds);
            response.sendRedirect("facturas.jsp");
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void buscarCompra(HttpServletRequest request, HttpServletResponse response) {
        try {
            String data = String.valueOf(request.getParameter("b_compras"));
            daoComunImp = new DaoComunImp();
            List<DaoComunImp.F_CompraNested> compraNesteds = daoComunImp.buscar(data);
            double totalCompra = 0;
            double totalVentas = 0;
            List<DaoComunImp.V_CompraNested> ventasNesteds = daoComunImp.listarVentas();
            totalCompra = compraNesteds.stream().map((c) -> c.getTotalCompra()).reduce(totalCompra, (accumulator, _item) -> accumulator + _item);
            totalVentas = ventasNesteds.stream().map((v) -> v.getTotalVenta()).reduce(totalVentas, (accumulator, _item) -> accumulator + _item);
            request.getSession().setAttribute("compras", compraNesteds);
            request.getSession().setAttribute("totalCompras", String.valueOf(totalCompra));
            request.getSession().setAttribute("totalVentas", String.valueOf(totalVentas));
            request.getSession().setAttribute("ventas", ventasNesteds);
            response.sendRedirect("facturas.jsp");
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void buscarVenta(HttpServletRequest request, HttpServletResponse response) {
        try {
            String data = String.valueOf(request.getParameter("b_ventas"));
            daoComunImp = new DaoComunImp();
            List<DaoComunImp.V_CompraNested> ventasNested = daoComunImp.buscarVentas(data);
            List<DaoComunImp.F_CompraNested> compraNesteds = daoComunImp.listarCompras();
            double totalCompra = 0;
            double totalVentas = 0;
            List<DaoComunImp.V_CompraNested> ventasNesteds = daoComunImp.listarVentas();
            totalCompra = compraNesteds.stream().map((c) -> c.getTotalCompra()).reduce(totalCompra, (accumulator, _item) -> accumulator + _item);
            totalVentas = ventasNesteds.stream().map((v) -> v.getTotalVenta()).reduce(totalVentas, (accumulator, _item) -> accumulator + _item);
            request.getSession().setAttribute("compras", compraNesteds);
            request.getSession().setAttribute("totalCompras", String.valueOf(totalCompra));
            request.getSession().setAttribute("totalVentas", String.valueOf(totalVentas));
            request.getSession().setAttribute("ventas", ventasNested);
            response.sendRedirect("facturas.jsp");
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void ventas(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect("ventas.jsp");
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void buscarProducto(HttpServletRequest request, HttpServletResponse response) {
        try {
            daoProductoImp = new ProductoImp();
            Pattern regex = Pattern.compile("[a-zA-Z]");
            String codigoOrNombre = String.valueOf(request.getParameter("b_producto"));
            Matcher matcher = regex.matcher(codigoOrNombre);
            if (matcher.find()) {
                productoVenta = daoProductoImp.seleccionar(codigoOrNombre);
            } else {
                productoVenta = daoProductoImp.seleccionar(Integer.parseInt(codigoOrNombre));
            }
            System.out.println(codigoOrNombre);
            if (productoVenta.getNombre() != null) {
                request.getSession().setAttribute("no_existe", "is-valid");
                request.getSession().setAttribute("producto", productoVenta);
            } else {
                request.getSession().setAttribute("no_existe", "is-invalid");
            }
            response.sendRedirect("ventas.jsp");
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void agregarCarrito(HttpServletRequest request, HttpServletResponse response) {
        try {
            int cantidad = Integer.parseInt(request.getParameter("c_comprar"));
            productoVenta.setDisponibilidad(cantidad);
            double estaVenta = productoVenta.getPrecioVenta() * cantidad;
            totalVenta += estaVenta;
            cantidadProductos += cantidad;
            productoVenta.setPrecioVenta(estaVenta);
            productosCarrito.add(productoVenta);
            request.getSession().setAttribute("productosCarrito", productosCarrito);
            request.getSession().setAttribute("totalVenta", String.valueOf(totalVenta));
            request.getSession().removeAttribute("producto");
            response.sendRedirect("ventas.jsp");
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void buscarCliente(HttpServletRequest request, HttpServletResponse response) {
        try {
            daoClienteImp = new ClienteImp();
            int idCliente = Integer.parseInt(request.getParameter("b_cliente"));
            clienteVenta = daoClienteImp.seleccionar(idCliente);
            request.getSession().setAttribute("productosCarrito", productosCarrito);
            request.getSession().setAttribute("cliente", clienteVenta);
            request.getSession().setAttribute("totalVenta", String.valueOf(totalVenta));
            response.sendRedirect("ventas.jsp");
        } catch (IOException | NumberFormatException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void registrarVenta(HttpServletRequest request, HttpServletResponse response) {
        try {
            daoFacturaImp = new FacturaImp();
            daoDetalleFacturaImp = new DetalleFacturaImp();
            daoComunImp = new DaoComunImp();
            factura = new Factura();
            factura.setIdfactura((int) (Math.random() * 315654));
            factura.setNoventa(UUID.randomUUID().toString());
            factura.setCantidadProductos(cantidadProductos);
            factura.setTotalVenta(totalVenta);
            factura.setDetalleVenta(productosCarrito);
            boolean fInserta = daoFacturaImp.insertar(factura);
            if (fInserta) {
                productosCarrito.forEach((p) -> {
                    DetalleFactura df = new DetalleFactura(p.getIdproducto(), factura.getIdfactura());
                    boolean dfInserta = daoDetalleFacturaImp.insertar(df);
                    if (dfInserta) {
                        System.out.println("Detalle agregado");
                    } else {
                        System.out.println("No se pudo agregar");
                    }
                });
                Factura_C_V cv = new Factura_C_V(factura.getIdfactura(), clienteVenta.getIdentificacion());
                boolean fvInserta = daoComunImp.insertar(cv);
                if (fvInserta) {
                    System.out.println("Se registro la venta");
                    request.getSession().setAttribute("alertVenta", "Se registro la venta correctamente._alert-success");
                    request.getSession().removeAttribute("productosCarrito");
                    request.getSession().removeAttribute("cliente");
                    request.getSession().removeAttribute("totalVenta");
                } else {
                    System.out.println("Error al registrar la venta");
                    request.getSession().setAttribute("alertVenta", "Error al intentar registrar la venta._alert-danger");
                }
            } else {
                request.getSession().setAttribute("alertVenta", "Se presento un error al intentar facturar la compra._alert-danger");
            }
            response.sendRedirect("ventas.jsp");
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
}
