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

    @Before
    public void setUp() {
        try {
            robot = new Robot();
            controlador = new Controlador();

            nombre_usuario = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
            password = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PASSWORD);
            login = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LOGIN);
            registrar = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REGISTRAR);

            Assert.assertTrue("El campo nombre de usuario deberia estar vacio", nombre_usuario.getText().isEmpty());
            Assert.assertTrue("El campo password deberia estar vacio", password.getText().isEmpty());

        } catch (AWTException e) {}
    }

    @Test
    public void testCamposVacios() {
        robot.delay(GuiTestUtils.getDelay());
        // Deberia estar todos los textField vacios
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
        GuiTestUtils.cargarJTextField(nombre_usuario, "a", robot);

        Assert.assertFalse("El boton de log in deberia estar deshabilitado", login.isEnabled());
        Assert.assertTrue("El boton de registrarse deberia estar habilitado", registrar.isEnabled());
    }

    @Test
    public void testLogSoloPass() {
        GuiTestUtils.cargarJTextField(password, "a", robot);

        Assert.assertFalse("El boton de log in deberia estar deshabilitado", login.isEnabled());
        Assert.assertTrue("El boton de registrarse deberia estar habilitado", registrar.isEnabled());
    }

    @Test
    public void testLogCorrecto() {
        GuiTestUtils.cargarJTextField(nombre_usuario, "a", robot);
        GuiTestUtils.cargarJTextField(password, "a", robot);

        Assert.assertTrue("El boton de log in deberia estar habilitado", login.isEnabled());
        Assert.assertTrue("El boton de registrarse deberia estar habilitado", registrar.isEnabled());
    }

    @After
    public void tearDown() {
        JFrame vista = (JFrame) controlador.getVista();
        vista.setVisible(false);
    }

}
