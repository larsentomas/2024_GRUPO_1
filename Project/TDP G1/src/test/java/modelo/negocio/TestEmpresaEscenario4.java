package modelo.negocio;

import excepciones.ClienteConPedidoPendienteException;
import excepciones.ClienteConViajePendienteException;
import excepciones.UsuarioYaExisteException;
import modeloNegocio.Empresa;
import modeloDatos.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;
import util.Mensajes;

import java.util.ArrayList;

public class TestEmpresaEscenario4 {
    Empresa emp;

    Cliente cliente1;
    Cliente cliente2;

    ChoferPermanente chofer1;
    ChoferTemporario chofer2;

    Auto auto1;
    Auto auto2;
    Moto moto1;

    Pedido pedido1;

    @Before
    public void setUp() {
        emp = Empresa.getInstance();

        try {
            // Clientes
            emp.agregarCliente("Lolo123", "mandarina123", "Lorenzo");
            emp.agregarCliente("tomi123", "mandarina123", "Tomas");
            cliente1 = emp.getClientes().get("Lolo123");
            cliente2 = emp.getClientes().get("tomi123");

            // Choferes
            chofer1 = new ChoferPermanente("22222222", "Mateo", 2020, 4);
            chofer2 = new ChoferTemporario("33333333", "Pablito");
            emp.agregarChofer(chofer1);
            emp.agregarChofer(chofer2);

            // Vehiculos
            auto1 = new Auto("BBB111", 4, false);
            auto2 = new Auto("BBB333", 4, true);
            moto1 = new Moto("BBB222");
            emp.agregarVehiculo(auto1);
            emp.agregarVehiculo(auto2);
            emp.agregarVehiculo(moto1);

            // Pedidos
            pedido1 = new Pedido(cliente1, 4, false, false,1, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido1);

            // Viaje de chofer2 y cliente1
            emp.getViajesTerminados().add(new Viaje(new Pedido(cliente1, 2, false, false, 4, Constantes.ZONA_SIN_ASFALTAR), chofer2, auto1));
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testAgregarPedidoClienteConPedidoPendiente() {
        try {
            Pedido pedido2 = new Pedido(cliente1, 4, false, false, 1, Constantes.ZONA_SIN_ASFALTAR);
            emp.agregarPedido(pedido2);
            Assert.fail("Deberia haber lanzado la excepcion ClienteConPedidoPendienteException");
        } catch (ClienteConPedidoPendienteException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.CLIENTE_CON_PEDIDO_PENDIENTE.getValor());
        } catch (Exception e) {
            Assert.fail("Deberia haber lanzado la excepcion ClienteConPedidoPendienteException, pero lanzo " + e);
        }
    }

    @Test
    public void testVehiculosOrdenadosPorPedido() {
        ArrayList<Vehiculo> vehiculosOrdenados = emp.vehiculosOrdenadosPorPedido(pedido1);
        if (vehiculosOrdenados != null && vehiculosOrdenados.size() >= 2) {
            for (int i = 0; i < vehiculosOrdenados.size() - 1; i++) {
                Assert.assertTrue("La lista deberia estar ordenada de forma descendente", vehiculosOrdenados.get(i).getPuntajePedido(pedido1) >= vehiculosOrdenados.get(i + 1).getPuntajePedido(pedido1));
            }
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
