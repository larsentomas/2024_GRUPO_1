package integracion;

import excepciones.*;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;
import util.Mensajes;

public class TestIntegracion {

    Empresa emp;
    Cliente cliente;
    Vehiculo vehiculo;
    ChoferPermanente chofer;

    @Before
    public void setUp() throws UsuarioYaExisteException, VehiculoRepetidoException, ChoferRepetidoException {

        emp = Empresa.getInstance();
        emp.agregarCliente("franveron", "mandarina123", "Francisco Veron");
        this.cliente = emp.getClientes().get("franveron");

        Vehiculo vehiculo = new Auto("AAA000", 4, false);
        emp.agregarVehiculo(vehiculo);
        this.vehiculo = vehiculo;

        ChoferPermanente chofer = new ChoferPermanente("14786952", "Juan", 2020, 4);
        emp.agregarChofer(chofer);
        this.chofer = chofer;

    }

    @After
    public void tearDown()
    {
        emp.getClientes().clear();
        emp.getPedidos().clear();

        emp.getVehiculos().clear();
        emp.getVehiculosDesocupados().clear();

        emp.getChoferes().clear();
        emp.getChoferesDesocupados().clear();

        emp.getViajesIniciados().clear();
        emp.getViajesTerminados().clear();

        emp.setUsuarioLogeado(null);
    }

    //------------------------------------LOGIN------------------------------------------
    @Test
    public void testLoginCorrecto() throws UsuarioNoExisteException, PasswordErroneaException {
        try {
            emp.login("franveron", "mandarina123");
            Assert.assertEquals(emp.getUsuarioLogeado(), emp.getClientes().get("franveron"));
        } catch (UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        }
    }

    @Test
    public void testLoginAdminCorrecto() throws UsuarioNoExisteException, PasswordErroneaException {
        try {
            emp.login("admin", "admin");
            Assert.assertTrue("Admin Logeado",emp.isAdmin());
        } catch (UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        }
    }

    @Test
    public void testLoginUsuarioNoExiste() {
        try {
            emp.login("veronfran", "mandarina123");
            Assert.fail("Deberia lanzar excepcion");
        } catch (UsuarioNoExisteException e) {
        } catch (PasswordErroneaException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        }
    }

    @Test
    public void testLoginPasswordErronea() {
        try {
            emp.login("franveron", "mandarina");
            Assert.fail("Deberia lanzar excepcion");
        } catch (UsuarioNoExisteException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (PasswordErroneaException e) {
        }
    }
    //------------------------------------REGISTRO------------------------------------------

    @Test
    public void testRegistroCorrecto() {
        try {
            emp.agregarCliente("tlarsen", "mandarina", "Tomas Larsen");
            Assert.assertTrue(emp.getClientes().containsKey("tlarsen"));
        } catch (UsuarioYaExisteException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        }
    }

    @Test
    public void testRegistroUsuarioYaExiste() {
        try {
            emp.agregarCliente("franveron", "mandarina123", "Francisco Veron");
            Assert.fail("Deberia lanzar excepcion");
        } catch (UsuarioYaExisteException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.USUARIO_REPETIDO.getValor());
            Assert.assertEquals("El parametro usuario pretendido de la excepcion es incorrecto", e.getUsuarioPretendido(), "franveron");
        }
    }

    //------------------------------------CLIENTE------------------------------------------

    @Test
    public void testAgregarPedidoCorrecto() {
        try {
            emp.login("franveron", "mandarina123");
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            Assert.assertTrue(emp.getPedidos().containsValue(pedido));
        } catch (ClienteNoExisteException | ClienteConPedidoPendienteException | SinVehiculoParaPedidoException |
                 ClienteConViajePendienteException | PasswordErroneaException | UsuarioNoExisteException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        }
    }

