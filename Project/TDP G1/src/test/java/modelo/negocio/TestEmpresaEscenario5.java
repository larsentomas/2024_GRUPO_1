package modelo.negocio;

import modeloNegocio.Empresa;
import modeloDatos.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import util.Constantes;

public class TestEmpresaEscenario5 {
    Empresa emp;

    Cliente cliente1;
    Cliente cliente2;

    ChoferPermanente chofer1;
    Auto auto1;
    Pedido pedido1;

    @Before
    public void setUp() {
        emp = Empresa.getInstance();

        try {
            // Clientes
            emp.agregarCliente("fran123", "mandarina123", "Francisco");
            emp.agregarCliente("mati123", "mandarina123", "Matias");
            cliente1 = emp.getClientes().get("fran123");
            cliente2 = emp.getClientes().get("mati123");

            // Choferes
            chofer1 = new ChoferPermanente("11111111", "Mateo", 2020, 4);
            emp.agregarChofer(chofer1);

            // Vehiculos
            auto1 = new Auto("AAA111", 4, false);
            emp.agregarVehiculo(auto1);

            // Viaje iniciado de cliente2
            pedido1 = new Pedido(cliente2, 3, false, false, 8, Constantes.ZONA_STANDARD);
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
