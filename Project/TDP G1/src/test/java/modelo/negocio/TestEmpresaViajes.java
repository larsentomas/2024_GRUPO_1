package modelo.negocio;

import excepciones.*;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Constantes;
import util.Mensajes;

public class TestEmpresaViajes {
    static Empresa emp;

    @BeforeClass
    public static void setUp() {
        emp = Empresa.getInstance();
    }

    @Test
    public void testCrearViajePedidoInexistente() {
        Pedido pedido = null;
        try {
            // Cliente
            emp.agregarCliente("franveron1", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron1");

            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("11111111", "Mateo", 2020, 4);
            emp.agregarChofer(chofer);

            // Vehiculo
            Auto auto = new Auto("AAA111", 4, true);
            emp.agregarVehiculo(auto);

            // Pedido
            pedido = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD);
            emp.crearViaje(pedido, chofer, auto);
            Assert.fail("Deberia haber arrojado excepcion PedidoInexistenteException");
        } catch(PedidoInexistenteException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.PEDIDO_INEXISTENTE.getValor());
            Assert.assertEquals(e.getPedido(), pedido);
        } catch (Exception e) {}
    }

    @Test
    public void testCrearViajeChoferNoDisponible() {
        ChoferPermanente chofer = null;
        try {
            // Cliente
            emp.agregarCliente("franveron2", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron2");

            // Chofer
            chofer = new ChoferPermanente("22222222", "Mateo", 2020, 4);

            // Vehiculo
            Auto auto = new Auto("AAA222", 4, true);
            emp.agregarVehiculo(auto);

            // Pedido
            Pedido pedido = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            emp.crearViaje(pedido, chofer, auto);
            Assert.fail("Deberia haber arrojado excepcion ChoferNoDisponibleException");
        } catch (ChoferNoDisponibleException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.CHOFER_NO_DISPONIBLE.getValor());
            Assert.assertEquals(e.getChofer(), chofer);
        } catch (Exception e) {}
    }

    @Test
    public void testCrearViajeVehiculoNoDisponible() {
        Auto auto = null;
        try {
            // Cliente
            emp.agregarCliente("franveron3", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron3");

            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("33333333", "Mateo", 2020, 4);
            emp.agregarChofer(chofer);

            // Vehiculo
            auto = new Auto("AAA333", 4, true);

            // Pedido
            Pedido pedido = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            emp.crearViaje(pedido, chofer, auto);
            Assert.fail("Deberia haber arrojado excepcion VehiculoNoDisponibleException");
        } catch (VehiculoNoDisponibleException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.VEHICULO_NO_DISPONIBLE.getValor());
            Assert.assertEquals(e.getVehiculo(), auto);
        } catch (Exception e) {}
    }

    @Test
    public void testCrearViajeVehiculoNoValido() {
        Moto moto = null;
        try {
            // Cliente
            emp.agregarCliente("franveron4", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron4");

            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("44444444", "Mateo", 2020, 4);
            emp.agregarChofer(chofer);

            moto = new Moto("AAA123");
            emp.agregarVehiculo(moto);

            // Pedido
            Pedido pedido = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);

            emp.crearViaje(pedido, chofer, moto);
            Assert.fail("Deberia haber arrojado excepcion VehiculoNoValidoException");
        } catch (VehiculoNoValidoException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.VEHICULO_NO_VALIDO.getValor());
            Assert.assertEquals(e.getVehiculo(), moto);
        } catch (Exception e) {}
    }

    @Test
    public void testCrearViajeClienteConViaje() {
        try {
            // Cliente
            emp.agregarCliente("franveron5", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron5");

            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("55555555", "Juan", 2020, 4);
            ChoferPermanente chofer2 = new ChoferPermanente("66666666", "Pedro", 2020, 4);
            emp.agregarChofer(chofer);
            emp.agregarChofer(chofer2);

            // Vehiculo
            Auto auto = new Auto("AAA555", 4, true);
            Auto auto2 = new Auto("AAA666", 4, true);
            emp.agregarVehiculo(auto);
            emp.agregarVehiculo(auto2);

            // Pedido
            Pedido pedido1 = new Pedido(cliente, 2, false, false, 6, Constantes.ZONA_STANDARD);
            Pedido pedido2 = new Pedido(cliente, 2, false, false, 6, Constantes.ZONA_STANDARD);

            emp.agregarPedido(pedido1);
            emp.crearViaje(pedido1, chofer, auto);

            emp.getPedidos().put(cliente, pedido2);
            emp.crearViaje(pedido2, chofer2, auto2);

            Assert.fail("Deberia haber arrojado excepcion ClienteConViajePendienteException");
        } catch (ClienteConViajePendienteException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.CLIENTE_CON_VIAJE_PENDIENTE.getValor());
        } catch (Exception e) {}
    }

    @Test
    public void testAgregarViaje() {
        try {
            // Cliente
            emp.agregarCliente("franveron7", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron7");

            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("77777777", "Juan", 2020, 4);
            emp.agregarChofer(chofer);

            // Vehiculo
            Auto auto = new Auto("AAA777", 4, true);
            emp.agregarVehiculo(auto);

            // Pedido
            Pedido pedido = new Pedido(cliente, 2, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);

            emp.crearViaje(pedido, chofer, auto);

            Assert.assertTrue(emp.getViajesIniciados().containsKey(cliente));
        } catch (Exception e) {}
    }
}
