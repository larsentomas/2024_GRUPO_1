import excepciones.*;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

import modeloDatos.Auto;
import modeloDatos.Cliente;
import modeloDatos.Pedido;


public class TestEmpresa {
    Empresa emp;
    Cliente cliente;
    Pedido pedido;
    Auto auto;
    ChoferPermanente chofer;

    @Before
    public void setUp() throws VehiculoRepetidoException, ClienteNoExisteException, ClienteConViajePendienteException, SinVehiculoParaPedidoException, ClienteConPedidoPendienteException, ChoferRepetidoException, UsuarioYaExisteException {
        emp = Empresa.getInstance();
        emp.agregarCliente("pepe123", "mandarina123", "Pepe Fernandez");
        cliente = emp.getClientes().get("pepe123");
        pedido = new Pedido(cliente, 2, false, true, 6, Constantes.ZONA_STANDARD);
        auto = new Auto("AAA123", 4, false);
        chofer = new ChoferPermanente("47859632", "Mateo", 2020, 4);

        emp.agregarVehiculo(auto);
        emp.agregarPedido(pedido);
        emp.agregarChofer(chofer);
    }

    @Test
    public void testAgregarChofer() throws ChoferRepetidoException {
        Chofer chofer2 = new ChoferTemporario("74125639", "Luana");
        emp.agregarChofer(chofer2);
        Assert.assertTrue(emp.getChoferes().containsValue(chofer2));

        Assert.assertThrows(ChoferRepetidoException.class, () -> emp.agregarChofer(chofer2));
    }

    // El throws aca lo agrego copilot, esta bien? si no lo pongo marca error
    @Test
    public void testAgregarCliente() throws UsuarioYaExisteException {
        emp.agregarCliente("maite123", "cantrasenia", "Maite Nigro");
        Assert.assertTrue(emp.getClientes().containsKey("maite123"));

        Assert.assertThrows(UsuarioYaExisteException.class, () -> emp.agregarCliente("maite123", "cantrasenia", "Maite Nigro"));
    }

    @Test
    public void testAgregarPedido() throws SinVehiculoParaPedidoException, ClienteConPedidoPendienteException, ClienteConViajePendienteException, ClienteNoExisteException, UsuarioYaExisteException, VehiculoRepetidoException {

        Cliente clienteNoRegistrado = new Cliente("Lorenzo123", "contrasenia", "Lorenzo Martin");

        emp.agregarCliente("Pabloo", "pablito", "Pablo");
        Cliente clienteRegistrado = emp.getClientes().get("Pabloo");

        Moto moto = new Moto("QQQ123");
        emp.agregarVehiculo(moto); // es necesario hacer asserto de que lo agrego bien?
        Pedido pedido1 = new Pedido(clienteRegistrado, 1, false, false, 6, Constantes.ZONA_STANDARD);
        Pedido pedido2 = new Pedido(clienteRegistrado, 10, false, true, 6, Constantes.ZONA_STANDARD);
        Pedido pedido3 = new Pedido(clienteRegistrado, 2, false, true, 6, Constantes.ZONA_STANDARD);
        Pedido pedido4 = new Pedido(clienteNoRegistrado, 3, false, true, 6, Constantes.ZONA_STANDARD);

        Assert.assertThrows(SinVehiculoParaPedidoException.class, () -> emp.agregarPedido(pedido2));

        // FALTA CLIENTECONVIAJEPENDIENTE

        emp.agregarPedido(pedido1);
        Assert.assertThrows(ClienteConPedidoPendienteException.class, () -> emp.agregarPedido(pedido3));

        Assert.assertThrows(ClienteNoExisteException.class, () -> emp.agregarPedido(pedido4));
    }

    @Test
    public void testVehiculosOrdenadosPorPedido() throws VehiculoRepetidoException {
        Combi combi = new Combi("WWW111", 7, false);

        emp.agregarVehiculo(combi);

        Assert.assertTrue(!emp.vehiculosOrdenadosPorPedido(pedido).isEmpty());
        // Sabemos que debe tener por lo menos algun vehiculo (la combi), podria tambien
        // tener el vehiculo de algun test, que como no sabemos en que orden
        // se ejecutan las funciones, ya puede haber pasado o no
    }

    @Test
    public void testValidarPedido() throws VehiculoRepetidoException {
        Combi combi = new Combi("BBB123", 10, false);
        Pedido pedidoImposible = new Pedido(cliente, 10, false, true, 6, Constantes.ZONA_STANDARD);

        Assert.assertFalse(emp.validarPedido(pedidoImposible));
        // Deberia devolver siempre false, ya que todos las creados en las diferentes
        // tests (si es que ya ocurrieron) ninguna puede cargar 10 pasajeros

        emp.agregarVehiculo(combi);
        Assert.assertFalse(emp.vehiculosOrdenadosPorPedido(pedido).isEmpty());
        // Sabemos que debe tener la combi recien agregada ya que es la unica
        // de todas las test que cumple con la capacidad de 10
    }


    @Test
    public void testAgregarVehiculo() throws VehiculoRepetidoException {
        Moto moto = new Moto("ABC123");
        emp.agregarVehiculo(moto);
        Assert.assertTrue(emp.getVehiculos().containsValue(moto));

        Assert.assertThrows(VehiculoRepetidoException.class, () -> emp.agregarVehiculo(moto));
    }


    // SEGUIR POR ACA

}
