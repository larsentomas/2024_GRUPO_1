package vista;

import controlador.Controlador;
import excepciones.ChoferRepetidoException;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

import javax.swing.*;
import java.awt.*;

public class GuiTestAdminGeneral {

    Robot robot;
    Controlador controlador;
    Empresa e;

    JTextField totalSueldos;

    JButton cerrarSesion;

    FalsoOptionPane panel = new FalsoOptionPane();

    @Before
    public void setUp() {
        try {
            e = Empresa.getInstance();

            robot = new Robot();
            controlador = new Controlador();
            controlador.getVista().setOptionPane(panel);

            // Login admin
            JTextField nombre_usuario = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
            JTextField password = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PASSWORD);
            JButton login = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LOGIN);

            GuiTestUtils.cargarJTextField(nombre_usuario, "admin", robot);
            GuiTestUtils.cargarJTextField(password, "admin", robot);
            GuiTestUtils.clickComponente(login, robot);
            robot.delay(GuiTestUtils.getDelay());

            robot.delay(GuiTestUtils.getDelay());
            totalSueldos = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.TOTAL_SUELDOS_A_PAGAR);
            cerrarSesion = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CERRAR_SESION_ADMIN);

            controlador.getVista().actualizar();
        } catch (Exception e) {}
    }

    @Test
    public void testCerrarSesion() {
        JPanel panelAdmin = (JPanel) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PANEL_ADMINISTRADOR);
        Assert.assertEquals("El admin deberia estar logueado", e.getUsuarioLogeado().getNombreUsuario(), "admin");
        GuiTestUtils.clickComponente(cerrarSesion, robot);
        Assert.assertNull("El cliente deberia estar deslogueado", e.getUsuarioLogeado());
        robot.delay(GuiTestUtils.getDelay());

        try {
            JPanel paginaLogin = (JPanel) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PANEL_LOGIN);

            Assert.assertTrue("El panel de login deberia estar visible", paginaLogin.isShowing());
            Assert.assertFalse("El panel de admin no deberia estar visible", panelAdmin.isShowing());
        } catch (NullPointerException e) {
            Assert.fail("No se encontro el panel de login");
        }
    }

    @Test
    public void testSueldosTotales() {
        try {
            e.agregarChofer(new ChoferPermanente("12345678", "Pepe", 2000, 2));
            e.agregarChofer(new ChoferTemporario("87654321", "Juan"));

            controlador.getVista().actualizar();
            String sueldosRondeado = String.format("%.2f", e.getTotalSalarios());

            Assert.assertEquals("El total de sueldos a pagar es incorrecto", sueldosRondeado, totalSueldos.getText());
        } catch(ChoferRepetidoException e) {}
    }

}
