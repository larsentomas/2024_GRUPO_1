package modelo.negocio;

import excepciones.ClienteConViajePendienteException;
import excepciones.SinVehiculoParaPedidoException;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;
import util.Mensajes;

public class TestEmpresaEscenario6 {
    Empresa emp;

    Cliente cliente1;
    Cliente cliente2;
    ChoferPermanente chofer1;
    ChoferPermanente chofer2;
    Auto auto1;
    Auto auto2;
    Moto moto1;
    Pedido pedido1;
    Pedido pedido2;

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
            emp.agregarChofer(chofer1);

            // Vehiculos
            auto2 = new Auto("BBB111", 4, false);)
            moto1 = new Moto("BBB222");
            emp.agregarVehiculo(auto2);
            emp.agregarVehiculo(moto1);

            // Pedidos
            pedido1 = new Pedido(cliente1, 4, false, false,1, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido1);

            // Viaje iniciado de cliente2
            chofer2 = new ChoferPermanente("33333333", "Pablito", 2020, 4);
            emp.agregarChofer(chofer2);
            auto1 = new Auto("BBB333", 4, false);
            emp.agregarVehiculo(auto1);
            pedido2 = new Pedido(cliente2, 4, false, false,1, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido2);
            emp.crearViaje(pedido2, chofer2, auto1);

        } catch (Exception e) {
            Assert.fail("No deberia haber fallado");
        }
    }

    @Test
    public void testAgregarPedidoClienteConViajePendiente() {
        try {
            Pedido pedido2 = new Pedido(cliente1, 4, false, false, 1, Constantes.ZONA_SIN_ASFALTAR);
            emp.agregarPedido(pedido2);
            Assert.fail("Deberia haber lanzado la excepcion ClienteConViajePendienteException");
        } catch (ClienteConViajePendienteException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.CLIENTE_CON_VIAJE_PENDIENTE.getValor());
        } catch (Exception e) {
            Assert.fail("Deberia haber lanzado la excepcion ClienteConViajePendienteException, pero lanzo " + e);
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
