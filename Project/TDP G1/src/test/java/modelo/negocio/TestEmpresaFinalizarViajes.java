package modelo.negocio;

import excepciones.ChoferRepetidoException;
import excepciones.ClienteSinViajePendienteException;
import excepciones.SinViajesException;
import modeloDatos.Auto;
import modeloDatos.ChoferPermanente;
import modeloDatos.Cliente;
import modeloDatos.Pedido;
import modeloNegocio.Empresa;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Constantes;
import util.Mensajes;

public class TestEmpresaFinalizarViajes {

    static Empresa emp;

    @BeforeClass
    public static void setUp() {
        emp = Empresa.getInstance();
    }

    @Test
    public void testPagarYFinalizarLimiteInferior() {
        try {
            // Cliente
            emp.agregarCliente("franveron1", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron1");

            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("11111111", "Mateo", 2020, 4);
            emp.agregarChofer(chofer);

            // Vehiculo
            Auto auto = new Auto("AAA111", 4, false);
            emp.agregarVehiculo(auto);

            // Pedido
            Pedido pedido = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);

            // Viaje
            emp.crearViaje(pedido, chofer, auto);

            emp.login("franveron1", "mandarina123");
            emp.pagarYFinalizarViaje(0);

            Assert.assertFalse(emp.getViajesIniciados().containsKey(cliente));
            Assert.assertNull(emp.getViajeDeCliente(cliente));
        } catch (Exception e) {}
    }

    @Test
    public void testPagarYFinalizarLimiteSuperior() {
        try {
            // Cliente
            emp.agregarCliente("franveron2", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron2");

            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("22222222", "Mateo", 2020, 4);
            emp.agregarChofer(chofer);

            // Vehiculo
            Auto auto = new Auto("AAA222", 4, false);
            emp.agregarVehiculo(auto);

            // Pedido
            Pedido pedido = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);

            // Viaje
            emp.crearViaje(pedido, chofer, auto);

            emp.login("franveron2", "mandarina123");
            emp.pagarYFinalizarViaje(5);

            Assert.assertFalse(emp.getViajesIniciados().containsKey(cliente));
            Assert.assertNull(emp.getViajeDeCliente(cliente));
        } catch (Exception e) {}
    }

    @Test
    public void testPagarYFinalizarClienteNoLogueado() {
        try {
            // Cliente
            emp.agregarCliente("franveron3", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron3");

            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("33333333", "Mateo", 2020, 4);
            emp.agregarChofer(chofer);

            // Vehiculo
            Auto auto = new Auto("AAA333", 4, false);
            emp.agregarVehiculo(auto);

            emp.setUsuarioLogeado(null);
            emp.pagarYFinalizarViaje(3);

            Assert.fail("Deberia haber lanzado excepcion ClienteSinViajePendienteException");
        } catch (ClienteSinViajePendienteException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.CLIENTE_SIN_VIAJE_PENDIENTE.getValor());
        } catch (Exception e) {}
    }

    @Test
    public void testCalificacionDeChoferVacio() {
        try {
            ChoferPermanente chofer = new ChoferPermanente("44444444", "Mateo", 2020, 4);
            emp.agregarChofer(chofer);
            emp.calificacionDeChofer(chofer);
            Assert.fail("Deberia haber arrojado excepcion SinViajesException");
        } catch (SinViajesException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.CHOFER_SIN_VIAJES.getValor());
        } catch (ChoferRepetidoException e) {}
    }

    @Test
    public void testCalificacionDeChofer() {
        try {
            // Cliente
            emp.agregarCliente("franveron5", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron5");

            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("55555555", "Mateo", 2020, 4);
            emp.agregarChofer(chofer);

            // Vehiculo
            Auto auto = new Auto("AAA555", 4, false);
            emp.agregarVehiculo(auto);

            Pedido pedido1 = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido1);

            // Viaje
            emp.crearViaje(pedido1, chofer, auto);
            emp.login("franveron5", "mandarina123");
            emp.pagarYFinalizarViaje(5);

            // Pedido 2
            Pedido pedido2 = new Pedido(cliente, 2, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido2);
            emp.crearViaje(pedido2, chofer, auto);
            emp.login("franveron2", "mandarina123");
            emp.pagarYFinalizarViaje(0);

            Assert.assertEquals(2.5, emp.calificacionDeChofer(chofer), 0.0000000001);
        } catch (Exception e) {}
    }

}
