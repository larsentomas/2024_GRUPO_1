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
    public void testCamposVacios() {
        robot.delay(GuiTestUtils.getDelay());
        // Deberia estar todo vacio
        Assert.assertTrue("El campo nombre de usuario deberia estar vacio", nombre_usuario.getText().isEmpty());
        Assert.assertTrue("El campo password deberia estar vacio", password.getText().isEmpty());
        Assert.assertTrue("El campo confirmar password deberia estar vacio", passwordConfirm.getText().isEmpty());
        Assert.assertTrue("El campo nombre deberia estar vacio", nombre.getText().isEmpty());
    }

    @Test
    public void testRegistroNada() {
        robot.delay(GuiTestUtils.getDelay());
        Assert.assertFalse("El boton de registrar deberia estar deshabilitado", registrar.isEnabled());
        Assert.assertTrue("El boton de cancelar deberia estar habilitado", cancelar.isEnabled());
    }

    @Test
    public void testSoloUsuario() {
        GuiTestUtils.cargarJTextField(nombre_usuario, "a", robot);

        Assert.assertFalse("El boton de registrar deberia estar deshabilitado", registrar.isEnabled());
        Assert.assertTrue("El boton de cancelar deberia estar habilitado", cancelar.isEnabled());
    }

    @Test
    public void testSoloPass() {
        GuiTestUtils.cargarJTextField(password, "a", robot);

        Assert.assertFalse("El boton de registrar deberia estar deshabilitado", registrar.isEnabled());
        Assert.assertTrue("El boton de cancelar deberia estar habilitado", cancelar.isEnabled());
    }

    @Test
    public void testSoloConfirmPass() {
        GuiTestUtils.cargarJTextField(passwordConfirm, "a", robot);

        Assert.assertFalse("El boton de registrar deberia estar deshabilitado", registrar.isEnabled());
        Assert.assertTrue("El boton de cancelar deberia estar habilitado", cancelar.isEnabled());
    }

    @Test
    public void testSoloNombre() {
        GuiTestUtils.cargarJTextField(nombre, "a", robot);

        Assert.assertFalse("El boton de registrar deberia estar deshabilitado", registrar.isEnabled());
        Assert.assertTrue("El boton de cancelar deberia estar habilitado", cancelar.isEnabled());
    }

    @Test
    public void testUsuarioTodo() {
        GuiTestUtils.cargarJTextField(nombre_usuario, "a", robot);
        GuiTestUtils.cargarJTextField(password, "a", robot);
        GuiTestUtils.cargarJTextField(passwordConfirm, "a", robot);
        GuiTestUtils.cargarJTextField(nombre, "a", robot);

        Assert.assertTrue("El boton de registrar deberia estar habilitado", registrar.isEnabled());
        Assert.assertTrue("El boton de cancelar deberia estar habilitado", cancelar.isEnabled());
    }

    @After
    public void tearDown() {
        JFrame ventana = (JFrame) controlador.getVista();
        ventana.setVisible(false);
    }

}
