package modelo.negocio;

import excepciones.ClienteConViajePendienteException;
import excepciones.ClienteNoExisteException;
import excepciones.PasswordErroneaException;
import excepciones.UsuarioNoExisteException;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;
import util.Mensajes;

import java.util.ArrayList;

public class TestEmpresaEscenario1 {
    Empresa emp;

    @Before
    public void setUp() {
        emp = Empresa.getInstance();
    }

    @Test
    public void testAgregarChofer() {
        try {
            Assert.assertTrue("La lista de choferes deberia estar vacia", emp.getChoferes().isEmpty());
            ChoferPermanente chofer;
            chofer = new ChoferPermanente("11111111", "Mateo", 2020, 4);
            emp.agregarChofer(chofer);
            Assert.assertEquals("El chofer deberia estar en la lista de choferes", 1, emp.getChoferes().size());
            Assert.assertEquals("El chofer registrado es incorrecto", chofer, emp.getChoferes().get("11111111"));
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testAgregarCliente() {
        try {
            Assert.assertTrue("La lista de clientes deberia estar vacia", emp.getClientes().isEmpty());
            emp.agregarCliente("pepe123", "mandarina", "Pedro");
            Assert.assertEquals("El cliente se deberia haber regsitrado", 1, emp.getClientes().size());
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testAgregarPedidoClienteNoExisteException() {
        try {
            Cliente cliente1 = new Cliente("pepe123", "mandarina", "Pedro");
            Pedido pedido2 = new Pedido(cliente1, 4, false, false, 1, Constantes.ZONA_SIN_ASFALTAR);
            emp.agregarPedido(pedido2);
            Assert.fail("Deberia haber lanzado la excepcion ClienteNoExisteException");
        } catch (ClienteNoExisteException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.CLIENTE_NO_EXISTE.getValor());
        } catch (Exception e) {
            Assert.fail("Deberia haber lanzado la excepcion ClienteNoExisteException");
        }
    }

    @Test
    public void testIsAdmin_User() {
        try {
            Assert.assertFalse("Deberia haber lanzalo excepcion NullPointerException",emp.isAdmin());
        }catch (Exception e){
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testAgregarVehiculo() {
        try {
            Assert.assertTrue("La lista de vehiculos deberia estar vacia", emp.getVehiculos().isEmpty());
            Auto auto1 = new Auto("BBB112", 4, false);
            emp.agregarVehiculo(auto1);
            Assert.assertEquals("El vehiculo deberia estar en la lista de vehiculos", 1, emp.getVehiculos().size());
            Assert.assertEquals("El vehiculo registrado es incorrecto", auto1, emp.getVehiculos().get("BBB112"));
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testLogInAdmin() {
        try {
            emp.login("admin", "admin");
            Assert.assertTrue("El usuario deberia ser admin", emp.isAdmin());
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testLogInAdminPasswordErronea() {
        try {
            emp.login("admin", "adminn");
            Assert.fail("Se deberia haber lanzado la excepcion PasswordErroneaException");
        } catch (PasswordErroneaException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.PASS_ERRONEO.getValor());
            Assert.assertEquals("El parametro usuario pretendido de la excepcion es incorrecto", e.getUsuarioPretendido(), "admin");
            Assert.assertEquals("El parametro password pretendido de la excepcion es incorrecto", e.getPasswordPretendida(), "adminn");
        } catch (Exception e) {
            Assert.fail("Se deberia haber lanzado la excepcion PasswordErroneaException pero se lanzo " + e);
        }
    }

    @Test
    public void testgetUsuarioLogueado_user(){
        try {
            Assert.assertNull(emp.getUsuarioLogeado());
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @After
    public void tearDown()
    {
        emp.setUsuarioLogeado(null);

        emp.getClientes().clear();
        emp.getPedidos().clear();

        emp.getVehiculos().clear();
        emp.getVehiculosDesocupados().clear();

        emp.getChoferes().clear();
        emp.getChoferesDesocupados().clear();

        emp.getViajesIniciados().clear();
        emp.getViajesTerminados().clear();
    }
}