    @Test
    public void testAgregarPedidoClienteNoExiste() {
        try {
            emp.login("franveron", "mandarina123");
            Pedido pedido = new Pedido(new Cliente("tlarsen", "mandarina", "Tomas Larsen"), 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteConViajePendienteException | ClienteConPedidoPendienteException |
                 SinVehiculoParaPedidoException | PasswordErroneaException | UsuarioNoExisteException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (ClienteNoExisteException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.CLIENTE_NO_EXISTE.getValor());
        }

    }

    @Test
    public void testAgregarPedidoClienteConViajePendiente() {
        try {
            emp.login("franveron", "mandarina123");
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            emp.crearViaje(pedido,chofer,vehiculo);
            emp.agregarPedido(pedido);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | VehiculoNoValidoException | VehiculoNoDisponibleException |
                 PedidoInexistenteException | ChoferNoDisponibleException | ClienteConPedidoPendienteException |
                 SinVehiculoParaPedidoException | PasswordErroneaException | UsuarioNoExisteException  e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (ClienteConViajePendienteException e){
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.CLIENTE_CON_VIAJE_PENDIENTE.getValor());
        }
    }

    @Test
    public void testAgregarPedidoSinVehiculoParaPedido() {
        try {
            emp.login("franveron", "mandarina123");
            Pedido pedido = new Pedido(cliente, 3, true, false, 6, Constantes.ZONA_STANDARD);
            //El pedido tiene mascota no deberia conseguir auto
            emp.agregarPedido(pedido);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | ClienteConPedidoPendienteException | ClienteConViajePendienteException |
                 PasswordErroneaException | UsuarioNoExisteException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (SinVehiculoParaPedidoException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.SIN_VEHICULO_PARA_PEDIDO.getValor());
        }
    }

    @Test
    public void testAgregarPedidoClienteConPedidoPendiente() {
        try {
            emp.login("franveron", "mandarina123");
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            emp.agregarPedido(pedido);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | SinVehiculoParaPedidoException | ClienteConViajePendienteException |
                 PasswordErroneaException | UsuarioNoExisteException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (ClienteConPedidoPendienteException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.CLIENTE_CON_PEDIDO_PENDIENTE.getValor());
        }
    }

    @Test
    public void testPagarYFinalizarCorrecto(){
        try {
            emp.login("franveron", "mandarina123");
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            emp.crearViaje(pedido,chofer,vehiculo);
            emp.pagarYFinalizarViaje(5);
            Assert.assertFalse(emp.getViajesIniciados().containsKey(cliente));
        } catch (ClienteNoExisteException | VehiculoNoValidoException | VehiculoNoDisponibleException |
                 PedidoInexistenteException | ChoferNoDisponibleException | ClienteConPedidoPendienteException |
                 SinVehiculoParaPedidoException | ClienteConViajePendienteException |
                 ClienteSinViajePendienteException | PasswordErroneaException | UsuarioNoExisteException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        }
    }

    @Test
    public void testPagarYFinalizarClienteSinViajePendiente(){
        try {
            emp.login("franveron", "mandarina123");
            emp.pagarYFinalizarViaje(5);
            Assert.fail("Deberia lanzar excepcion");

        } catch (UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (ClienteSinViajePendienteException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.CLIENTE_SIN_VIAJE_PENDIENTE.getValor());
        }
    }

    //------------------------------------ADMINISTRADOR------------------------------------------

    @Test
    public void testAgregarVehiculoCorrecto(){
        try {
            emp.login("admin", "admin");
            Vehiculo vehiculo = new Auto("AAA111", 4, true);
            emp.agregarVehiculo(vehiculo);
            Assert.assertTrue(emp.getVehiculos().containsValue(vehiculo));
        } catch (VehiculoRepetidoException | PasswordErroneaException | UsuarioNoExisteException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        }
    }

    @Test
    public void testAgregarVehiculoRepetido(){
        try {
            emp.login("admin", "admin");
            emp.agregarVehiculo(vehiculo);
            Assert.fail("Deberia lanzar excepcion");
        } catch (PasswordErroneaException | UsuarioNoExisteException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (VehiculoRepetidoException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.VEHICULO_YA_REGISTRADO.getValor());
        }
    }

    @Test
    public void testAgregarChoferCorrecto(){
        try {
            emp.login("admin", "admin");
            ChoferPermanente chofer = new ChoferPermanente("16186552", "Jorge", 2012, 1);
            emp.agregarChofer(chofer);
            Assert.assertTrue(emp.getChoferes().containsValue(chofer));
        } catch (ChoferRepetidoException | UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        }
    }

    @Test
    public void testAgregarChoferRepetido() {
        try {
            emp.login("admin", "admin");
            emp.agregarChofer(chofer);
            Assert.fail("Deberia lanzar excepcion");
        } catch (UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (ChoferRepetidoException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.CHOFER_YA_REGISTRADO.getValor());
        }
    }

    @Test
    public void testCrearViajeCorrecto(){
        try {
            emp.login("admin", "admin");
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            emp.crearViaje(pedido,chofer,vehiculo);
            Assert.assertTrue(emp.getViajesIniciados().containsKey(cliente));
        } catch (ClienteNoExisteException | VehiculoNoValidoException | VehiculoNoDisponibleException |
                 PedidoInexistenteException | ChoferNoDisponibleException | ClienteConPedidoPendienteException |
                 SinVehiculoParaPedidoException | ClienteConViajePendienteException | UsuarioNoExisteException |
                 PasswordErroneaException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        }
    }

    @Test
    public void testCrearViajeClienteConViajePendiente(){
        try {
            emp.login("admin", "admin");

            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            emp.crearViaje(pedido,chofer,vehiculo);

            Pedido pedido2 = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido2);
            ChoferPermanente chofer2 = new ChoferPermanente("16186552", "Jorge", 2012, 1);
            emp.agregarChofer(chofer2);
            Vehiculo vehiculo2 = new Auto("AAA111", 4, true);
            emp.agregarVehiculo(vehiculo2);
            emp.crearViaje(pedido2,chofer2,vehiculo2);

            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | VehiculoNoValidoException | VehiculoNoDisponibleException |
                 PedidoInexistenteException | ChoferNoDisponibleException | ClienteConPedidoPendienteException |
                 SinVehiculoParaPedidoException | UsuarioNoExisteException | PasswordErroneaException |
                 VehiculoRepetidoException | ChoferRepetidoException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (ClienteConViajePendienteException e){
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.CLIENTE_CON_VIAJE_PENDIENTE.getValor());
        }
    }

