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
        emp.getPedidos().clear();
        emp.getViajesIniciados().clear();
        emp.agregarCliente("pepe123", "mandarina123", "Pepe Fernandez");
        cliente = emp.getClientes().get("pepe123");
        pedido1 = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD);
        auto = new Auto("AAA123", 4, false);
        chofer = new ChoferPermanente("47859632", "Mateo", 2020, 4);

    }

    @Test
    public void testGetInstance() {
        Assert.assertEquals(emp, Empresa.getInstance());
        Empresa empresa2 = Empresa.getInstance();
        Assert.assertEquals(emp, empresa2);
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
            emp.agregarCliente("a", "b", "c");
            Assert.assertTrue(emp.getClientes().containsKey("a"));

            emp.agregarCliente("a", "b", "c");
            Assert.fail("Deberia haber lanzado la excepcion UsuarioYaExisteException");
        } catch (UsuarioYaExisteException e) {
        }
    }

    @Test
    public void testAgregarPedidoSinVehiculo() {
        try {
            emp.agregarPedido(pedido1);
            Assert.fail("Deberia haber lanzado la excepcion SinVehiculoParaPedidoException");
        } catch (Exception e){}
    }

    @Test
    public void testAgregarPedidoClienteConViaje() {
        try {
            Pedido pedido = new Pedido(cliente, 2, false, true, 6, Constantes.ZONA_STANDARD);
            Auto auto2 = new Auto("AAA222", 4, true);
            ChoferPermanente chofer2 = new ChoferPermanente("14786952", "Juan", 2020, 4);

            emp.agregarChofer(chofer);
            emp.agregarChofer(chofer2);
            emp.agregarVehiculo(auto);
            emp.agregarVehiculo(auto2);

            emp.agregarPedido(pedido1);
            emp.crearViaje(pedido1, chofer, auto);

            System.out.println(emp.getViajesIniciados().toString());

            emp.agregarPedido(pedido);

            Assert.fail("Deberia haber lanzado la excepcion ClienteConViajePendienteException"); // TODO: Fallo
        } catch (Exception e) {}
    }

    @Test
    public void testAgregarPedidoClienteConPedido() {
        try {
            Pedido pedido = new Pedido(cliente, 2, false, true, 6, Constantes.ZONA_STANDARD);
            Auto auto2 = new Auto("AAA222", 4, true);
            ChoferPermanente chofer2 = new ChoferPermanente("14786952", "Juan", 2020, 4);

            emp.agregarVehiculo(auto);
            emp.agregarVehiculo(auto2);
            emp.agregarChofer(chofer);
            emp.agregarChofer(chofer2);

            emp.agregarPedido(pedido1);
            emp.agregarPedido(pedido);
            Assert.fail("Deberia haber lanzado la excepcion ClienteConPedidoPendienteException");
        } catch (Exception e) {}

    }

    @Test
    public void testAgregarPedidoClienteInexistente() {
        try {
            Cliente clienteNoRegistrado = new Cliente("Lorenzo123", "contrasenia", "Lorenzo Martin");
            Pedido pedido = new Pedido(clienteNoRegistrado, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarChofer(chofer);
            emp.agregarVehiculo(auto);
            emp.agregarPedido(pedido);
            Assert.fail("Deberia haber lanzado la excepcion ClienteNoExisteException");

        } catch (Exception e) {}
    }

    @Test
    public void testAgregarPedido() {
        try {
            emp.agregarChofer(chofer);
            emp.agregarVehiculo(auto);
            emp.agregarPedido(pedido1);
            Assert.assertTrue(emp.getPedidos().containsValue(pedido1));
        } catch (Exception e) {}
    }

    @Test
    public void testVehiculosOrdenadosPorPedido() {
        try {
            emp.agregarVehiculo(auto);
            Assert.assertFalse(emp.vehiculosOrdenadosPorPedido(pedido1).isEmpty());
        } catch (VehiculoRepetidoException e) {}
    }

    @Test
    public void testValidarPedido() {
        try {
            Pedido pedidoImposible = new Pedido(cliente, 10, false, true, 6, Constantes.ZONA_STANDARD);
            Assert.assertFalse(emp.validarPedido(pedidoImposible));

            emp.agregarVehiculo(auto);
            Assert.assertTrue(emp.validarPedido(pedido1));
        } catch (VehiculoRepetidoException e) {}
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

    @Test
    public void testCrearViajePedidoInexistente() {
        try {
            emp.agregarChofer(chofer);
            emp.agregarVehiculo(auto);
            emp.crearViaje(pedido1, chofer, auto);
            Assert.fail("Deberia haber arrojado excepcion PedidoInexistenteException");
        } catch (Exception e) {}
    }

    @Test
    public void testCrearViajeChoferNoDisponible() {
        try {
            emp.agregarVehiculo(auto);
            emp.agregarPedido(pedido1);
            emp.crearViaje(pedido1, chofer, auto);
            Assert.fail("Deberia haber arrojado excepcion ChoferNoDisponibleException");
        } catch (Exception e) {}
    }

    @Test
    public void testCrearViajeVehiculoNoDisponible() {
        try {
            emp.agregarChofer(chofer);
            emp.agregarPedido(pedido1);
            emp.crearViaje(pedido1, chofer, auto);
            Assert.fail("Deberia haber arrojado excepcion VehiculoNoDisponibleException");
        } catch (Exception e) {}
    }

    @Test
    public void testCrearViajeVehiculoNoValido() {
        try {
            Moto moto = new Moto("AAA123");
            emp.agregarChofer(chofer);
            emp.agregarVehiculo(auto);
            emp.agregarVehiculo(moto);
            emp.agregarPedido(pedido1);
            emp.crearViaje(pedido1, chofer, moto);
            Assert.fail("Deberia haber arrojado excepcion VehiculoNoValidoException");
        } catch (Exception e) {}
    }

    @Test
    public void testCrearViajeClienteConViaje() {
        try {
            ChoferPermanente chofer2 = new ChoferPermanente("14786952", "Juan", 2020, 4);
            Auto auto2 = new Auto("AAA222", 4, true);
            Pedido pedido2 = new Pedido(cliente, 2, false, false, 6, Constantes.ZONA_STANDARD);

            emp.agregarChofer(chofer);
            emp.agregarChofer(chofer2);
            emp.agregarVehiculo(auto);
            emp.agregarVehiculo(auto2);

            emp.agregarPedido(pedido1);
            emp.getPedidos().put(cliente, pedido2);
            emp.crearViaje(pedido1, chofer, auto);
            emp.crearViaje(pedido2, chofer2, auto2);

            Assert.fail("Deberia haber arrojado excepcion ClienteConViajePendienteException");
        } catch (Exception e) {
        }
    }

    @Test
    public void testAgregarViaje() {
        try {
            emp.agregarChofer(chofer);
            emp.agregarVehiculo(auto);
            emp.agregarPedido(pedido1);
            emp.crearViaje(pedido1, chofer, auto);
            Assert.assertTrue(emp.getViajesIniciados().containsKey(cliente));
            Assert.assertEquals(emp.getViajesIniciados().get(cliente).getPedido(), pedido1);
        } catch (Exception e) {}
    }

    @Test
    public void testPagarYFinalizarLimiteInferior() {
        try {
            emp.agregarChofer(chofer);
            emp.agregarVehiculo(auto);
            emp.agregarPedido(pedido1);
            emp.crearViaje(pedido1, chofer, auto);

            emp.login("pepe123", "mandarina123");
            emp.pagarYFinalizarViaje(0);

            Assert.assertFalse(emp.getViajesIniciados().containsKey(cliente));

        } catch (Exception e) {}
    }

    @Test
    public void testPagarYFinalizarLimiteSuperior() {
        try {
            emp.agregarChofer(chofer);
            emp.agregarVehiculo(auto);
            emp.agregarPedido(pedido1);
            emp.crearViaje(pedido1, chofer, auto);

            emp.login("pepe123", "mandarina123");
            emp.pagarYFinalizarViaje(5);

            Assert.assertFalse(emp.getViajesIniciados().containsKey(cliente));

        } catch (Exception e) {}
    }

    @Test
    public void testPagarYFinalizarClienteNoLogueado() {
        try {
            emp.agregarChofer(chofer);
            emp.agregarVehiculo(auto);
            emp.agregarPedido(pedido1);

            emp.login("pepe123", "mandarina123");
            emp.pagarYFinalizarViaje(3);

            Assert.fail("Deberia haber lanzado excepcion ClienteSinViajePendienteException");
        } catch(UsuarioNoExisteException | PasswordErroneaException | ChoferRepetidoException | VehiculoRepetidoException | ClienteNoExisteException | ClienteConViajePendienteException | SinVehiculoParaPedidoException | ClienteConPedidoPendienteException | ClienteSinViajePendienteException e) {}
    }

    @Test
    public void testLoginUsuarioInexistente() {
        try {
            emp.login("pepe000", "contrasenia");
            Assert.fail("Deberia haber lanzado la excepcion UsuarioNoExisteException");
        } catch (UsuarioNoExisteException | PasswordErroneaException e) {}
    }

    @Test
    public void testLoginAdminPassErronea() {
        try {
            emp.login("pepe123", "contraseniaaa");
            Assert.fail("Deberia haber lanzado la excepcion PasswordErroneaException");
        } catch (PasswordErroneaException | UsuarioNoExisteException e) {}
    }

    @Test
    public void testLoginAdminCorrecto() {
        try {
            emp.agregarCliente("a", "b", "c");
            emp.login("a", "b");
            Assert.assertEquals(emp.getUsuarioLogeado(), emp.getClientes().get("a"));
        } catch (UsuarioYaExisteException | UsuarioNoExisteException | PasswordErroneaException e) {}
    }

    @Test
    public void testGetHistorialViajeCliente() {
        Assert.assertTrue(emp.getHistorialViajeCliente(cliente).isEmpty());
        try {
            emp.agregarChofer(chofer);
            emp.agregarVehiculo(auto);
            emp.agregarPedido(pedido1);
            emp.crearViaje(pedido1, chofer, auto);
            emp.login("pepe123", "mandarina123");
            emp.pagarYFinalizarViaje(3);
        } catch (Exception e) {}

        Assert.assertFalse(emp.getHistorialViajeCliente(cliente).isEmpty());
    }

    @Test
    public void testGetHistorialViajeChofer() {
        try {
            emp.agregarChofer(chofer);
            emp.agregarVehiculo(auto);
            emp.agregarPedido(pedido1);

            Assert.assertTrue(emp.getHistorialViajeChofer(chofer).isEmpty());

            emp.crearViaje(pedido1, chofer, auto);
            emp.login("pepe123", "mandarina123");
            emp.pagarYFinalizarViaje(3);
        } catch (Exception e) {}

        Assert.assertFalse(emp.getHistorialViajeChofer(chofer).isEmpty());
    }

    @Test
    public void testCalificacionDeChofer() {
        try {
            emp.agregarChofer(chofer);

            Assert.assertThrows(SinViajesException.class, () -> emp.calificacionDeChofer(chofer)); //TODO: No lanza excepcion, simplemente devuelve 0

            emp.agregarVehiculo(auto);
            emp.agregarPedido(pedido1);
            emp.crearViaje(pedido1, chofer, auto);
            emp.login("pepe123", "mandarina123");
            emp.pagarYFinalizarViaje(5);

            Pedido pedido2 = new Pedido(cliente, 2, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido2);
            emp.crearViaje(pedido2, chofer, auto);
            emp.login("pepe123", "mandarina123");
            emp.pagarYFinalizarViaje(0);

            Assert.assertEquals(2.5, emp.calificacionDeChofer(chofer), 0.0000000001);
        } catch (Exception e) {}
    }

    @Test
    public void testGetPedidoDeCliente() {
        Assert.assertNull(emp.getPedidoDeCliente(cliente));
        try {
            emp.agregarChofer(chofer);
            emp.agregarVehiculo(auto);
            emp.agregarPedido(pedido1);
            Assert.assertEquals(pedido1, emp.getPedidoDeCliente(cliente));
        } catch (Exception e) {}
    }

    @Test
    public void testGetViajeDeCliente() {
        Assert.assertNull(emp.getViajeDeCliente(cliente));
        try {
            emp.agregarChofer(chofer);
            emp.agregarVehiculo(auto);
            emp.agregarPedido(pedido1);
            emp.crearViaje(pedido1, chofer, auto);
            Assert.assertEquals(emp.getViajesIniciados().get(cliente), emp.getViajeDeCliente(cliente));
        } catch (Exception e) {}
    }

    @Test
    public void testGetUsuarioLogueadoNull() {
        emp.logout();
        Assert.assertNull(emp.getUsuarioLogeado());
    }

    @Test
    public void testGetUsuarioLogueadoAdmin() {
        try {
            emp.login("admin", "admin");
            Assert.assertEquals(Administrador.getInstance(), emp.getUsuarioLogeado());
        } catch (UsuarioNoExisteException | PasswordErroneaException e) {}
    }

    @Test
    public void testGetUsuarioLogueadoCliente() {
        try {
            emp.login("pepe123", "mandarina123");
            Assert.assertEquals(emp.getClientes().get("pepe123"), emp.getUsuarioLogeado());
            emp.logout();
        } catch (UsuarioNoExisteException | PasswordErroneaException e) {}
    }

    @Test
    public void testIsAdminNull() {
        emp.logout();
        Assert.assertFalse(emp.isAdmin());
    }

    @Test
    public void testIsAdminValida() {
        try {
            emp.login("admin", "admin");
            Assert.assertTrue(emp.isAdmin());
        } catch(UsuarioNoExisteException | PasswordErroneaException e) {}
    }

    @Test
    public void testIsAdminInvalido() {
        try {
            emp.login("pepe123", "mandarina123");
            Assert.assertFalse(emp.isAdmin());
        } catch(UsuarioNoExisteException | PasswordErroneaException e) {}
    }

}
