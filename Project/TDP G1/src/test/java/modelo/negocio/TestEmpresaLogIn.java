package modelo.negocio;

import excepciones.PasswordErroneaException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaExisteException;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.*;
import util.Constantes;
import util.Mensajes;

public class TestEmpresaLogIn {

    static Empresa emp;

    @BeforeClass
    public static void setUp()  {
        try {
            emp = Empresa.getInstance();
            emp.agregarCliente("pepe123", "mandarina123", "Pepe Fernandez");
        } catch (UsuarioYaExisteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testIsAdminNull() {
        Assert.assertFalse(emp.isAdmin());
    }

    @Test
    public void testIsAdminValida() {
        try {
            emp.login("admin", "admin");
            Assert.assertTrue(emp.isAdmin());
        } catch(UsuarioNoExisteException | PasswordErroneaException e) {}
    }

    @Test
    public void testIsAdminInvalido() {
        try {
            emp.login("pepe123", "mandarina123");
            Assert.assertFalse(emp.isAdmin());
        } catch(UsuarioNoExisteException | PasswordErroneaException e) {}
    }

    @Test
    public void testLoginUsuarioValido() {
        try {
            emp.login("pepe123", "mandarina123");
            Assert.assertEquals(emp.getClientes().get("pepe123"), emp.getUsuarioLogeado());
        } catch (UsuarioNoExisteException | PasswordErroneaException e) {
        }
    }

    @Test
    public void testLoginUsuarioInexistente() {
        try {
            emp.login("pepe000", "contrasenia");
            Assert.fail("Deberia haber lanzado la excepcion UsuarioNoExisteException");
        } catch (UsuarioNoExisteException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.USUARIO_DESCONOCIDO.getValor());
            Assert.assertEquals(e.getUsuarioPretendido(), "pepe000");
        } catch (PasswordErroneaException e) {}
    }

    @Test
    public void testLoginPassErronea() {
        try {
            emp.login("pepe123", "contraseniaaa");
            Assert.fail("Deberia haber lanzado la excepcion PasswordErroneaException");
        } catch (PasswordErroneaException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.PASS_ERRONEO.getValor());
            Assert.assertEquals(e.getPasswordPretendida(), "contraseniaaa");
            Assert.assertEquals(e.getUsuarioPretendido(), "pepe123");
        } catch (UsuarioNoExisteException e) {}
    }

    @Test
    public void testLoginAdminCorrecto() {
        try {
            emp.login("admin", "admin");
            Assert.assertTrue(emp.isAdmin());
        } catch (UsuarioNoExisteException | PasswordErroneaException e) {}
    }

    @Test
    public void testLoginAdminIncorrecto() {
        try {
            emp.login("admin", "adminn");
            Assert.assertTrue(emp.isAdmin());
        } catch (UsuarioNoExisteException | PasswordErroneaException e) {}
    }

    @Test
    public void testGetUsuarioLogueadoNull() {
        emp.logout();
        Assert.assertNull(emp.getUsuarioLogeado());
    }

    @Test
    public void testGetUsuarioLogueadoAdmin() {
        try {
            emp.login("admin", "admin");
            Assert.assertEquals(Administrador.getInstance(), emp.getUsuarioLogeado());
        } catch (UsuarioNoExisteException | PasswordErroneaException e) {}
    }

    @Test
    public void testGetUsuarioLogueadoCliente() {
        try {
            emp.login("pepe123", "mandarina123");
            Assert.assertEquals(emp.getClientes().get("pepe123"), emp.getUsuarioLogeado());
            emp.logout();
        } catch (UsuarioNoExisteException | PasswordErroneaException e) {}
    }

    @After
    public void tearDown() {
        emp.logout();
    }

}
