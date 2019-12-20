<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="modelos.Proveedor" %>
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
                        <li class="nav-item active">
                            <a class="nav-link" href="proveedores.do">Proveedores
                                <span class="sr-only">(current)</span>
                            </a>
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
                    <h1 class="mt-5">Proveedores</h1>
                    <p class="lead">Registro de proveedores de compra..</p>

                    <form action="registrar_proveedor.do" method="POST">
                        <div class="form-row">
                            <div class="form-group col-md-4">
                                <label for="c_proveedor">Codigo proveedor</label>
                                <input type="text" class="form-control" id="c_proveedor" name="c_proveedor" required>
                            </div>
                            <div class="form-group col-md-4">
                                <label for="n_proveedor">Nombre</label>
                                <input type="text" class="form-control" id="n_proveedor" name="n_proveedor" required>
                            </div>
                            <div class="form-group col-md-4">
                                <label for="ci_proveedor">Ciudad</label>
                                <input type="text" class="form-control" id="ci_proveedor" name="ci_proveedor" required>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-4">
                                <label for="t_proveedor">Telefono</label>
                                <input type="number" maxlength="10" minlength="10" class="form-control" id="t_proveedor" name="t_proveedor" required>
                            </div>
                            <div class="form-group col-md-4">
                                <label for="nc_proveedor">Nombre de Contacto</label>
                                <input type="text" class="form-control" id="nc_proveedor" name="nc_proveedor" required>
                            </div>
                        </div>
                        <button type="submit" id="btn" class="btn btn-primary">Registrar</button>
                    </form>

                    <div class="my-2"></div>
                    <%
                    String alert = (String) request.getSession().getAttribute("alertProveedor");
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
                                <th scope="col">Ciudad</th>
                                <th scope="col">Telefono</th>
                                <th scope="col">Contacto</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (request.getSession().getAttribute("proveedores") != null) {
                                    List<Proveedor> proveedores = (List) request.getSession().getAttribute("proveedores");
                                    if (proveedores.size() > 0) {
                                        for (Proveedor p : proveedores) {
                            %>
                            <tr class="text-center">
                                <th scope="row"><%=p.getIdproveedor()%></th>
                                <td><%=p.getNombre()%></td>
                                <td><%=p.getCiudad()%></td>
                                <td><%=p.getTelefono()%></td>
                                <td><%=p.getNombreContacto()%></td>
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
