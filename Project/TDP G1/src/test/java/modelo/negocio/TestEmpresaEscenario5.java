package modelo.negocio;

import excepciones.VehiculoNoValidoException;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;
import util.Mensajes;

public class TestEmpresaEscenario5 {

    Empresa emp;
    ChoferPermanente chofer1;
    Combi combi1;
    Moto moto1;
    Cliente cliente1;
    Pedido pedido1;

    @Before
    public void setUp() {
        emp = Empresa.getInstance();

        try {
            // Clientes
            emp.agregarCliente("Lolo123", "mandarina123", "Lorenzo");
            cliente1 = emp.getClientes().get("Lolo123");

            // Choferes
            chofer1 = new ChoferPermanente("22222222", "Mateo", 2020, 4);
            emp.agregarChofer(chofer1);

            // Vehiculos
            combi1 = new Combi("BBB111", 8, true);
            moto1 = new Moto("BBB222");
            emp.agregarVehiculo(combi1);
            emp.agregarVehiculo(moto1);

            // Pedidos
            pedido1 = new Pedido(cliente1, 4, false, false,1, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido1);


        } catch (Exception e) {
            Assert.fail("No deberia haber fallado");
        }
    }

    @Test
    public void testCrearViajeVehiculoValido() {
        try {
            emp.crearViaje(pedido1, chofer1, combi1);
            Assert.fail("Deberia haber lanzado la excepcion VehiculoNoValidoException");
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testCrearViajeVehiculoNoValido() {
        try {
            emp.crearViaje(pedido1, chofer1, moto1);
            Assert.fail("Deberia haber lanzado la excepcion VehiculoNoValidoException");
        } catch (VehiculoNoValidoException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.VEHICULO_NO_VALIDO.getValor());
            Assert.assertEquals("El parametro chofer de la excepcion es incorrecto", e.getVehiculo(), moto1);
            Assert.assertEquals("El parametro pedido de la excepcion es incorrecto", e.getPedido(), pedido1);
        } catch (Exception e) {
            Assert.fail("Deberia haber lanzado la excepcion VehiculoNoValidoException, pero lanzo " + e);
        }
    }

    @After
    public void tearDown()
    {
        emp.setUsuarioLogeado(null);

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
