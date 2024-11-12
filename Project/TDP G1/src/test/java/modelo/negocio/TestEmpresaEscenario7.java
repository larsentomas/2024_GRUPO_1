package modelo.negocio;

import modeloNegocio.Empresa;
import modeloDatos.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import util.Constantes;

public class TestEmpresaEscenario7 {
    Empresa emp;

    Cliente cliente1;
    Cliente cliente2;

    Auto auto1;
    Auto auto2;

    ChoferPermanente chofer1;

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

            // Vehiculos
            auto2 = new Auto("BBB111", 4, false);
            emp.agregarVehiculo(auto2);

            // Pedido
            pedido2 = new Pedido(cliente1, 4, false, false,1, Constantes.ZONA_STANDARD);
            emp.getPedidos().put(pedido2.getCliente(), pedido2);

            // Viaje iniciado
            chofer1 = new ChoferPermanente("22222222", "Mateo", 2020, 4);
            emp.agregarChofer(chofer1);
            auto1 = new Auto("BBB333", 4, false);
            emp.agregarVehiculo(auto1);
            pedido1 = new Pedido(cliente2, 4, false, false,1, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido1);
            emp.crearViaje(pedido1, chofer1, auto1);

        } catch (Exception e) {
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
