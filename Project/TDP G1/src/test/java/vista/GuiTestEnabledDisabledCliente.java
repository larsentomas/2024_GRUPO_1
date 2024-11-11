package vista;

import java.awt.*;

import controlador.Controlador;
import excepciones.*;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.After;
import org.junit.Assert;
import util.Constantes;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

public class GuiTestEnabledDisabledCliente {

    static String usuario = "franveron";
    static String password = "mandarina123";
    static String nombre = "Francisco Veron";

    Robot robot;
    Controlador controlador;
    Empresa e;
    Cliente cliente;

    JTextArea pedidosYViajes;

    JTextField calificacion;
    JTextField valorViaje;
    JTextField cantPax;
    JTextField cantKM;

    JRadioButton zonaEstandar;
    JRadioButton zonaSinAsfaltar;
    JRadioButton zonaPeligrosa;

    JCheckBox baul;
    JCheckBox mascota;

    JButton calificarPagar;
    JButton nuevoPedido;
    JButton cerrarSesion;


    @Before
    public void setUp() {
        try {
            robot = new Robot();
            e = Empresa.getInstance();
            e.agregarCliente(usuario, password, nombre);
            cliente = e.getClientes().get(usuario);

            // Por cada test se abre una nueva ventana, por lo que se deben re definir los botones y textos
            controlador = new Controlador();

            JButton login = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LOGIN);

            JTextField nombre_usuario = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
            JTextField password_usuario = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PASSWORD);

            // Logueo usuario
            GuiTestUtils.cargarJTextField(nombre_usuario, usuario, robot);
            GuiTestUtils.cargarJTextField(password_usuario, password, robot);
            GuiTestUtils.clickComponente(login, robot);
            robot.delay(GuiTestUtils.getDelay());

