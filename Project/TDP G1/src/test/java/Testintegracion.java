import excepciones.*;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

public class Testintegracion {

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
        } catch (UsuarioYaExisteException ignored) {

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
        } catch (ClienteNoExisteException ignored) {
        }

    }

    //Consultar
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
        } catch (ClienteConViajePendienteException ignored){
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
        } catch (ClienteConPedidoPendienteException ignored) {
        }
    }

    @Test
    public void testCrearViajeClienteCorrecto(){
        try {
            emp.login("franveron", "mandarina123");
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            emp.crearViaje(pedido,chofer,vehiculo);
            Assert.assertTrue(emp.getViajesIniciados().containsKey(cliente));
        } catch (ClienteNoExisteException | VehiculoNoValidoException | VehiculoNoDisponibleException |
                 PedidoInexistenteException | ChoferNoDisponibleException | ClienteConPedidoPendienteException |
                 SinVehiculoParaPedidoException | ClienteConViajePendienteException |
                 PasswordErroneaException | UsuarioNoExisteException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        }
    }

    @Test
    public void testCrearViajeClienteSinPedidoPendiente(){
        try {
            emp.login("franveron", "mandarina123");
            emp.crearViaje(new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD),chofer,vehiculo);
            Assert.fail("Deberia lanzar excepcion");
        } catch (VehiculoNoValidoException | VehiculoNoDisponibleException | ChoferNoDisponibleException |
                 ClienteConViajePendienteException | UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (PedidoInexistenteException ignored){
        }
    }

    @Test
    public void testCrearViajeClienteChoferNoDisponible(){
        try {
            emp.login("franveron", "mandarina123");
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            ChoferPermanente chofer2 = new ChoferPermanente("16186552", "Jorge", 2012, 1);
            emp.crearViaje(pedido,chofer2,vehiculo);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | VehiculoNoValidoException | VehiculoNoDisponibleException |
                 PedidoInexistenteException | ClienteConPedidoPendienteException | ClienteConViajePendienteException |
                 SinVehiculoParaPedidoException | UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (ChoferNoDisponibleException ignored){
        }
    }

    @Test
    public void testCrearViajeClienteVehiculoNoDisponible(){
        try {
            emp.login("franveron", "mandarina123");
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            Vehiculo vehiculo2 = new Auto("AAA111", 4, true);
            emp.crearViaje(pedido,chofer,vehiculo2);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | VehiculoNoValidoException | PedidoInexistenteException |
                 ChoferNoDisponibleException | ClienteConPedidoPendienteException | ClienteConViajePendienteException |
                 SinVehiculoParaPedidoException | UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (VehiculoNoDisponibleException ignored){
        }
    }

    @Test
    public void testCrearViajeClienteViajeExistente(){
        try {
            emp.login("franveron", "mandarina123");
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            emp.crearViaje(pedido,chofer,vehiculo);
            emp.crearViaje(pedido,chofer,vehiculo);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | VehiculoNoValidoException | VehiculoNoDisponibleException |
                 PedidoInexistenteException | ChoferNoDisponibleException | ClienteConPedidoPendienteException |
                 SinVehiculoParaPedidoException | UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (ClienteConViajePendienteException ignored){
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
            emp.pagarYFinalizarViaje(5);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteSinViajePendienteException ignored) {
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
        } catch (VehiculoRepetidoException ignored) {
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
        } catch (ChoferRepetidoException ignored) {
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
        } catch (ClienteConViajePendienteException ignored){
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
        } catch (PedidoInexistenteException ignored){
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
        } catch (VehiculoNoValidoException ignored){
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
        }
    }
}
