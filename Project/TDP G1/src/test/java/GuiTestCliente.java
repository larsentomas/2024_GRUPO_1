import controlador.Controlador;
import excepciones.*;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.*;
import util.Constantes;
import util.Mensajes;

import javax.swing.*;
import java.awt.*;

public class GuiTestCliente {

    static String usuario = "franveron";
    static String password = "mandarina123";

    static Robot robot;
    static Controlador controlador;
    static Empresa e;
    static Cliente cliente;

    JTextArea pedidosYViajes;

    JTextField calificacion;
    JTextField cantPax;
    JTextField cantKM;

    JRadioButton zonaEstandar;

    JCheckBox baul;
    JCheckBox mascota;

    JButton calificarPagar;
    JButton nuevoPedido;

    JList<Viaje> viajesCliente;

    FalsoOptionPane panel = new FalsoOptionPane();

    @BeforeClass
    public static void setUpClass() {
        try {
            e = Empresa.getInstance();
            e.agregarCliente(usuario, password, "Francisco Veron");
            cliente = e.getClientes().get(usuario);
        } catch (UsuarioYaExisteException ex) {}
    }

    @Before
    public void setUp() {
        try {
            // Por cada test se abre una nueva ventana, por lo que se deben re definir los botones y textos
            robot = new Robot();
            controlador = new Controlador();
            controlador.getVista().setOptionPane(panel);

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
            cantPax = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CANT_PAX);
            cantKM = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CANT_KM);

            zonaEstandar = (JRadioButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.ZONA_STANDARD);

            baul = (JCheckBox) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CHECK_BAUL);
            mascota = (JCheckBox) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CHECK_MASCOTA);

            calificarPagar = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CALIFICAR_PAGAR);
            nuevoPedido = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO);

            viajesCliente = (JList<Viaje>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_VIAJES_CLIENTE);

            // Reinicio los pedidos para que me permita hacer varios
            e.getPedidos().clear();
        } catch (AWTException ex) {
        }

    }

    @Test
    public void testNuevoPedidoHayVehiculo() {
        try {
            // Vehiculo capaz de tomar el pedido
            e.agregarVehiculo(new Moto("BBB123"));

            // Cargo pedido
            GuiTestUtils.cargarJTextField(cantPax, "1", robot);
            GuiTestUtils.cargarJTextField(cantKM, "2", robot);
            GuiTestUtils.clickComponente(zonaEstandar, robot);

            // Hago pedido
            GuiTestUtils.clickComponente(nuevoPedido, robot);

            // Se deberian limpiar los textField
            Assert.assertTrue("El campo de cantidad de pasajeros deberia estar vacio", cantPax.getText().isEmpty());
            Assert.assertTrue("El campo de cantidad de kilometros deberia estar vacio", cantKM.getText().isEmpty());
            Assert.assertFalse("El campo baul deberia estar desmarcado", baul.isSelected());
            Assert.assertFalse("El campo mascota deberia estar desmarcado", mascota.isSelected());

        } catch (VehiculoRepetidoException ex) {
        }
    }

    @Test
    public void testNuevoPedidoNoHayVehiculo() {

        // Cargo pedido
        GuiTestUtils.cargarJTextField(cantPax, "5", robot);
        GuiTestUtils.cargarJTextField(cantKM, "2", robot);
        GuiTestUtils.clickComponente(zonaEstandar, robot);
        GuiTestUtils.clickComponente(baul, robot);
        GuiTestUtils.clickComponente(mascota, robot);

        // Hago pedido
        GuiTestUtils.clickComponente(nuevoPedido, robot);

        Assert.assertEquals("El mensaje es incorrecto", Mensajes.SIN_VEHICULO_PARA_PEDIDO.getValor(), panel.getMessage());
    }

    @Test
    public void testCalificarViaje() {
        try {
            // Me aseguro que el cliente no tenga viajes anteriores asi es mas facil fijarme si lo agrego a la lista de historicos
            e.getViajesTerminados().clear();

            // cargo chofer y auto
            Chofer c = new ChoferPermanente("12345678", "Juan Perez", 2000, 2);
            Vehiculo v = new Auto("AAA123", 4, true);

            e.getVehiculos().put("AAA123", v);
            e.getChoferes().put("12345678", c);

            // creo pedido
            Pedido p = new Pedido(cliente, 2, false, false, 2, Constantes.ZONA_STANDARD);

            // creo viaje
            e.crearViaje(p, c, v);
            Viaje viaje = e.getViajeDeCliente(cliente);

            // califico viaje y pago
            GuiTestUtils.cargarJTextField(calificacion, "5", robot);
            GuiTestUtils.clickComponente(calificarPagar, robot);

            Assert.assertTrue("El viaje ya se deberia haber terminado", pedidosYViajes.getText().isEmpty());
            Assert.assertEquals("El viaje deberia estar en viajes historicos", viajesCliente.getModel().getElementAt(0), viaje);

        } catch (Exception e) {}
    }

    @After
    public void tearDown() {
        JFrame ventana = (JFrame) controlador.getVista();
        ventana.setVisible(false);
    }

}
