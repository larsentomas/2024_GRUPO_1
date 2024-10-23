import excepciones.*;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Constantes;

import modeloDatos.Auto;
import modeloDatos.Cliente;
import modeloDatos.Pedido;


public class TestEmpresa {
    Empresa emp;
    Cliente cliente;
    Pedido pedido1;
    Auto auto;
    ChoferPermanente chofer;

    @Before
    public void setUp() throws UsuarioYaExisteException {
        emp = Empresa.getInstance();
        emp.getClientes().clear();
        emp.getChoferes().clear();
        emp.getVehiculos().clear();
        emp.agregarCliente("pepe123", "mandarina123", "Pepe Fernandez");
        cliente = emp.getClientes().get("pepe123");
        pedido1 = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD);
        auto = new Auto("AAA123", 4, false);
        chofer = new ChoferPermanente("47859632", "Mateo", 2020, 4);

    }

    @Test
    public void testAgregarChofer() {
        try {
            emp.agregarChofer(chofer);
            Assert.assertTrue(emp.getChoferes().containsValue(chofer));

            emp.agregarChofer(chofer);
            Assert.fail("Deberia haber lanzado la excepcion ChoferRepetidoException");
        } catch (ChoferRepetidoException e) {}
    }

    @Test
    public void testAgregarCliente() {
        try {
            emp.agregarCliente("maite123", "cantrasenia", "Maite Nigro");
            Assert.assertTrue(emp.getClientes().containsKey("maite123"));

            emp.agregarCliente("maite123", "cantrasenia", "Maite Nigro");
            Assert.fail("Deberia haber lanzado la excepcion UsuarioYaExisteException");
        } catch (UsuarioYaExisteException e) {
        }
    }

    @Test
    public void testAgregarPedido() {
        try {
            Cliente clienteNoRegistrado = new Cliente("Lorenzo123", "contrasenia", "Lorenzo Martin");

            Moto moto = new Moto("QQQ123");
            emp.agregarVehiculo(moto);
            Pedido pedido2 = new Pedido(cliente, 10, false, true, 6, Constantes.ZONA_STANDARD);
            Pedido pedido3 = new Pedido(cliente, 2, false, true, 6, Constantes.ZONA_STANDARD);
            Pedido pedido4 = new Pedido(clienteNoRegistrado, 3, false, true, 6, Constantes.ZONA_STANDARD);

            try {
                emp.agregarPedido(pedido2);
                Assert.fail("Deberia haber lanzado la excepcion SinVehiculoParaPedidoException");
            } catch (SinVehiculoParaPedidoException | ClienteNoExisteException | ClienteConViajePendienteException | ClienteConPedidoPendienteException e){}


                // FALTA CLIENTECONVIAJEPENDIENTE, como hago que el cliente tenga el viaje pendiente?


            try {
                emp.agregarPedido(pedido1);
                emp.agregarPedido(pedido3);
                Assert.fail("Deberia haber lanzado la excepcion ClienteConPedidoPendienteException");
            } catch (ClienteConPedidoPendienteException | ClienteNoExisteException | ClienteConViajePendienteException | SinVehiculoParaPedidoException e) {}

            try {
                emp.agregarPedido(pedido4);
                Assert.fail("Deberia haber lanzado la excepcion ClienteNoExisteException");
            } catch (ClienteConPedidoPendienteException | ClienteNoExisteException | ClienteConViajePendienteException | SinVehiculoParaPedidoException e) {}


        } catch(VehiculoRepetidoException e) {
            Assert.fail("No deberia haber arrojado excepcion VehiculoRepetidoException");
        }
    }

    @Test
    public void testVehiculosOrdenadosPorPedido() {
        try {
            emp.agregarVehiculo(auto);
            Assert.assertFalse(emp.vehiculosOrdenadosPorPedido(pedido1).isEmpty());
        } catch (VehiculoRepetidoException e) {
            Assert.fail("No deberia haber arrojado excepcion VehiculoRepetidoException");
        }
    }

    @Test
    public void testValidarPedido() {
        try {
            Pedido pedidoImposible = new Pedido(cliente, 10, false, true, 6, Constantes.ZONA_STANDARD);

            Assert.assertFalse(emp.validarPedido(pedidoImposible));

            emp.agregarVehiculo(auto);
            Assert.assertTrue(emp.validarPedido(pedido1));
        } catch (VehiculoRepetidoException e) {
            Assert.fail("No deberia haber arrojado excepcion VehiculoRepetidoException");
        }
    }

    @Test
    public void testAgregarVehiculo() {
        try {
            emp.agregarVehiculo(auto);
            Assert.assertTrue(emp.getVehiculos().containsValue(auto));

            emp.agregarVehiculo(auto);
            Assert.fail("Deberia haber arrojado excepcion VehiculoRepetidoException");
        } catch (VehiculoRepetidoException e) {
        }
    }


        // SEGUIR POR ACA

    }