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
    JButton registrar;

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
    public void TestLogInPassAdminCorrecto() {
        GuiTestUtils.cargarJTextField(nombre_usuario, "admin", robot);
        GuiTestUtils.cargarJTextField(password, "admin", robot);
        GuiTestUtils.clickComponente(login, robot);

        // TODO: Como se testea que paso a ventana admin?
    }

    @Test
    public void TestLogInPassClienteCorrecto() {
        String usuario = "pepe1";
        String pass = "mandarina123";

        try {
            emp.agregarCliente(usuario, pass, "Pedro");

            GuiTestUtils.cargarJTextField(nombre_usuario, usuario, robot);
            GuiTestUtils.cargarJTextField(password, pass, robot);
            GuiTestUtils.clickComponente(login, robot);

            // TODO: Como se testea que paso a ventana cliente?
        } catch (UsuarioYaExisteException e) {}
    }

    @After
    public void tearDown() {
        JFrame ventana = (JFrame) controlador.getVista();
        ventana.setVisible(false);
    }
}
