package modelo.negocio;

import excepciones.*;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;
import util.Mensajes;

import java.util.ArrayList;

public class TestEmpresaEscenario2 {
    Empresa emp;

    Cliente cliente1;
    ChoferPermanente chofer1;
    Auto auto1;
    Moto moto1;
    Combi combi1;

    @Before
    public void setUp() {
        emp = Empresa.getInstance();

        try {
            // Clientes
            emp.agregarCliente("pepe123", "mandarina123", "Pedro");
            cliente1 = emp.getClientes().get("pepe123");

            // Choferes
            chofer1 = new ChoferPermanente("11111111", "Mateo", 2020, 4);
            emp.agregarChofer(chofer1);

            // Vehiculos
            auto1 = new Auto("AAA111", 4, false);
            moto1 = new Moto("AAA222");
            combi1 = new Combi("AAA333", 10, true);
            emp.agregarVehiculo(auto1);
            emp.agregarVehiculo(moto1);
            emp.agregarVehiculo(combi1);

            emp.getViajesTerminados().add(new Viaje(new Pedido(cliente1, 4, false, true, 5, Constantes.ZONA_SIN_ASFALTAR), chofer1, auto1));
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testAgregarChofer() {
        try {
            Assert.assertTrue("El chofer deberia estar registrado", emp.getChoferes().containsKey("11111111"));
            emp.agregarChofer(chofer1);
            Assert.fail("Deberia haber lanzado la excepcion ChoferRepetidoException");
        } catch (ChoferRepetidoException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.CHOFER_YA_REGISTRADO.getValor());
            Assert.assertEquals("El parametro chofer existente de la excepcion es incorrecto", e.getChoferExistente(), chofer1);
            Assert.assertEquals("El parametro dni de la excepcion es incorrecto", e.getDniPrentendido(), "11111111");
        }
    }

    @Test
    public void testAgregarCliente() {
        try {
            Assert.assertTrue("El usuario ya deberia estar registrado", emp.getClientes().containsKey("pepe123"));
            emp.agregarCliente("pepe123", "mandarina", "Pedro");
            Assert.fail("Deberia haber lanzado la excepcion UsuarioYaExisteException");
        } catch (UsuarioYaExisteException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.USUARIO_REPETIDO.getValor());
            Assert.assertEquals("El parametro usuario pretendido de la excepcion es incorrecto", e.getUsuarioPretendido(), "pepe123");
        }
    }

