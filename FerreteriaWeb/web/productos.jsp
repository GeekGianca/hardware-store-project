<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
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
        <!-- Navigation -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark static-top">
            <div class="container">
                <a class="navbar-brand" href="index.jsp">Ferreteria</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarResponsive">
                    <ul class="navbar-nav ml-auto">
                        <li class="nav-item">
                            <a class="nav-link" href="index.jsp">Compras</a>
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
                        <li class="nav-item active">
                            <a class="nav-link" href="productos.do">Productos
                                <span class="sr-only">(current)</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Page Content -->
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="mt-5">Productos disponibles</h1>
                    <p class="lead">Los productos son registrados desde una factura de compra.</p>
                    <div class="my-2"></div>
                    <%
                    String alert = (String) request.getSession().getAttribute("alertProductos");
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
                    <table class="table table-hover">
                        <thead>
                            <tr class="text-center">
                                <th scope="col">Codigo</th>
                                <th scope="col">Nombre</th>
                                <th scope="col">Precio</th>
                                <th scope="col">Disponibles</th>
                                <th scope="col">Categoria</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (request.getSession().getAttribute("productos") != null) {
                                    List<Producto> productos = (List) request.getSession().getAttribute("productos");
                                    if (productos.size() > 0) {
                                        for (Producto pd : productos) {
                            %>
                            <tr class="text-center">
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
                </div>
            </div>
        </div>

        <!-- Bootstrap core JavaScript -->
        <script src="vendor/jquery/jquery.slim.min.js"></script>
        <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    </body>

</html>
