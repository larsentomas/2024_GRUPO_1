package modelo.negocio;

import excepciones.UsuarioYaExisteException;
import modeloNegocio.Empresa;
import modeloDatos.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public class TestEmpresaEscenario3 {
    Empresa emp;

    Cliente cliente1;
    Cliente cliente2;

    @Before
    public void setUp() {
        emp = Empresa.getInstance();
        try {
            // Clientes
            emp.agregarCliente("juan123", "mandarina123", "Juan");
            emp.agregarCliente("martin123", "mandarina123", "Martin");
            cliente1 = emp.getClientes().get("juan123");
            cliente2 = emp.getClientes().get("martin123");
        } catch (UsuarioYaExisteException e) {
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