    @Test
    public void testAgregarPedidoSinVehiculo() {
        try {
            Pedido pedido2 = new Pedido(cliente1, 4, false, false, 1, Constantes.ZONA_SIN_ASFALTAR);
            emp.agregarPedido(pedido2);
            Assert.fail("Deberia haber lanzado la excepcion SinVehiculoParaPedidoException");
        } catch (SinVehiculoParaPedidoException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.SIN_VEHICULO_PARA_PEDIDO.getValor());
        } catch (Exception e) {
            Assert.fail("Deberia haber lanzado la excepcion SinVehiculoParaPedidoException, pero lanzo " + e);
        }
    }

    @Test
    public void testAgregarPedido() {
        try {
            Assert.assertTrue("No deberia haber pedidos", emp.getPedidos().isEmpty());
            Pedido pedido2 = new Pedido(cliente1, 4, false, false, 1, Constantes.ZONA_SIN_ASFALTAR);
            emp.agregarPedido(pedido2);
            Assert.assertEquals("Deberia haber un pedido", 1, emp.getPedidos().size());
            Assert.assertEquals("El pedido deberia ser el correcto", pedido2, emp.getPedidos().get(cliente1));
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testLogin(){
        try{
            emp.login("pepe123","mandarina123");
            Assert.assertEquals(emp.getUsuarioLogeado(), emp.getClientes().get("pepe123"));
        } catch (UsuarioNoExisteException | PasswordErroneaException e){
            Assert.fail("No deberia lanzar la excepcion: " + e);
        }
    }

    @Test
    public void testLoginUsuarioIncorrecto(){
        try{
            emp.login("ximenaconX","mandarinagloton");
            Assert.fail("Deberia lanzar excepción");
        } catch (PasswordErroneaException e){
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (UsuarioNoExisteException e){
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.USUARIO_DESCONOCIDO.getValor());
            Assert.assertEquals("El parametro usuario pretendido de la excepcion es incorrecto", e.getUsuarioPretendido(), "ximenaconX");
        }
    }

    @Test
    public void testLoginPasswordErronea(){
        try{
            emp.login("pepe123","mandarinagloton");
            Assert.fail("Deberia lanzar excepción");
        } catch (UsuarioNoExisteException e){
            Assert.fail("No deberia lanzar la excepcion: " + e);
        } catch (PasswordErroneaException e){
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.PASS_ERRONEO.getValor());
            Assert.assertEquals("El parametro usuario pretendido de la excepcion es incorrecto", e.getUsuarioPretendido(), "pepe123");
        }
    }

    @Test
    public void testIsAdmin_User() {
        try {
            emp.login("pepe123","mandarina123");
            Assert.assertFalse("El user NO deberia ser admin",emp.isAdmin());
        }catch (UsuarioNoExisteException | PasswordErroneaException  e){
            Assert.fail("No deberia lanzar la excepcion: " + e);
        }
    }
    @Test
    public void testIsAdmin_Admin() {
        try {
            emp.login("admin","admin");
            Assert.assertTrue("El user deberia ser admin",emp.isAdmin());
        }catch (UsuarioNoExisteException | PasswordErroneaException e){
            Assert.fail("No deberia lanzar la excepcion: " + e);
        }
    }

    @Test
    public void testValidarPedido() {
        try {
            Pedido pedido2 = new Pedido(cliente1, 4, false, false, 1, Constantes.ZONA_STANDARD);
            emp.agregarPedido(pedido2);
            Assert.assertTrue("El pedido deberia ser valido", emp.validarPedido(pedido2));
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testAgregarVehiculo() {
        try {
            Auto auto2 = new Auto("AAA111", 4, false);
            emp.agregarVehiculo(auto2);
            Assert.fail("Deberia haber lanzado la excepcion VehiculoRepetidoException");
        } catch(VehiculoRepetidoException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.VEHICULO_YA_REGISTRADO.getValor());
            Assert.assertEquals("El parametro vehiculo existente de la excepcion es incorrecto", e.getVehiculoExistente(), auto1);
            Assert.assertEquals("El parametro matricula pretendida de la excepcion es incorrecto", e.getPatentePrentendida(), "AAA111");
        } catch (Exception e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testCrearViajePedidoInexistente() {
        Pedido pedido1 = new Pedido(cliente1, 4, false, false, 1, Constantes.ZONA_STANDARD);
        try {
            emp.crearViaje(pedido1, chofer1, auto1);
            Assert.fail("Deberia haber lanzado la excepcion PedidoInexistenteException");
        } catch (PedidoInexistenteException e) {
            Assert.assertEquals("El mensaje de la excepcion es incorrecto", e.getMessage(), Mensajes.PEDIDO_INEXISTENTE.getValor());
            Assert.assertEquals("El parametro pedido de la excepcion es incorrecto", e.getPedido(), pedido1);
        } catch (Exception e) {
            Assert.fail("Deberia haber lanzado la excepcion PedidoInexistenteException, pero lanzo " + e);
        }
    }

    @Test
    public void testgetUsuarioLogueado_user(){
        try {
            emp.login("pepe123","mandarina123");
            Assert.assertEquals("El usuario logueado es incorrecto", emp.getUsuarioLogeado(), emp.getClientes().get("pepe123"));
        } catch (UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }
    @Test
    public void testgetUsuarioLogueado_admin(){
        try {
            emp.login("admin","admin");
            Assert.assertTrue("El usuario logueado es incorrecto", emp.isAdmin());
        } catch (UsuarioNoExisteException | PasswordErroneaException e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void testCalificacionDeChofer(){
        try {
            double puntajes = 0;
            int count = 0;
            for (Viaje viaje : emp.getHistorialViajeChofer(chofer1)) {
                puntajes += viaje.getCalificacion();
                count++;
            }
            puntajes /= count;
            Assert.assertEquals("La calificacion promedio del chofer es incorrecta", puntajes, emp.calificacionDeChofer(chofer1),0.000001);
        } catch (SinViajesException e) {
            Assert.fail("No deberia haber lanzado la excepcion " + e);
        }
    }

    @Test
    public void getHistorialViajeChofer(){
        try {
            Assert.assertNotNull(emp.getHistorialViajeChofer(chofer1));
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
