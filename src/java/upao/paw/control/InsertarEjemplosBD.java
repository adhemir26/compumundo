package upao.paw.control;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import upao.paw.compumundo.BD;
import upao.paw.compumundo.modelo.*;

/**
 * Crea un ejemplo en el que un cliente compra dos computadoras del mismo
 * fabricante, pero con diferentes configuraciones de pantalla
 *
 * @author jahd
 */
@WebServlet(name = "InsertarEjemplosBD", urlPatterns = {"/servlet/InsertarEjemplosBD"})
public class InsertarEjemplosBD extends HttpServlet {

    private static final String REDIRECCION = "/cm/admin/baseDeDatos.jsp";

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BD bd;
        try {
            bd = BD.getInstance();
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo conectar a la base de datos&error=" + ex.getMessage());
            return;
        }

        TipoPersonalizacion tpPantalla = new TipoPersonalizacion();
        tpPantalla.setNombre("Pantalla");
        try {
            bd.getTipoPersonalizacionDao().create(tpPantalla);
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo crear ejemplo en tabla Tipo Personalizacion&error=" + ex.getMessage());
            return;
        }

        Personalizacion per15 = new Personalizacion();
        per15.setTipoPersonalizacion(tpPantalla);
        per15.setNombre("15 pulgadas");
        per15.setPrecio(0);
        try {
            bd.getPersonalizacionDao().create(per15);
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo crear ejemplo en tabla Personalizacion&error=" + ex.getMessage());
            return;
        }

        Personalizacion per22 = new Personalizacion();
        per22.setTipoPersonalizacion(tpPantalla);
        per22.setNombre("22 pulgadas");
        per22.setPrecio(50);
        try {
            bd.getPersonalizacionDao().create(per22);
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo crear ejemplo en tabla Personalizacion&error=" + ex.getMessage());
            return;
        }

        Producto proComputadora = new Producto();
        proComputadora.setDescripcion("Computadora HP");
        proComputadora.setPrecio_base(890);
        try {
            bd.getProductoDao().create(proComputadora);
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo crear ejemplo en tabla Producto&error=" + ex.getMessage());
            return;
        }

        ConfiguracionInicial ciComputadora = new ConfiguracionInicial();
        ciComputadora.setProducto(proComputadora);
        ciComputadora.setPersonalizacion(per15);
        try {
            bd.getConfiguracionInicialDao().create(ciComputadora);
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo crear ejemplo en tabla ConfiguracionInicial&error=" + ex.getMessage());
            return;
        }

        Pedido pedido = new Pedido();
        try {
            bd.getPedidoDao().create(pedido);
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo crear ejemplo en tabla Pedido&error=" + ex.getMessage());
            return;
        }

        LineaPedido lp1 = new LineaPedido();
        lp1.setPedido(pedido);
        lp1.setProducto(proComputadora);
        try {
            bd.getLineaPedidoDao().create(lp1);
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo crear ejemplo en tabla LineaPedido&error=" + ex.getMessage());
            return;
        }

        Configuracion c1 = new Configuracion();
        c1.setLineaPedido(lp1);
        Producto pLp1 = lp1.getProducto();
        try {
            c1.setPersonalizacion(getPersonalizacionInicial(pLp1, bd));
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo consultar sobre ConfiguracionInicial de Producto&error=" + ex.getMessage());
            return;
        } catch (IndexOutOfBoundsException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo refrescar Producto de LineaPedido&error=" + ex.getMessage());
            return;
        }
        try {
            bd.getConfiguracionDao().create(c1);
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo crear ejemplo en tabla Configuracion (1)&error=" + ex.getMessage());
            return;
        }


        LineaPedido lp2 = new LineaPedido();
        lp2.setPedido(pedido);
        lp2.setProducto(proComputadora);
        try {
            bd.getLineaPedidoDao().create(lp2);
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo crear ejemplo en tabla LineaPedido&error=" + ex.getMessage());
            return;
        }

        Configuracion c2 = new Configuracion();
        c2.setLineaPedido(lp2);
        Producto pLp2 = lp2.getProducto();
        try {
            c2.setPersonalizacion(getPersonalizacionInicial(pLp2, bd));
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo consultar sobre ConfiguracionInicial de Producto&error=" + ex.getMessage());
            return;
        } catch (IndexOutOfBoundsException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo refrescar Producto de LineaPedido&error=" + ex.getMessage());
            return;
        }
        try {
            bd.getConfiguracionDao().create(c2);
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo crear ejemplo en tabla Configuracion (2)&error=" + ex.getMessage());
            return;
        }

        c2.setPersonalizacion(per22);
        try {
            bd.getConfiguracionDao().update(c2);
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo crear ejemplo en tabla Configuracion&error=" + ex.getMessage());
            return;
        }

        Comprador comprador = new Comprador();
        comprador.setNombre("John Perez");
        try {
            bd.getCompradorDao().create(comprador);
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo crear ejemplo en tabla Comprador&error=" + ex.getMessage());
            return;
        }

        pedido.setComprador(comprador);
        pedido.setEstado(Pedido.ESTADO_PEDIDO);
        try {
            bd.getPedidoDao().update(pedido);
        } catch (SQLException ex) {
            response.sendRedirect(REDIRECCION
                    + "?mensaje=No se pudo crear ejemplo en tabla Pedido&error=" + ex.getMessage());
            return;
        }

        response.sendRedirect(REDIRECCION
                + "?mensaje=Datos de ejemplo insertados con exito");
    }

    private Personalizacion getPersonalizacionInicial(Producto p, BD bd) throws SQLException, IndexOutOfBoundsException {
        bd.getProductoDao().refresh(p);
        ConfiguracionInicial ciPLp1;
        ciPLp1 = bd.getConfiguracionInicialDao().queryForEq("producto_id", p).get(0);
        return ciPLp1.getPersonalizacion();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
}
