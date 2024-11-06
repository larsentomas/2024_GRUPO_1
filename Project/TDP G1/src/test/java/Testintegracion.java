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
        this.cliente = emp.getClientes().get("pepe123");

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
            Assert.fail("No deberia lanzar excepcion");
        }
    }

    @Test
    public void testLoginAdminCorrecto() throws UsuarioNoExisteException, PasswordErroneaException {
        try {
            emp.login("admin", "admin");
            Assert.assertEquals(emp.getUsuarioLogeado().getNombreUsuario(), "admin");
        } catch (UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia lanzar excepcion");
        }
    }

    @Test
    public void testLoginUsuarioNoExiste() {
        try {
            emp.login("veronfran", "mandarina123");
            Assert.fail("Deberia lanzar excepcion");
        } catch (UsuarioNoExisteException e) {
        } catch (PasswordErroneaException e) {
            Assert.fail("No deberia lanzar excepcion");
        }
    }

    @Test
    public void testLoginPasswordErronea() {
        try {
            emp.login("franveron", "mandarina");
            Assert.fail("Deberia lanzar excepcion");
        } catch (UsuarioNoExisteException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (PasswordErroneaException e) {
        }
    }
    //------------------------------------REGISTRO------------------------------------------

    @Test
    public void testRegistroCorrecto() throws UsuarioYaExisteException {
        try {
            emp.agregarCliente("tlarsen", "mandarina", "Tomas Larsen");
            emp.login("tlarsen", "mandarina");
            Assert.assertEquals(emp.getUsuarioLogeado().getNombreUsuario(), emp.getClientes().get("tlarsen"));
        } catch (UsuarioYaExisteException | UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia lanzar excepcion");
        }
    }

    @Test
    public void testRegistroUsuarioYaExiste() {
        try {
            emp.agregarCliente("franveron", "mandarina123", "Francisco Veron");
            Assert.fail("Deberia lanzar excepcion");
        } catch (UsuarioYaExisteException e) {

        }
    }

    //------------------------------------CLIENTE------------------------------------------

    @Test
    public void testAgregarPedidoCorrecto() {
        try {
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            Assert.assertTrue(emp.getPedidos().containsValue(pedido));
        } catch (ClienteNoExisteException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (ClienteConViajePendienteException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (SinVehiculoParaPedidoException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (ClienteConPedidoPendienteException e) {
            Assert.fail("No deberia lanzar excepcion");
        }
    }

    @Test
    public void testAgregarPedidoClienteNoExiste() {
        try {
            Pedido pedido = new Pedido(new Cliente("tlarsen", "mandarina", "Tomas Larsen"), 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException e) {
        } catch (ClienteConViajePendienteException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (SinVehiculoParaPedidoException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (ClienteConPedidoPendienteException e) {
            Assert.fail("No deberia lanzar excepcion");
        }
    }

    @Test
    public void testAgregarPedidoClienteConViajePendiente() {
        try {
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            emp.crearViaje(pedido,chofer,vehiculo);
            emp.agregarPedido(pedido);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | VehiculoNoValidoException | VehiculoNoDisponibleException |
                 PedidoInexistenteException | ChoferNoDisponibleException | ClienteConPedidoPendienteException |
                 SinVehiculoParaPedidoException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (ClienteConViajePendienteException e){
        }
    }

    @Test
    public void testAgregarPedidoSinVehiculoParaPedido() {
        try {
            Pedido pedido = new Pedido(cliente, 3, true, false, 6, Constantes.ZONA_STANDARD);
            //El pedido tiene mascota no deberia conseguir auto
            emp.agregarPedido(pedido);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (ClienteConViajePendienteException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (SinVehiculoParaPedidoException e) {
        } catch (ClienteConPedidoPendienteException e) {
            Assert.fail("No deberia lanzar excepcion");
        }
    }

    @Test
    public void testAgregarPedidoClienteConPedidoPendiente() {
        try {
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            emp.agregarPedido(pedido);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (ClienteConViajePendienteException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (SinVehiculoParaPedidoException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (ClienteConPedidoPendienteException e) {
        }
    }

    @Test
    public void testPagarYFinalizarCorrecto(){
        try {
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            emp.crearViaje(pedido,chofer,vehiculo);
            emp.pagarYFinalizarViaje(5);
            Assert.assertFalse(emp.getViajesIniciados().containsKey(cliente));
        } catch (ClienteNoExisteException | VehiculoNoValidoException | VehiculoNoDisponibleException |
                 PedidoInexistenteException | ChoferNoDisponibleException | ClienteConPedidoPendienteException |
                 SinVehiculoParaPedidoException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (ClienteConViajePendienteException | ClienteSinViajePendienteException e) {
            Assert.fail("No deberia lanzar excepcion");
        }
    }

    @Test
    public void testPagarYFinalizarClienteSinViajePendiente(){
        try {
            emp.pagarYFinalizarViaje(5);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteSinViajePendienteException e) {
        }
    }

    //------------------------------------ADMINISTRADOR------------------------------------------

    @Test
    public void testAgregarVehiculoCorrecto() throws VehiculoRepetidoException {
        try {
            Vehiculo vehiculo = new Auto("AAA111", 4, true);
            emp.agregarVehiculo(vehiculo);
            Assert.assertTrue(emp.getVehiculos().containsValue(vehiculo));
        } catch (VehiculoRepetidoException e) {
            Assert.fail("No deberia lanzar excepcion");
        }
    }

    @Test
    public void testAgregarVehiculoRepetido() {
        try {
            emp.agregarVehiculo(vehiculo);
            Assert.fail("Deberia lanzar excepcion");
        } catch (VehiculoRepetidoException e) {
        }
    }

    @Test
    public void testAgregarChoferCorrecto() throws ChoferRepetidoException {
        try {
            ChoferPermanente chofer = new ChoferPermanente("16186552", "Jorge", 2012, 1);
            emp.agregarChofer(chofer);
            Assert.assertTrue(emp.getChoferes().containsValue(chofer));
        } catch (ChoferRepetidoException e) {
            Assert.fail("No deberia lanzar excepcion");
        }
    }

    @Test
    public void testAgregarChoferRepetido() {
        try {
            emp.agregarChofer(chofer);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ChoferRepetidoException e) {
        }
    }

    @Test
    public void testCrearViajeCorrecto(){
        try {
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            emp.crearViaje(pedido,chofer,vehiculo);
            Assert.assertTrue(emp.getViajesIniciados().containsKey(cliente));
        } catch (ClienteNoExisteException | VehiculoNoValidoException | VehiculoNoDisponibleException |
                 PedidoInexistenteException | ChoferNoDisponibleException | ClienteConPedidoPendienteException |
                 SinVehiculoParaPedidoException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (ClienteConViajePendienteException e){
            Assert.fail("No deberia lanzar excepcion");
        }
    }

    @Test
    public void testCrearViajeClienteConViajePendiente(){
        try {
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            emp.crearViaje(pedido,chofer,vehiculo);
            emp.crearViaje(pedido,chofer,vehiculo);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | VehiculoNoValidoException | VehiculoNoDisponibleException |
                 PedidoInexistenteException | ChoferNoDisponibleException | ClienteConPedidoPendienteException |
                 SinVehiculoParaPedidoException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (ClienteConViajePendienteException e){
        }
    }

    @Test
    public void testCrearViajePedidoInexistente(){
        try {
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.crearViaje(pedido,chofer,vehiculo);
            Assert.fail("Deberia lanzar excepcion");
        } catch (VehiculoNoValidoException | VehiculoNoDisponibleException | ChoferNoDisponibleException |
                 ClienteConViajePendienteException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (PedidoInexistenteException e){
        }
    }

    @Test
    public void testCrearViajeVehiculoNoValido(){
        try {
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            Vehiculo vehiculo = new Auto("AAA111", 4, true);
            emp.crearViaje(pedido,chofer,vehiculo);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | VehiculoNoDisponibleException | PedidoInexistenteException |
                 ChoferNoDisponibleException | ClienteConPedidoPendienteException | SinVehiculoParaPedidoException |
                 ClienteConViajePendienteException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (VehiculoNoValidoException e){
        }
    }

    @Test
    public void testCrearViajeVehiculoNoDisponible(){
        try {
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            Vehiculo vehiculo = new Auto("AAA111", 4, true);
            emp.agregarVehiculo(vehiculo);
            emp.crearViaje(pedido,chofer,vehiculo);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | VehiculoNoValidoException | PedidoInexistenteException |
                 ChoferNoDisponibleException | ClienteConPedidoPendienteException | ClienteConViajePendienteException |
                 SinVehiculoParaPedidoException | VehiculoRepetidoException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (VehiculoNoDisponibleException e){
        }
    }

    @Test
    public void testCrearViajeChoferNoDisponible(){
        try {
            Pedido pedido = new Pedido(cliente, 3, false, false, 6, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido);
            ChoferPermanente chofer = new ChoferPermanente("16186552", "Jorge", 2012, 1);
            emp.agregarChofer(chofer);
            emp.crearViaje(pedido,chofer,vehiculo);
            Assert.fail("Deberia lanzar excepcion");
        } catch (ClienteNoExisteException | VehiculoNoValidoException | VehiculoNoDisponibleException |
                 PedidoInexistenteException | ClienteConPedidoPendienteException | ClienteConViajePendienteException |
                 SinVehiculoParaPedidoException | ChoferRepetidoException e) {
            Assert.fail("No deberia lanzar excepcion");
        } catch (ChoferNoDisponibleException e){
        }
    }
}