            pedidosYViajes = (JTextArea) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PEDIDO_O_VIAJE_ACTUAL);
            calificacion = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CALIFICACION_DE_VIAJE);
            valorViaje = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.VALOR_VIAJE);
            cantPax = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CANT_PAX);
            cantKM = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CANT_KM);

            zonaEstandar = (JRadioButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.ZONA_STANDARD);
            zonaSinAsfaltar = (JRadioButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.ZONA_SIN_ASFALTAR);
            zonaPeligrosa = (JRadioButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.ZONA_PELIGROSA);

            baul = (JCheckBox) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CHECK_BAUL);
            mascota = (JCheckBox) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CHECK_MASCOTA);

            calificarPagar = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CALIFICAR_PAGAR);
            nuevoPedido = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO);
            cerrarSesion = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CERRAR_SESION_CLIENTE);

        } catch (Exception ex) {
        }
    }

    @Test
    public void testCamposVacios() {
        robot.delay(GuiTestUtils.getDelay());
        // Deberian estar todos los textfield vacios
        Assert.assertTrue("Valor de viaje deberia estar vacio", valorViaje.getText().isEmpty());
        Assert.assertTrue("Cantidad de pasajeros deberia estar vacio", cantPax.getText().isEmpty());
        Assert.assertTrue("Cantidad de km deberia estar vacio", cantKM.getText().isEmpty());
        Assert.assertTrue("Calificacion deberia estar vacio", calificacion.getText().isEmpty());

        Assert.assertTrue("Cerrar sesion deberia estar visible", cerrarSesion.isVisible());
    }

    @Test
    public void testSinViaje() {
        robot.delay(GuiTestUtils.getDelay());
        Assert.assertEquals("Panel de pedido y viaje deberia estar vacio", pedidosYViajes.getText().length(), 0);

        Assert.assertFalse("Calificacion deberia estar deshabilitado", calificacion.isEnabled());
        Assert.assertFalse("Calificar y pagar deberia estar deshabilitado", calificarPagar.isEnabled());
        Assert.assertTrue("Valor viaje deberia estar habilitado", valorViaje.isEnabled());
        Assert.assertTrue("Valor de viaje deberia estar vacio", valorViaje.getText().isEmpty());

        Assert.assertTrue("Cantidad de pasajeros deberia estar habilitado", cantPax.isEnabled());
        Assert.assertTrue("Cantidad de km deberia estar habilitado", cantKM.isEnabled());
        Assert.assertTrue("Zona estandar deberia estar habilitado", zonaEstandar.isEnabled());
        Assert.assertTrue("Zona sin asfaltar deberia estar habilitado", zonaSinAsfaltar.isEnabled());
        Assert.assertTrue("Zona peligrosa deberia estar habilitado", zonaPeligrosa.isEnabled());
        Assert.assertTrue("Baul deberia estar habilitado", baul.isEnabled());
        Assert.assertTrue("Mascota deberia estar habilitado", mascota.isEnabled());
        Assert.assertFalse("Nuevo pedido deberia estar deshabilitado", nuevoPedido.isEnabled()); // Porque cant pax esta vacio y cant km tambien

        // Caso 1: Todo vacio, INVALIDO
        Assert.assertFalse("Nuevo pedido deberia estar deshabilitado", nuevoPedido.isEnabled());

        // Caso 2: Todo valido, Limite inferior cantidad de pasajeros y limite inferior cantidad de km
        GuiTestUtils.cargarJTextField(cantPax, "1", robot);
        GuiTestUtils.cargarJTextField(cantKM, "1", robot);
        Assert.assertTrue("Nuevo pedido deberia estar habilitado", nuevoPedido.isEnabled());

        // Caso 3: Todo valido, Limite superior cantidad de pasajeros, VALIDO
        GuiTestUtils.limpiarYCargar(cantPax, "10", robot);
        Assert.assertTrue("Nuevo pedido deberia estar habilitado", nuevoPedido.isEnabled());

        // Caso 4: Cantidad pasajeros valido, cant kilometros invalido
        GuiTestUtils.limpiarYCargar(cantKM, "", robot);
        Assert.assertFalse("Nuevo pedido deberia estar deshabilitado", nuevoPedido.isEnabled());

        // Caso 5: Cantidad pasajeros invalido, cant kilometros valido
        GuiTestUtils.limpiarYCargar(cantPax, "", robot);
        GuiTestUtils.limpiarYCargar(cantKM, "1", robot);
        Assert.assertFalse("Nuevo pedido deberia estar deshabilitado", nuevoPedido.isEnabled());
    }

    @Test
    public void testConPedido() {
        try {
            Pedido pedido1 = new Pedido(cliente, 2, false, false, 2, Constantes.ZONA_STANDARD);
            // Hacer un pedido
            e.agregarPedido(pedido1);
            controlador.getVista().actualizar();
            robot.delay(GuiTestUtils.getDelay());

            Assert.assertTrue("Panel de pedido y viaje deberia tener un pedido", pedidosYViajes.getText().contains("Pedido"));

            Assert.assertFalse("Calificacion deberia estar deshabilitado", calificacion.isEnabled()); //TODO: No se cumple y salta siempre error, por eso lo comento
            Assert.assertFalse("Calificar y pagar deberia estar deshabilitado", calificarPagar.isEnabled());
            Assert.assertTrue("Valor viaje deberia estar habilitado", valorViaje.isEnabled());
            Assert.assertTrue("Valor de viaje deberia estar vacio", valorViaje.getText().isEmpty());
            Assert.assertFalse("Cantidad de pasajeros deberia estar deshabilitado", cantPax.isEnabled());
            Assert.assertFalse("Cantidad de km deberia estar deshabilitado", cantKM.isEnabled());
            Assert.assertFalse("Zona estandar deberia estar deshabilitado", zonaEstandar.isEnabled());
            Assert.assertFalse("Zona sin asfaltar deberia estar deshabilitado", zonaSinAsfaltar.isEnabled());
            Assert.assertFalse("Zona peligrosa deberia estar deshabilitado", zonaPeligrosa.isEnabled());
            Assert.assertFalse("Baul deberia estar deshabilitado", baul.isEnabled());
            Assert.assertFalse("Mascota deberia estar deshabilitado", mascota.isEnabled());
            Assert.assertFalse("Nuevo pedido deberia estar deshabilitado", nuevoPedido.isEnabled());
        } catch (SinVehiculoParaPedidoException | ClienteNoExisteException | ClienteConViajePendienteException | ClienteConPedidoPendienteException e) {}
    }

    @Test
    public void testConViaje() {
        try {
            Pedido pedido1 = new Pedido(cliente, 2, false, false, 2, Constantes.ZONA_STANDARD);
            Auto auto = new Auto("AAA123", 4, true);
            Chofer chofer = new ChoferPermanente("12345678", "Juan Perez", 2000, 2);
            e.agregarChofer(chofer);
            e.agregarVehiculo(auto);
            e.agregarPedido(pedido1);
            // Cargo viaje
            e.crearViaje(pedido1, chofer, auto);
            Viaje viaje = e.getViajeDeCliente(cliente);
            controlador.getVista().actualizar();
            robot.delay(GuiTestUtils.getDelay());

            Assert.assertTrue("Panel de pedido y viaje deberia tener un viaje", pedidosYViajes.getText().contains("Viaje"));

            Assert.assertFalse("Cantidad Pax deberia estar deshabilitado", cantPax.isEnabled());
            Assert.assertFalse("Cantidad KM deberia estar deshabilitado", cantKM.isEnabled());
            Assert.assertFalse("Zona estandar deberia estar deshabilitado", zonaEstandar.isEnabled());
            Assert.assertFalse("Zona sin asfaltar deberia estar deshabilitado", zonaSinAsfaltar.isEnabled());
            Assert.assertFalse("Zona peligrosa deberia estar deshabilitado", zonaPeligrosa.isEnabled());
            Assert.assertFalse("Baul deberia estar deshabilitado", baul.isEnabled());
            Assert.assertFalse("Mascota deberia estar deshabilitado", mascota.isEnabled());
            Assert.assertFalse("Nuevo pedido deberia estar deshabilitado", nuevoPedido.isEnabled());

            Assert.assertTrue("Calificacion deberia estar habilitado", calificacion.isEnabled());

            String calificacionEsperada = String.valueOf(Double.valueOf(viaje.getValor()));
            Assert.assertEquals(calificacionEsperada, String.valueOf(Double.valueOf(calificacion.getText())));

            // Caso 1: Todo vacio, invalido
            Assert.assertFalse("Calificar y pagar deberia estar deshabilitado", calificarPagar.isEnabled());

            // Caso 2: limite superior de calificar, VALIDO
            GuiTestUtils.cargarJTextField(calificacion, "5", robot);
            Assert.assertTrue("Calificar y pagar deberia estar habilitado", calificarPagar.isEnabled());

            // Caso 3: limite inferior de calificar, VALIDO
            GuiTestUtils.limpiarYCargar(calificacion, "0", robot);
            Assert.assertTrue("Calificar y pagar deberia estar habilitado", calificarPagar.isEnabled());

            // Caso 4 : Menor al limite inferior
            GuiTestUtils.limpiarYCargar(calificacion, "-1", robot);
            Assert.assertFalse("Calificar y pagar deberia estar deshabilitado", calificarPagar.isEnabled());

            // Caso 5: Mayor al limite superior
            GuiTestUtils.limpiarYCargar(calificacion, "6", robot);
            Assert.assertFalse("Calificar y pagar deberia estar deshabilitado", calificarPagar.isEnabled());

        } catch (Exception e) {}


    }

    @After
    public void tearDown() {
        JFrame ventana = (JFrame) controlador.getVista();
        ventana.setVisible(false);

        // Reseteo pedidos y viajes
        e.getPedidos().clear();
        e.getViajesTerminados().clear();
        e.getViajesIniciados().clear();
        e.getChoferes().clear();
        e.getVehiculos().clear();
        e.getClientes().clear();
    }



}
