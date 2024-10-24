import java.awt.*;

import controlador.Controlador;
import org.junit.After;
import org.junit.Assert;
import util.Constantes;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

public class GuiTestEnabledDisabledRegistro {

    Robot robot;
    Controlador controlador;

    JTextField nombre_usuario;
    JTextField password;
    JTextField passwordConfirm;
    JTextField nombre;

    JButton paginaRegistrarse;
    JButton registrar;
    JButton cancelar;

    public GuiTestEnabledDisabledRegistro() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
        }
    }

    @Before
    public void setUp() {
        controlador = new Controlador();

        // Abro pagina registrarse
        paginaRegistrarse = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REGISTRAR);
        robot.delay(GuiTestUtils.getDelay());
        GuiTestUtils.clickComponente(paginaRegistrarse, robot);

        nombre_usuario = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REG_USSER_NAME);
        password = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REG_PASSWORD);
        passwordConfirm = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REG_CONFIRM_PASSWORD);
        nombre = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REG_REAL_NAME);

        registrar = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REG_BUTTON_REGISTRAR);
        cancelar = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REG_BUTTON_CANCELAR);

    }

    @Test
    public void testRegistroNada() {
        robot.delay(GuiTestUtils.getDelay());
        Assert.assertFalse("El boton de registrar deberia estar deshabilitado", registrar.isEnabled());
        Assert.assertTrue("El boton de cancelar deberia estar habilitado", cancelar.isEnabled());
    }

    @Test
    public void testSoloUsuario() {
        robot.delay(GuiTestUtils.getDelay());
        GuiTestUtils.clickComponente(nombre_usuario, robot);
        GuiTestUtils.tipeaTexto("franveron", robot);

        Assert.assertFalse("El boton de registrar deberia estar deshabilitado", registrar.isEnabled());
        Assert.assertTrue("El boton de cancelar deberia estar habilitado", cancelar.isEnabled());
    }

    @Test
    public void testSoloPass() {
        robot.delay(GuiTestUtils.getDelay());
        GuiTestUtils.clickComponente(password, robot);
        GuiTestUtils.tipeaTexto("franveron", robot);

        Assert.assertFalse("El boton de registrar deberia estar deshabilitado", registrar.isEnabled());
        Assert.assertTrue("El boton de cancelar deberia estar habilitado", cancelar.isEnabled());
    }

    @Test
    public void testSoloConfirmPass() {
        robot.delay(GuiTestUtils.getDelay());
        GuiTestUtils.clickComponente(passwordConfirm, robot);
        GuiTestUtils.tipeaTexto("franveron", robot);

        Assert.assertFalse("El boton de registrar deberia estar deshabilitado", registrar.isEnabled());
        Assert.assertTrue("El boton de cancelar deberia estar habilitado", cancelar.isEnabled());
    }

    @Test
    public void testSoloNombre() {
        robot.delay(GuiTestUtils.getDelay());
        GuiTestUtils.clickComponente(nombre, robot);
        GuiTestUtils.tipeaTexto("franveron", robot);

        Assert.assertFalse("El boton de registrar deberia estar deshabilitado", registrar.isEnabled());
        Assert.assertTrue("El boton de cancelar deberia estar habilitado", cancelar.isEnabled());
    }

    @Test
    public void testUsuarioTodo() {
        robot.delay(GuiTestUtils.getDelay());
        GuiTestUtils.clickComponente(nombre_usuario, robot);
        GuiTestUtils.tipeaTexto("franveron", robot);

        robot.delay(GuiTestUtils.getDelay());
        GuiTestUtils.clickComponente(password, robot);
        GuiTestUtils.tipeaTexto("mandarina123", robot);

        robot.delay(GuiTestUtils.getDelay());
        GuiTestUtils.clickComponente(passwordConfirm, robot);
        GuiTestUtils.tipeaTexto("mandarina123", robot);

        robot.delay(GuiTestUtils.getDelay());
        GuiTestUtils.clickComponente(nombre, robot);
        GuiTestUtils.tipeaTexto("Francisco Veron", robot);

        Assert.assertTrue("El boton de registrar deberia estar habilitado", registrar.isEnabled());
        Assert.assertTrue("El boton de cancelar deberia estar habilitado", cancelar.isEnabled());
    }

    @After
    public void tearDown() {
        JFrame ventana = (JFrame) controlador.getVista();
        ventana.setVisible(false);
    }

}
