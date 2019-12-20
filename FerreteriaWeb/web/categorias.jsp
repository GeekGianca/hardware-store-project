<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="modelos.Categoria" %>
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
                        <li class="nav-item active">
                            <a class="nav-link" href="categorias.do">Categorias
                                <span class="sr-only">(current)</span>
                            </a>
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
                    <h1 class="mt-5">Registro de categorias</h1>
                    <form action="registrar_categoria.do" method="POST">
                        <div class="form-row align-items-center">
                            <div class="col-auto">
                                <label class="sr-only" for="c_categoria">Codigo Categoria</label>
                                <input type="text" class="form-control mb-2" id="c_categoria" name="c_categoria" placeholder="Codigo categoria">
                            </div>
                            <div class="col-auto">
                                <label class="sr-only" for="n_categoria">Nombre categoria</label>
                                <div class="input-group mb-2">
                                    <input type="text" class="form-control" id="n_categoria" name="n_categoria" placeholder="Nombre categoria">
                                </div>
                            </div>
                            <div class="col-auto">
                                <button type="submit" class="btn btn-primary mb-2">Registrar</button>
                            </div>
                        </div>
                    </form>
                    <div class="my-2"></div>
                    <%
                    String alert = (String) request.getSession().getAttribute("alertCategoria");
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
                    <table class="table table-sm">
                        <thead>
                            <tr>
                                <th scope="col">Id Categoria</th>
                                <th scope="col" class="text-center">Nombre</th>
                                <th scope="col" class="text-center">Cantidad disp.</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (request.getSession().getAttribute("categorias") != null) {
                                    List<Categoria> categorias = (List) request.getSession().getAttribute("categorias");
                                    if (categorias.size() > 0) {
                                        for (Categoria ct : categorias) {
                            %>
                            <tr>
                                <td><%=ct.getCategoria()%></td>
                                <td class="text-center"><%=ct.getNombre()%></td>
                                <td class="text-center"><%=ct.getCantidadDisponible()%></td>
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
