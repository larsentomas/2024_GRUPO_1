package modelo.negocio;

import excepciones.ClienteConViajePendienteException;
import excepciones.ClienteNoExisteException;
import excepciones.PasswordErroneaException;
import excepciones.UsuarioNoExisteException;
import modeloDatos.ChoferPermanente;
import modeloDatos.Cliente;
import modeloDatos.Pedido;
import modeloDatos.Vehiculo;
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
            Assert.assertEquals(1, emp.getChoferes().size());
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
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.CLIENTE_CON_VIAJE_PENDIENTE.getValor());
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

    @After
    public void tearDown()
    {
        emp.logout();

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
