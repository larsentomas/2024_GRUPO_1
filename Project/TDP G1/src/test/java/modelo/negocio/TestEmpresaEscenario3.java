package modelo.negocio;

import excepciones.*;
import modeloNegocio.Empresa;
import modeloDatos.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;
import util.Mensajes;

import java.util.ArrayList;

public class TestEmpresaEscenario3 {
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
        if (vehiculosOrdenados.size() >= 2) {
            for (int i = 0; i < vehiculosOrdenados.size() - 1; i++) {
                Assert.assertNotNull("El vehiculo debe ser valido para el pedido", vehiculosOrdenados.get(i).getPuntajePedido(pedido1));
                Assert.assertNotNull("El vehiculo debe ser valido para el pedido", vehiculosOrdenados.get(i+1).getPuntajePedido(pedido1));
                Assert.assertTrue("La lista deberia estar ordenada de forma descendente", vehiculosOrdenados.get(i).getPuntajePedido(pedido1) >= vehiculosOrdenados.get(i + 1).getPuntajePedido(pedido1));
            }
        } else {
            if (vehiculosOrdenados.size() == 1) {
                Assert.assertNotNull("El vehiculo debe ser valido para el pedido", vehiculosOrdenados.get(0).getPuntajePedido(pedido1));
            }
        }
    }

    @Test
    public void testValidarPedido() {
        try {
            Pedido pedido2 = new Pedido(cliente2, 4, false, false, 1, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido2);
            Assert.assertTrue("El pedido deberia ser valido", emp.validarPedido(pedido2));
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testCrearViajeVehiculoNoDisponible() {
        Auto auto2 = new Auto("AAA333", 4, false);
        try {
            emp.crearViaje(pedido1, chofer1, auto2);
            Assert.fail("Deberia haber lanzado la excepcion VehiculoNoDisponibleException");
        } catch (VehiculoNoDisponibleException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.VEHICULO_NO_DISPONIBLE.getValor());
            Assert.assertEquals("El parametro chofer de la excepcion es incorrecto", e.getVehiculo(), auto2);
        } catch (Exception e) {
            Assert.fail("Deberia haber lanzado la excepcion VehiculoNoDisponibleException, pero lanzo " + e);
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

    @Test
    public void testCrearViaje() {
        try {
            emp.crearViaje(pedido1, chofer1, auto1);
            Assert.assertFalse("El pedido deberia haberse eliminado de la lista de pedidos", emp.getPedidos().containsKey(cliente1));
            Assert.assertFalse("El vehiculo deberia haberse eliminado de la lista de vehiculos desocupados", emp.getVehiculosDesocupados().contains(auto1));
            Assert.assertFalse("El chofer deberia haberse eliminado de la lista de choferes desocupados", emp.getChoferesDesocupados().contains(chofer1));
            Assert.assertTrue("El viaje deberia haberse agregado a la lista de viajes iniciados", emp.getViajesIniciados().containsKey(cliente1));
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testGetHistorialViajeCliente() {
        ArrayList<Viaje> viajesDeCliente = emp.getHistorialViajeCliente(cliente1);
        Assert.assertEquals("El historial de viajes del cliente es incorrecto", 1, viajesDeCliente.size());
    }

    @Test
    public void testGetHistorialViajeClienteVacio() {
        ArrayList<Viaje> viajesDeCliente = emp.getHistorialViajeCliente(cliente2);
        Assert.assertTrue("El historial de viajes del cliente deberia estar vacio", viajesDeCliente.isEmpty());
    }

    @Test
    public void testGetSalariosTotales(){
        try {
            double totalSalarios = 0;
            for (Chofer chofer : emp.getChoferes().values()) {
                totalSalarios += chofer.getSueldoNeto();
            }
            Assert.assertEquals("El total de sueldos a pagar es incorrecto", emp.getTotalSalarios(), totalSalarios, 0.0000000001);
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);        }
    }

    @Test
    public void testGetPedidodeCliente_conPedido(){
        try{
             Pedido pedido = emp.getPedidos().get(cliente1);
            Assert.assertEquals(pedido, emp.getPedidoDeCliente(cliente1));
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testGetPedidodeCliente_sinPedido(){
        try{
            Assert.assertNull(emp.getPedidoDeCliente(cliente2));
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testCalificacionDeChoferSinViajesException(){
        try {
            emp.calificacionDeChofer(chofer1);
            Assert.fail(" Deberia  lanzar la excepcion SinViajesException");
        } catch (SinViajesException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.CHOFER_SIN_VIAJES.getValor());
        }
    }

    @Test
    public void getHistorialViajeChofer(){
        try {
            Assert.assertTrue(emp.getHistorialViajeChofer(chofer1).isEmpty());
        }catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
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
