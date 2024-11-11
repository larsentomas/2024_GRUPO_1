package modelo.negocio;

import excepciones.VehiculoRepetidoException;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Constantes;

import java.util.ArrayList;

public class TestEmpresaGetters {
    static Empresa emp;

    @BeforeClass
    public static void setUp() {
        emp = Empresa.getInstance();
    }

    @Test
    public void testGetHistorialViajeCliente() {
        try {
            emp.agregarCliente("franveron1", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron1");

            Assert.assertTrue(emp.getHistorialViajeCliente(cliente).isEmpty());

            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("11111111", "Juancito", 2020, 5);
            emp.agregarChofer(chofer);

            // Vehiculo
            Auto auto = new Auto("AAA111", 4, false);
            emp.agregarVehiculo(auto);

            // Pedido
            Pedido pedido1 = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido1);

            // Viaje
            emp.crearViaje(pedido1, chofer, auto);
            Viaje viaje = emp.getViajeDeCliente(cliente);
            emp.login("franveron1", "mandarina123");
            emp.pagarYFinalizarViaje(3);

            Assert.assertFalse(emp.getHistorialViajeCliente(cliente).isEmpty());
            Assert.assertEquals("El viaje guardado no es correcto", emp.getHistorialViajeCliente(cliente).get(0), viaje);
        } catch (Exception e) {}
    }

    @Test
    public void testGetHistorialViajeChofer() {
        try {
            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("22222222", "Juancito", 2020, 5);
            emp.agregarChofer(chofer);
            Assert.assertTrue("El historial de viajes del chofer deberia estar vacio", emp.getHistorialViajeChofer(chofer).isEmpty());

            // Cliente
            emp.agregarCliente("franveron2", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron2");

            // Vehiculo
            Auto auto = new Auto("AAA222", 4, false);
            emp.agregarVehiculo(auto);

            // Pedido
            Pedido pedido1 = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD);

            emp.crearViaje(pedido1, chofer, auto);
            Viaje viaje = emp.getViajeDeCliente(cliente);
            emp.login("franveron2", "mandarina123");
            emp.pagarYFinalizarViaje(3);

            Assert.assertFalse(emp.getHistorialViajeChofer(chofer).isEmpty());
            Assert.assertEquals("El viaje guardado es incorrecto", viaje, emp.getHistorialViajeChofer(chofer).get(0));
        } catch (Exception e) {}
    }

    @Test
    public void testGetPedidoDeCliente() {
        try {
            // Cliente
            emp.agregarCliente("franveron3", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron3");

            Assert.assertNull(emp.getPedidoDeCliente(cliente));

            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("33333333", "Juancito", 2020, 5);
            emp.agregarChofer(chofer);

            // Vehiculo
            Auto auto = new Auto("AAA333", 4, false);
            emp.agregarVehiculo(auto);

            // Pedido
            Pedido pedido = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            Assert.assertEquals(pedido, emp.getPedidoDeCliente(cliente));
        } catch (Exception e) {}
    }

    @Test
    public void testGetViajeDeCliente() {
        try {
            // Cliente
            emp.agregarCliente("franveron4", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron4");

            Assert.assertNull(emp.getViajeDeCliente(cliente));

            // Chofer
            ChoferPermanente chofer = new ChoferPermanente("44444444", "Juancito", 2020, 5);
            emp.agregarChofer(chofer);

            // Vehiculo
            Auto auto = new Auto("AAA444", 4, false);
            emp.agregarVehiculo(auto);

            // Pedido
            Pedido pedido = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);

            // Viaje
            emp.crearViaje(pedido, chofer, auto);
            Viaje viaje = emp.getViajesIniciados().get(cliente);

            Assert.assertEquals(viaje, emp.getViajeDeCliente(cliente));
        } catch (Exception e) {}
    }

    @Test
    public void testVehiculosOrdenadosPorPedido() {
        try {
            // Cliente
            emp.agregarCliente("franveron5", "mandarina123", "Francisco Veron");
            Cliente cliente1 = emp.getClientes().get("franveron5");

            emp.agregarCliente("franveron5.1", "mandarina123", "Francisco Veron");
            Cliente cliente2 = emp.getClientes().get("franveron5.1");

            emp.agregarCliente("franveron5.2", "mandarina123", "Francisco Veron");
            Cliente cliente3 = emp.getClientes().get("franveron5.2");

            // Vehiculos
            Combi combi1 = new Combi("AAA555", 5, false);
            Combi combi2 = new Combi("BBB555", 6, true);
            Combi combi3 = new Combi("CCC555", 7, false);
            Combi combi4 = new Combi("DDD555", 8, true);
            emp.agregarVehiculo(combi1);
            emp.agregarVehiculo(combi2);
            emp.agregarVehiculo(combi3);
            emp.agregarVehiculo(combi4);

            // Pedidos (solo se pueden cumplir por combis)
            Pedido pedido1 = new Pedido(cliente1, 5, false, false, 6, Constantes.ZONA_STANDARD);
            Pedido pedido2 = new Pedido(cliente2, 7, true, true, 6, Constantes.ZONA_STANDARD);
            Pedido pedido3 = new Pedido(cliente3, 10, false, false, 6, Constantes.ZONA_STANDARD);

            ArrayList<Vehiculo> vehiculosPedido1 = emp.vehiculosOrdenadosPorPedido(pedido1);
            ArrayList<Vehiculo> vehiculosPedido1Reales = new ArrayList<>();
            vehiculosPedido1Reales.add(combi1);
            vehiculosPedido1Reales.add(combi2);
            vehiculosPedido1Reales.add(combi3);
            vehiculosPedido1Reales.add(combi4);

            ArrayList<Vehiculo> vehiculosPedido2 = emp.vehiculosOrdenadosPorPedido(pedido2);
            ArrayList<Vehiculo> vehiculosPedido2Reales = new ArrayList<>();
            vehiculosPedido2Reales.add(combi3);
            vehiculosPedido2Reales.add(combi4);

            ArrayList<Vehiculo> vehiculosPedido3 = emp.vehiculosOrdenadosPorPedido(pedido3);

            Assert.assertEquals("El pedido1 deberia tener 4 vehiculos", 4, vehiculosPedido1.size());
            Assert.assertEquals("Los vehiculos para pedido1 son incorrectos", vehiculosPedido1Reales, vehiculosPedido1);
            Assert.assertEquals("El pedido2 deberia tener 2 vehiculos", 2, vehiculosPedido2.size());
            Assert.assertEquals("Los vehiculos para pedido2 son incorrectos", vehiculosPedido2Reales, vehiculosPedido2);
            Assert.assertNull("El pedido3 deberia tener 0 vehiculos", vehiculosPedido3);
        } catch (Exception e) {}
    }

}