    @Test
    public void testCrearViajePedidoInexistente(){
        try {
            emp.login("admin", "admin");
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.crearViaje(pedido,chofer,vehiculo);
            Assert.fail("Deberia lanzar excepcion");
        } catch (VehiculoNoValidoException | VehiculoNoDisponibleException | ChoferNoDisponibleException |
                 ClienteConViajePendienteException | UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (PedidoInexistenteException e){
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.PEDIDO_INEXISTENTE.getValor());
        }
    }

    @Test
    public void testCrearViajeVehiculoNoValido(){
        try {
            emp.login("admin", "admin");
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            Vehiculo vehiculo2 = new Auto("AAA111", 1, true);
            emp.agregarVehiculo(vehiculo2);
            emp.crearViaje(pedido,chofer,vehiculo2);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | VehiculoNoDisponibleException | PedidoInexistenteException |
                 ChoferNoDisponibleException | ClienteConPedidoPendienteException | SinVehiculoParaPedidoException |
                 ClienteConViajePendienteException | UsuarioNoExisteException | PasswordErroneaException |
                 VehiculoRepetidoException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (VehiculoNoValidoException e){
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.VEHICULO_NO_VALIDO.getValor());
        }
    }

    @Test
    public void testCrearViajeVehiculoNoDisponible(){
        try {
            emp.login("admin", "admin");
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            Vehiculo vehiculo2 = new Auto("AAA111", 4, true);
            emp.crearViaje(pedido,chofer,vehiculo2);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | VehiculoNoValidoException | PedidoInexistenteException |
                 ChoferNoDisponibleException | ClienteConPedidoPendienteException | ClienteConViajePendienteException |
                 SinVehiculoParaPedidoException | UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (VehiculoNoDisponibleException e){
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.VEHICULO_NO_DISPONIBLE.getValor());
        }
    }

    @Test
    public void testCrearViajeChoferNoDisponible(){
        try {
            emp.login("admin", "admin");
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            ChoferPermanente chofer2 = new ChoferPermanente("16186552", "Jorge", 2012, 1);
            emp.crearViaje(pedido,chofer2,vehiculo);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | VehiculoNoValidoException | VehiculoNoDisponibleException |
                 PedidoInexistenteException | ClienteConPedidoPendienteException | ClienteConViajePendienteException |
                 SinVehiculoParaPedidoException | UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (ChoferNoDisponibleException e){
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.CHOFER_NO_DISPONIBLE.getValor());
        }
    }
}