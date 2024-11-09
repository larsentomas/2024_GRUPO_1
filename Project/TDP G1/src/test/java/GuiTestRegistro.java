import controlador.Controlador;
import modeloDatos.Cliente;
import modeloNegocio.Empresa;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;
import util.Mensajes;

import javax.swing.*;
import java.awt.*;

public class GuiTestRegistro {

    Empresa e;
    Robot robot;
    Controlador controlador;

    JTextField nombre_usuario;
    JTextField password;
    JTextField passwordConfirm;
    JTextField nombre;

    JButton paginaRegistrarse;
    JButton registrar;
    JButton cancelar;

    FalsoOptionPane panel = new FalsoOptionPane();

    @Before
    public void setUp() {
        try {
            robot = new Robot();
            controlador = new Controlador();
            controlador.getVista().setOptionPane(panel);

            e = Empresa.getInstance();
            Cliente c = new Cliente("franveron", "mandarina123", "Francisco Veron");
            e.getClientes().put("franveron", c);

            // Abro pagina registrarse
            paginaRegistrarse = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REGISTRAR);
            robot.delay(GuiTestUtils.getDelay());
            GuiTestUtils.clickComponente(paginaRegistrarse, robot);
            robot.delay(GuiTestUtils.getDelay());

            nombre_usuario = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REG_USSER_NAME);
            password = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REG_PASSWORD);
            passwordConfirm = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REG_CONFIRM_PASSWORD);
            nombre = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REG_REAL_NAME);

            registrar = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REG_BUTTON_REGISTRAR);
            cancelar = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.REG_BUTTON_CANCELAR);
        } catch (AWTException e) {
        }
    }

    @Test
    public void testCancelar() {
        GuiTestUtils.clickComponente(cancelar, robot);
        robot.delay(GuiTestUtils.getDelay());

        try {
            JPanel paginaLogIn = (JPanel) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PANEL_LOGIN);

            Assert.assertTrue("El panel de login deberia estar visible", paginaLogIn.isShowing());
            Assert.assertFalse("El panel de registro deberia estar oculto", paginaRegistrarse.isShowing());
            Assert.assertTrue("No deberia haber ningun usuario registrado", e.getClientes().isEmpty());
        } catch (NullPointerException e) {
            Assert.fail("No se cambio a el panel de login");
        }
    }

    @Test
    public void testUsuarioDisponible() {
        Assert.assertFalse("El usuario no deberia estar registrado", e.getClientes().containsKey("a"));
        JPanel panelRegistro = (JPanel) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PANEL_REGISTRO);
        GuiTestUtils.cargarJTextField(nombre_usuario, "a", robot);
        GuiTestUtils.cargarJTextField(password, "a", robot);
        GuiTestUtils.cargarJTextField(passwordConfirm, "a", robot);
        GuiTestUtils.cargarJTextField(nombre, "a", robot);
        GuiTestUtils.clickComponente(registrar, robot);

        Assert.assertTrue("El usuario deberia estar registrado", e.getClientes().containsKey("a"));

        try {
            robot.delay(GuiTestUtils.getDelay()*2);
            JPanel panelLogIn = (JPanel) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PANEL_LOGIN);
            Assert.assertTrue("El panel de login deberia estar visible", panelLogIn.isShowing());
            Assert.assertFalse("El panel de registro deberia estar oculto", panelRegistro.isShowing());
        } catch (NullPointerException e) {
            Assert.fail("No se cambio a el panel de login");
        }
    }

    @Test
    public void testUsuarioOcupadoCliente() {
        Assert.assertTrue("El usuario deberia existir de antemano", e.getClientes().containsKey("franveron"));
        GuiTestUtils.cargarJTextField(nombre_usuario, "franveron", robot);
        GuiTestUtils.cargarJTextField(password, "a", robot);
        GuiTestUtils.cargarJTextField(passwordConfirm, "a", robot);
        GuiTestUtils.cargarJTextField(nombre, "a", robot);
        GuiTestUtils.clickComponente(registrar, robot);

        Assert.assertEquals("Solo deberia haber un cliente registrado", 1, e.getClientes().size());
        Assert.assertEquals("El mensaje es incorrecto", Mensajes.USUARIO_REPETIDO.getValor(), panel.getMessage());

    }

    @Test
    public void testUsuarioOcupadoAdmin() {
        GuiTestUtils.cargarJTextField(nombre_usuario, "admin", robot);
        GuiTestUtils.cargarJTextField(password, "a", robot);
        GuiTestUtils.cargarJTextField(passwordConfirm, "a", robot);
        GuiTestUtils.cargarJTextField(nombre, "a", robot);
        GuiTestUtils.clickComponente(registrar, robot);

        Assert.assertEquals("El mensaje es incorrecto", Mensajes.USUARIO_REPETIDO.getValor(), panel.getMessage());
    }

    @Test
    public void testPassNoCoinciden() {
        GuiTestUtils.cargarJTextField(nombre_usuario, "Maria0", robot);
        GuiTestUtils.cargarJTextField(password, "a", robot);
        GuiTestUtils.cargarJTextField(passwordConfirm, "b", robot);
        GuiTestUtils.cargarJTextField(nombre, "Maria", robot);
        GuiTestUtils.clickComponente(registrar, robot);

        Assert.assertFalse("El usuario no deberia estar registrado", e.getClientes().containsKey("Maria0"));
        Assert.assertEquals("El mensaje es incorrecto", Mensajes.PASS_NO_COINCIDE.getValor(), panel.getMessage());
    }

    @After
    public void tearDown() {
        JFrame ventana = (JFrame) controlador.getVista();
        ventana.setVisible(false);

        e.getClientes().clear();
    }

}
