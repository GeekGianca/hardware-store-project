<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="modelos.Cliente" %>
<%@page import="modelos.Producto" %>
<%@page import="java.text.DecimalFormat" %>
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
        <link href="vendor/css/font-awesome.min.css" rel="stylesheet">

    </head>

    <body>
        <%
            DecimalFormat df = new DecimalFormat("###,###.###");
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
                        <li class="nav-item">
                            <a class="nav-link" href="index.jsp">Compras</a>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link" href="ventas.do">Ventas
                                <span class="sr-only">(current)</span>
                            </a>
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
                    <h1 class="mt-5">Ventas</h1>
                    <form action="buscar_producto.do" method="POST">
                        <div class="form-row align-items-center d-flex justify-content-between">
                            <div class="col-md-4">
                                <label class="sr-only" for="b_producto">Buscar producto</label>
                                <%
                                    String validacion = null;
                                    if(request.getSession().getAttribute("no_existe") != null){
                                        validacion = (String) request.getSession().getAttribute("no_existe");
                                %>
                                <input type="text" class="form-control mb-2 <%=validacion%>" id="b_producto" placeholder="Codigo del producto" name="b_producto" required>
                                <%
                                    if(validacion.equals("is-invalid")){
                                %>
                                <div class="invalid-feedback">
                                    El producto no existe.
                                </div>
                                <%
                                    }
                                %>
                                <%
                                    }else{
                                %>
                                <input type="text" class="form-control mb-2" id="b_producto" placeholder="Codigo del producto" name="b_producto" required>
                                <%
                                    }
                                %>
                            </div>
                            <div class="col-auto">
                                <button type="submit" class="btn btn-info mb-2">Buscar producto</button>
                            </div>
                        </div>
                    </form>
                    <form action="agregar_carrito.do" method="POST">
                        <%
                            Producto producto = (Producto) request.getSession().getAttribute("producto");
                            if (producto != null){
                        %>
                        <div class="form-row d-flex justify-content-between">
                            <div class="form-group col-md-4">
                                <label for="n_producto">Producto</label>
                                <input type="number" class="form-control" placeholder="<%=producto.getNombre()%>" id="n_producto" name="n_producto" readonly>
                            </div>
                            <div class="form-group col-md-2">
                                <label for="p_producto">Precio</label>
                                <input type="number" class="form-control" placeholder="<%=producto.getPrecioVenta()%>" id="p_producto" name="p_producto" readonly>

                            </div>
                            <div class="form-group col-md-2">
                                <label for="c_disponible">Disponibles</label>
                                <input type="number" class="form-control" placeholder="<%=producto.getDisponibilidad()%>" id="c_disponible" name="c_disponible" readonly>
                            </div>
                            <div class="form-group col-md-2">
                                <label for="c_comprar">Cantidad a agregar</label>
                                <input type="text" class="form-control"  id="c_comprar" name="c_comprar" required>
                            </div>
                        </div>
                        <div class="d-flex justify-content-end">
                            <button type="submit" class="btn btn-primary align-content-end">
                                <i class="fa fa-cart-plus" aria-hidden="true"></i> Agregar producto
                            </button>
                        </div>
                        <%
                            }
                        %>
                    </form>
                    <div class="my-2"></div>
                </div>
                <div class="col-lg-12 d-flex justify-content-between">
                    <div class="my-2"></div>
                    <button type="button" data-toggle="modal" data-target="#exampleModalCenter" class="btn btn-outline-success align-content-end">
                        Buscar cliente
                    </button>
                    <div class="my-2"></div>
                    <!--<button type="submit" class="btn btn-outline-danger align-content-end">
                        Eliminar cliente
                    </button>
                    <div class="my-2"></div>
                    <button type="submit" class="btn btn-outline-info align-content-end">
                        Agregar cliente
                    </button>-->
                </div>
                <div class="col-lg-12">
                    <div class="my-2"></div>
                    <%
                        String alert = (String) request.getSession().getAttribute("alertVenta");
                        if (alert != null) {
                            String[] alertSpplit = alert.split("_");
                    %>
                    <div class="alert <%=alertSpplit[1]%>" role="alert">
                        <%=alertSpplit[0]%>
                    </div>
                    <%
                        }
                    %>
                    <div class="my-2"></div>
                    <table class="table table-striped">
                        <thead>
                            <tr class="text-center">
                                <th scope="col">Codigo</th>
                                <th scope="col">Nombre</th>
                                <th scope="col">Precio</th>
                                <th scope="col">Cantidad</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if(request.getSession().getAttribute("productosCarrito") != null){
                                    List<Producto> productos = (List) request.getSession().getAttribute("productosCarrito");
                                    for(Producto p : productos){
                            %>
                            <tr class="text-center">
                                <th scope="row"><%=p.getIdproducto()%></th>
                                <td><%=p.getNombre()%></td>
                                <td><%=p.getPrecioVenta()%></td>
                                <td><%=p.getIdproducto()%></td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                        </tbody>
                    </table>
                    <div class="my-2"></div>
                    <div class="d-flex justify-content-between">
                        <form action="registrar_venta.do">
                            <div class="form-group">
                                <%
                                    if(request.getSession().getAttribute("cliente") != null){
                                        Cliente cli = (Cliente)request.getSession().getAttribute("cliente");
                                %>
                                <input class="form-control" type="text" placeholder="<%=cli.getNombre()%>" required readonly>
                                <%
                                    }else{
                                %>
                                <input class="form-control" type="text" placeholder="Cliente" required>
                                <%
                                    }
                                %>
                            </div>
                            <button type="submit" class="btn btn-success">Facturar venta</button>
                        </form>
                        <%
                            if(request.getSession().getAttribute("totalVenta") != null){
                                String totalUc = (String)request.getSession().getAttribute("totalVenta");
                                double val = Double.parseDouble(totalUc);
                                String total = df.format(val);
                        %>
                        <h3 class="mt-2">Total venta: $<%=total%></h3>
                        <%
                            }else{
                        %>
                        <h3 class="mt-2">Total venta: $0</h3>
                        <%
                            }
                        %>
                    </div>
                    <div class="my-2"></div>
                    <div class="my-2"></div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalCenterTitle">Buscar cliente</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <form action="buscar_cliente.do" method="POST">
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="b_cliente">Identificacion</label>
                                <input type="number" class="form-control" id="b_cliente" name="b_cliente" aria-describedby="b_cliente">
                                <small id="b_cliente" class="form-text text-muted">Digite la identificacion del usuario, si no existe puede agregarlo..</small>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">Buscar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Bootstrap core JavaScript -->
        <script src="vendor/jquery/jquery.slim.min.js"></script>
        <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    </body>

</html>

