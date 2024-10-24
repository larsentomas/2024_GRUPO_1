import java.awt.*;

import controlador.Controlador;
import org.junit.After;
import org.junit.Assert;
import util.Constantes;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

public class GuiTestEnabledDisabledLogIn {

    Robot robot;
    Controlador controlador;
    JTextField nombre_usuario;
    JTextField password;
    JButton login;
    JButton registrar;

    public GuiTestEnabledDisabledLogIn () {
        try {
            robot = new Robot();
        } catch (AWTException e) {
        }
    }

    @Before
    public void setUp() {
        controlador = new Controlador();

        nombre_usuario = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
        password = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PASSWORD);
        login = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LOGIN);
        registrar = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REGISTRAR);

        Assert.assertTrue("El campo nombre de usuario deberia estar vacio", nombre_usuario.getText().isEmpty());
        Assert.assertTrue("El campo password deberia estar vacio", password.getText().isEmpty());
    }

    @Test
    public void testLogNada() {
        robot.delay(GuiTestUtils.getDelay());
        Assert.assertFalse("El boton de log in deberia estar deshabilitado", login.isEnabled());
        Assert.assertTrue("El boton de registrarse deberia estar habilitado", registrar.isEnabled());
    }

    @Test
    public void testLogSoloNombre() {
        robot.delay(GuiTestUtils.getDelay());
        GuiTestUtils.clickComponente(nombre_usuario, robot);
        GuiTestUtils.tipeaTexto("franveron", robot);

        Assert.assertFalse("El boton de log in deberia estar deshabilitado", login.isEnabled());
        Assert.assertTrue("El boton de registrarse deberia estar habilitado", registrar.isEnabled());
    }

    @Test
    public void testLogSoloPass() {
        robot.delay(GuiTestUtils.getDelay());
        GuiTestUtils.clickComponente(password, robot);
        GuiTestUtils.tipeaTexto("franveron", robot);

        Assert.assertFalse("El boton de log in deberia estar deshabilitado", login.isEnabled());
        Assert.assertTrue("El boton de registrarse deberia estar habilitado", registrar.isEnabled());
    }

    @Test
    public void testLogCorrecto() {
        robot.delay(GuiTestUtils.getDelay());

        GuiTestUtils.clickComponente(nombre_usuario, robot);
        GuiTestUtils.tipeaTexto("franveron", robot);

        GuiTestUtils.clickComponente(password, robot);
        GuiTestUtils.tipeaTexto("mandarina123", robot);

        Assert.assertTrue("El boton de log in deberia estar habilitado", login.isEnabled());
        Assert.assertTrue("El boton de registrarse deberia estar habilitado", registrar.isEnabled());
    }

    @After
    public void tearDown() {
        JFrame vista = (JFrame) controlador.getVista();
        vista.setVisible(false);
    }

}
