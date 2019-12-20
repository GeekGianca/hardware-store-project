<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="dao.DaoComunImp" %>
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

    </head>

    <body>
        <%
            DecimalFormat df = new DecimalFormat("###,###.###");
        %>
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
                    <h1 class="mt-5">Registro de compras y ventas</h1>
                    <h3 class="mt-2">Registro de compras</h3>
                    <form action="buscar_compra.do" method="POST">
                        <div class="form-row align-items-center d-flex justify-content-between">
                            <div class="col-auto">
                                <label class="sr-only" for="b_compras">Buscar factura de compra</label>
                                <input type="text" class="form-control mb-2" id="b_compras" placeholder="Fecha factura o codigo" name="b_compras">
                            </div>
                            <div class="col-auto">
                                <button type="submit" class="btn btn-info btn-lg btn-sm mb-2">Buscar</button>
                            </div>
                        </div>
                    </form>
                    <div class="my-2"></div>
                    <table class="table table-sm">
                        <thead>
                            <tr>
                                <th scope="col">C.Factura</th>
                                <th scope="col" class="text-center">C.Proveedor</th>
                                <th scope="col" class="text-center">Cantidad Productos</th>
                                <th scope="col" class="text-center">Total compra</th>
                                <th scope="col" class="text-center">Fecha</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (request.getSession().getAttribute("compras") != null) {
                                    List<DaoComunImp.F_CompraNested> compras = (List) request.getSession().getAttribute("compras");
                                    if (compras.size() > 0) {
                                        for (DaoComunImp.F_CompraNested cf : compras) {
                                            double valorV = cf.getTotalCompra();
                                            String val = df.format(valorV);
                            %>
                            <tr>
                                <td><%=cf.getCodigoFactura()%></td>
                                <td class="text-center"><%=cf.getCodigoProveedor()%></td>
                                <td class="text-center"><%=cf.getCantidadProductos()%></td>
                                <td class="text-center"><%=val%></td>
                                <td class="text-center"><%=cf.getFecha()%></td>
                            </tr>
                            <%
                                        }
                                    }
                                }
                            %>
                        </tbody>
                    </table>
                    <div class="my-2"></div>
                    <%
                        if(request.getSession().getAttribute("totalCompras") != null){
                            String total = (String)request.getSession().getAttribute("totalCompras");
                            double valor = Double.parseDouble(total);
                            String totalV = df.format(valor);
                    %>
                    <p class="lead">Total compra: $<%=totalV%></p>
                    <%
                        }else{
                    %>
                    <p class="lead">Total compra: $0</p>
                    <%
                        }
                    %>
                    <div class="my-2"></div>
                    <h3 class="mt-2">Registro de ventas</h3>
                    <form action="buscar_venta.do" method="POST">
                        <div class="form-row align-items-center d-flex justify-content-between">
                            <div class="col-auto">
                                <label class="sr-only" for="b_ventas">Buscar factura de compra</label>
                                <input type="text" class="form-control mb-2" id="b_ventas" placeholder="Codigo factura o fecha" name="b_ventas">
                            </div>
                            <div class="col-auto">
                                <button type="submit" class="btn btn-info btn-lg btn-sm mb-2">Buscar</button>
                            </div>
                        </div>
                    </form>
                    <table class="table table-sm">
                        <thead>
                            <tr>
                                <th scope="col">C.Factura</th>
                                <th scope="col" class="text-center">C.Cliente</th>
                                <th scope="col" class="text-center">Cantidad Productos</th>
                                <th scope="col" class="text-center">Total venta</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (request.getSession().getAttribute("ventas") != null) {
                                    List<DaoComunImp.V_CompraNested> ventas = (List) request.getSession().getAttribute("ventas");
                                    if (ventas.size() > 0) {
                                        for (DaoComunImp.V_CompraNested vf : ventas) {
                                            double valorV = vf.getTotalVenta();
                                            String val = df.format(valorV);
                            %>
                            <tr>
                                <td><%=vf.getCodigoFactura()%></td>
                                <td class="text-center"><%=vf.getCodigoCliente()%></td>
                                <td class="text-center"><%=vf.getCantidadProductos()%></td>
                                <td class="text-center"><%=val%></td>
                            </tr>
                            <%
                                        }
                                    }
                                }
                            %>
                        </tbody>
                    </table>
                        <div class="my-2"></div>
                    <%
                        if(request.getSession().getAttribute("totalVentas") != null){
                            String total = (String)request.getSession().getAttribute("totalVentas");
                            double valor = Double.parseDouble(total);
                            String totalV = df.format(valor);
                    %>
                    <p class="lead">Total venta: $<%=totalV%></p>
                    <%
                        }else{
                    %>
                    <p class="lead">Total venta: $0</p>
                    <%
                        }
                    %>
                    <div class="my-2"></div>
                </div>
            </div>
        </div>

        <!-- Bootstrap core JavaScript -->
        <script src="vendor/jquery/jquery.slim.min.js"></script>
        <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    </body>

</html>
