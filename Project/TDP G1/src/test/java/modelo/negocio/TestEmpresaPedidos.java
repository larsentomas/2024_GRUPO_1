package modelo.negocio;

import excepciones.*;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Constantes;
import util.Mensajes;

public class TestEmpresaPedidos {
    static Empresa emp;

    @BeforeClass
    public static void setUp() {
        emp = Empresa.getInstance();
    }

    @Test
    public void testAgregarPedidoSinVehiculo() {
        Pedido pedido = null;
        try {
            // Cliente
            emp.agregarCliente("franveron1", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron1");

            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("47859632", "Mateo", 2020, 4);
            emp.agregarChofer(chofer);

            // Pedido
            pedido = new Pedido(cliente, 10, false, false, 6, Constantes.ZONA_STANDARD);
            System.out.println(emp.getVehiculosDesocupados());
            emp.agregarPedido(pedido);
            Assert.fail("Deberia haber lanzado la excepcion SinVehiculoParaPedidoException");
        } catch(SinVehiculoParaPedidoException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.SIN_VEHICULO_PARA_PEDIDO.getValor());
            Assert.assertEquals(e.getPedido(), pedido);
        } catch (Exception e){}
    }

    @Test
    public void testAgregarPedidoClienteConViaje() {
        try {
            // Cliente
            emp.agregarCliente("franveron2", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron2");

            // Vehiculo
            Auto auto1 = new Auto("AAA111", 4, true);
            Auto auto2 = new Auto("AAA222", 4, true);
            emp.agregarVehiculo(auto1);
            emp.agregarVehiculo(auto2);

            // Chofer
            ChoferPermanente chofer1 = new ChoferPermanente("11111111", "Juan", 2020, 4);
            ChoferPermanente chofer2 = new ChoferPermanente("22222222", "Juan", 2020, 4);
            emp.agregarChofer(chofer1);
            emp.agregarChofer(chofer2);

            // Pedido
            Pedido pedido1 = new Pedido(cliente, 2, false, true, 6, Constantes.ZONA_STANDARD);
            Pedido pedido2 = new Pedido(cliente, 2, false, true, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido1);
            emp.crearViaje(pedido1, chofer1, auto1);

            emp.agregarPedido(pedido2);

            Assert.fail("Deberia haber lanzado la excepcion ClienteConViajePendienteException"); // TODO: Fallo
        } catch(ClienteConViajePendienteException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.CLIENTE_CON_VIAJE_PENDIENTE.getValor());
        } catch (Exception e) {}
    }

    @Test
    public void testAgregarPedidoClienteConPedido() {
        try {
            // Cliente
            emp.agregarCliente("franveron3", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron3");

            // Vehiculo
            Auto auto1 = new Auto("BBB111", 4, true);
            emp.agregarVehiculo(auto1);

            // Chofer
            ChoferPermanente chofer1 = new ChoferPermanente("33333333", "Juan", 2020, 4);
            emp.agregarChofer(chofer1);

            // Pedido
            Pedido pedido1 = new Pedido(cliente, 2, false, true, 6, Constantes.ZONA_STANDARD);
            Pedido pedido2 = new Pedido(cliente, 2, false, true, 6, Constantes.ZONA_STANDARD);

            emp.agregarPedido(pedido1);
            emp.agregarPedido(pedido2);

            Assert.fail("Deberia haber lanzado la excepcion ClienteConPedidoPendienteException");
        } catch(ClienteConPedidoPendienteException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.CLIENTE_CON_PEDIDO_PENDIENTE.getValor());
        } catch (Exception e) {}

    }

    @Test
    public void testAgregarPedidoClienteInexistente() {
        try {
            Cliente clienteNoRegistrado = new Cliente("Lorenzo123", "contrasenia", "Lorenzo Martin");
            Pedido pedido = new Pedido(clienteNoRegistrado, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            Assert.fail("Deberia haber lanzado la excepcion ClienteNoExisteException");
        } catch (ClienteNoExisteException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.CLIENTE_NO_EXISTE.getValor());
        } catch (Exception e) {}
    }

    @Test
    public void testAgregarPedido() {
        try {
            // Cliente
            emp.agregarCliente("franveron4", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron4");

            // Vehiculo
            Auto auto = new Auto("CCC111", 4, true);
            emp.agregarVehiculo(auto);

            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("44444444", "Juan", 2020, 4);

            // Pedido
            Pedido pedido = new Pedido(cliente, 2, false, true, 6, Constantes.ZONA_STANDARD);

            emp.agregarChofer(chofer);
            emp.agregarVehiculo(auto);
            emp.agregarPedido(pedido);
            Assert.assertTrue(emp.getPedidos().containsValue(pedido));
        } catch (Exception e) {}
    }

    @Test
    public void testValidarPedido() {
        try {
            // Cliente
            emp.agregarCliente("franveron5", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron5");

            Pedido pedido = new Pedido(cliente, 9, false, true, 6, Constantes.ZONA_STANDARD);
            Assert.assertFalse(emp.validarPedido(pedido));

            Combi combi = new Combi("AAA123", 9, true);
            emp.agregarVehiculo(combi);
            Assert.assertTrue(emp.validarPedido(pedido));
        } catch (VehiculoRepetidoException | UsuarioYaExisteException e) {}
    }
}
