import controlador.Controlador;
import excepciones.UsuarioYaExisteException;
import modeloNegocio.Empresa;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;
import util.Mensajes;
import vista.DefaultOptionPane;

import javax.swing.*;
import java.awt.*;

public class GuiTestLogIn {

    Empresa emp;
    Robot robot;
    Controlador controlador;
    JTextField nombre_usuario;
    JTextField password;
    JButton login;

    FalsoOptionPane panel = new FalsoOptionPane();

    @Before
    public void setUp() {
        try {
            robot = new Robot();
            controlador = new Controlador();
            controlador.getVista().setOptionPane(panel);

            // Reiniciar la empresa
            emp = Empresa.getInstance();
            emp.getClientes().clear();

            nombre_usuario = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
            password = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PASSWORD);
            login = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LOGIN);

        } catch (AWTException e) {}
    }

    @Test
    public void TestLogInInexistente() {
        GuiTestUtils.cargarJTextField(nombre_usuario, "a", robot);
        GuiTestUtils.cargarJTextField(password, "a", robot);
        GuiTestUtils.clickComponente(login, robot);

        Assert.assertEquals("El mensaje es incorrecto", Mensajes.USUARIO_DESCONOCIDO.getValor(), panel.getMessage());
    }

    @Test
    public void TestLogInPassIncorrecto() {
        GuiTestUtils.cargarJTextField(nombre_usuario, "admin", robot);
        GuiTestUtils.cargarJTextField(password, "a", robot);
        GuiTestUtils.clickComponente(login, robot);

        Assert.assertEquals("El mensaje es incorrecto", Mensajes.PASS_ERRONEO.getValor(), panel.getMessage());
    }

    @Test
    public void TestLogInPassCorrectoAdmin() {
        JPanel panelLogIn = (JPanel) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PANEL_LOGIN);
        GuiTestUtils.cargarJTextField(nombre_usuario, "admin", robot);
        GuiTestUtils.cargarJTextField(password, "admin", robot);
        GuiTestUtils.clickComponente(login, robot);

        try {
            JPanel panelLoginAdmin = (JPanel) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PANEL_ADMINISTRADOR);
            Assert.assertTrue("El panel admin deberia estar visible", panelLoginAdmin.isShowing());
            Assert.assertFalse("El panel login deberia estar escondido", panelLogIn.isShowing());
        } catch (NullPointerException e) {
            Assert.fail("No se cambio a el panel de admin");
        }
    }

    @Test
    public void TestLogInPassCorrectoCliente() {
        try {
            JPanel panelLogIn = (JPanel) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PANEL_LOGIN);
            Empresa emp = Empresa.getInstance();
            emp.agregarCliente("pepe1", "mandarina123", "Pedro");
            GuiTestUtils.cargarJTextField(nombre_usuario, "pepe1", robot);
            GuiTestUtils.cargarJTextField(password, "mandarina123", robot);
            GuiTestUtils.clickComponente(login, robot);

            try {
                JPanel panelLoginCliente = (JPanel) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PANEL_CLIENTE);
                Assert.assertFalse("El panel login deberia estar escondido", panelLogIn.isShowing());
                Assert.assertTrue("El panel cliente deberia estar visible", panelLoginCliente.isShowing());
            } catch (NullPointerException e) {
                Assert.fail("No se cambio a el panel de cliente");
            }
        } catch (UsuarioYaExisteException e) {}
    }


    @After
    public void tearDown() {
        JFrame ventana = (JFrame) controlador.getVista();
        ventana.setVisible(false);
    }
}
