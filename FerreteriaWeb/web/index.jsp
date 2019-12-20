<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="dao.ProveedorImp" %>
<%@page import="dao.CategoriaImp" %>
<%@page import="modelos.Proveedor" %>
<%@page import="modelos.Categoria" %>
<%@page import="modelos.Producto" %>
<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Ferreteria</title>

        <!-- Bootstrap core CSS -->
        <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    </head>

    <body>
        <%
            ProveedorImp daoProveedorImp = new ProveedorImp();
            List<Proveedor> proveedors = daoProveedorImp.lista();
            CategoriaImp daCategoriaImp = new CategoriaImp();
            List<Categoria> categorias = daCategoriaImp.lista();
        %>
        <!-- Navigation -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark static-top">
            <div class="container">
                <a class="navbar-brand" href="#">Ferreteria</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarResponsive">
                    <ul class="navbar-nav ml-auto">
                        <li class="nav-item active">
                            <a class="nav-link" href="index.jsp">Compras
                                <span class="sr-only">(current)</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="ventas.do">Ventas</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="facturas.do">Facturas</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="proveedores.do">Proveedores</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="categorias.do">Categorias</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="productos.do">Productos</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Page Content -->
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="mt-5">Facturacion</h1>
                    <p class="lead">Compras a proveedores</p>
                    <form action="agregar.do" method="POST">
                        <div class="form-row">
                            <div class="form-group col-md-4">
                                <label for="no_fact">N° Factura</label>
                                <%
                                    String noFactura = (String) request.getSession().getAttribute("noFactura");
                                    String cProductos = (String) request.getSession().getAttribute("cProducto");
                                    String tCompra = (String) request.getSession().getAttribute("tCompra");
                                    if (noFactura != null) {
                                %>
                                <input type="number" class="form-control" id="no_fact" value="<%=noFactura%>" name="no_fact" required readonly>
                                <%
                                } else {
                                %>
                                <input type="number" class="form-control" id="no_fact" name="no_fact" required>
                                <%
                                    }
                                %>
                            </div>
                            <div class="form-group col-md-2">
                                <label for="cantidad">Cantidad de productos</label>
                                <%
                                    if (cProductos != null) {
                                %>
                                <input type="number" class="form-control" id="cantidad" value="<%=cProductos%>" name="cantidad" readonly>
                                <%
                                } else {
                                %>
                                <input type="number" class="form-control" id="cantidad" name="cantidad" disabled>
                                <%
                                    }
                                %>
                            </div>
                            <div class="form-group col-md-2">
                                <label for="c_total">Total compra</label>
                                <%
                                    if (tCompra != null) {
                                %>
                                <input type="number" class="form-control" id="c_total" value="<%=tCompra%>" name="c_total" readonly>
                                <%
                                } else {
                                %>
                                <input type="number" class="form-control" id="c_total" name="c_total" disabled>
                                <%
                                    }
                                %>
                            </div>
                            <div class="form-group col-md-4">
                                <label for="d_proveedor">Proveedor</label>
                                <select class="form-control" id="d_proveedor" name="d_proveedor" required>
                                    <%
                                        if (proveedors.size() > 0) {
                                            for (Proveedor p : proveedors) {
                                    %>
                                    <option value="<%=p.getIdproveedor()%>"><%=p.getNombre()%></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-2">
                                <label for="p_codigo">Codigo producto</label>
                                <input type="text" class="form-control" id="p_codigo" placeholder="0000000" name="p_codigo" required>
                            </div>
                            <div class="form-group col-md-4">
                                <label for="p_nombre">Nombre</label>
                                <input type="text" class="form-control" id="p_nombre" placeholder="Producto" name="p_nombre" required>
                            </div>
                            <div class="form-group col-md-2">
                                <label for="p_venta">Precio venta</label>
                                <input type="number" class="form-control" id="p_venta" placeholder="$0.0" name="p_venta" required>
                            </div>
                            <div class="form-group col-md-2">
                                <label for="p_compra">Precio compra</label>
                                <input type="number" class="form-control" id="p_compra" placeholder="$0.0" name="p_compra" required>
                            </div>
                            <div class="form-group col-md-2">
                                <label for="c_comprada">Cantidad</label>
                                <input type="number" class="form-control" id="c_comprada" placeholder="0" name="c_comprada" required>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-3">
                                <label for="p_categoria">Categoria</label>
                                <select class="form-control" id="p_categoria" name="p_categoria" required>
                                    <%
                                        if (categorias.size() > 0) {
                                            for (Categoria c : categorias) {
                                    %>
                                    <option value="<%=c.getCategoria()%>"><%=c.getNombre()%></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary">Registrar</button>
                    </form>
                    <div class="my-2"></div>
                </div>
                <div class="my-2"></div>
                <%
                    String alert = (String) request.getSession().getAttribute("alert");
                    if (alert != null) {
                        String[] alertSpplit = alert.split("_");
                %>
                <div class="alert <%=alertSpplit[1]%>" role="alert">
                    <%=alertSpplit[0]%>
                </div>
                <%
                    }
                %>
                <%
                    if (request.getSession().getAttribute("alertEnd") != null) {
                        String alertEnd = (String) request.getSession().getAttribute("alertEnd");
                        String alertSplt[] = alertEnd.split("_");
                %>
                <div class="alert <%=alertSplt[1]%>" role="alert">
                    <%=alertSplt[0]%>
                </div>
                <%
                    }
                %>
                <div class="col-lg-12">
                    <div class="my-2"></div>
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th scope="col">Codigo</th>
                                <th scope="col">Nombre</th>
                                <th scope="col">Precio</th>
                                <th scope="col">Disponibles</th>
                                <th scope="col">Categoria</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (request.getSession().getAttribute("productosAdd") != null) {
                                    List<Producto> productos = (List) request.getSession().getAttribute("productosAdd");
                                    if (productos.size() > 0) {
                                        for (Producto pd : productos) {
                            %>
                            <tr>
                                <th scope="row"><%=pd.getIdproducto()%></th>
                                <td><%=pd.getNombre()%></td>
                                <td><%=pd.getPrecioCompra()%></td>
                                <td><%=pd.getDisponibilidad()%></td>
                                <td><%=pd.getCategoria()%></td>
                            </tr>
                            <%
                                        }
                                    }
                                }
                            %>
                        </tbody>
                    </table>
                    <form action="registrar_compra.do">
                        <button type="submit" class="btn btn-success">Finalizar</button>
                    </form>
                    <div class="my-2"></div>
                </div>
            </div>
        </div>

        <!-- Bootstrap core JavaScript -->
        <script src="vendor/jquery/jquery.slim.min.js"></script>
        <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    </body>

</html>
