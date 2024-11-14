package vista;

import controlador.Controlador;
import excepciones.ChoferRepetidoException;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.*;
import util.Constantes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GuiTestAdminGestionPedidos {

    static Robot robot;
    static Controlador controlador;
    static Empresa e;

    static JList<Pedido> pedidosPendientes;
    static JList<Chofer> choferesLibres;
    static JList<Vehiculo> vehiculosLibres;
    static JButton nuevoViaje;
    static JList<Viaje> viajesHistoricos;

    static Cliente cliente1;
    static Cliente cliente2;
    static Pedido p1;
    static Pedido p2;
    static Auto autito;
    static Moto moto;
    static Moto moto2;
    static Chofer chofer1;
    static Chofer chofer2;

    @Before
    public void setUp() {
        try {
            e = Empresa.getInstance();
            robot = new Robot();
            controlador = new Controlador();

            // Login admin
            JTextField nombre_usuario = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
            JTextField password = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PASSWORD);
            JButton login = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LOGIN);

            GuiTestUtils.cargarJTextField(nombre_usuario, "admin", robot);
            GuiTestUtils.cargarJTextField(password, "admin", robot);
            GuiTestUtils.clickComponente(login, robot);
            robot.delay(GuiTestUtils.getDelay());

            robot.delay(GuiTestUtils.getDelay());
            pedidosPendientes = (JList<Pedido>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_PEDIDOS_PENDIENTES);
            choferesLibres = (JList<Chofer>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_CHOFERES_LIBRES);
            vehiculosLibres = (JList<Vehiculo>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_DISPONIBLES);
            nuevoViaje = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.NUEVO_VIAJE);
            viajesHistoricos = (JList<Viaje>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_VIAJES_HISTORICOS);

            e.agregarCliente("pepito", "1234", "Pepe Ramon");
            e.agregarCliente("juanito", "1234", "Juan Perez");
            cliente1 = e.getClientes().get("pepito");
            cliente2 = e.getClientes().get("juanito");
            autito = new Auto("ABC123", 4, true);
            moto = new Moto("DEF456");
            moto2 = new Moto("GHI789");
            chofer1 = new ChoferTemporario("1457896", "Ramon");
            chofer2 = new ChoferPermanente("12345678", "Pepe", 2000, 2);
            p1 = new Pedido(cliente1, 2, false, false, 1, Constantes.ZONA_STANDARD);
            p2 = new Pedido(cliente2, 1, false, false, 1, Constantes.ZONA_STANDARD);

            e.agregarChofer(chofer1);
            e.agregarChofer(chofer2);
            e.agregarVehiculo(autito);
            e.agregarVehiculo(moto);
            e.agregarVehiculo(moto2);
            e.agregarPedido(p1);
            e.agregarPedido(p2);
        } catch (Exception e) {}
    }

    @Test
    public void testNuevoViajeSinSeleccionar() {
        controlador.getVista().actualizar();
        robot.delay(GuiTestUtils.getDelay());
        Assert.assertNotEquals("La lista de choferes libres deberia tener al menos un chofer", 0, choferesLibres.getModel().getSize());
        Assert.assertNotEquals("La lista de pedidos pendientes deberia tener al menos un pedido", 0, pedidosPendientes.getModel().getSize());
        Assert.assertEquals("La lista de vehiculo deberia estar vacia hasta que se seleccione un pedido", 0, vehiculosLibres.getModel().getSize());
        Assert.assertFalse("El boton de nuevo viaje deberia estar deshabilitado", nuevoViaje.isEnabled());
    }

    @Test
    public void testNuevoViajeSeleccionarPedidoYChofer() {
        controlador.getVista().actualizar();
        robot.delay(GuiTestUtils.getDelay());
        pedidosPendientes.setSelectedIndex(0);
        Pedido pedidosSeleccionado = pedidosPendientes.getSelectedValue();
        choferesLibres.setSelectedIndex(0);
        robot.delay(GuiTestUtils.getDelay());

        ArrayList<Vehiculo> vehiculosParaPedidoReal = e.vehiculosOrdenadosPorPedido(pedidosSeleccionado);
        ArrayList<Vehiculo> vehiculosParaPedidoVista = new ArrayList<>();

        for (int i = 0; i < vehiculosLibres.getModel().getSize(); i++) {
            vehiculosParaPedidoVista.add(vehiculosLibres.getModel().getElementAt(i));
        }
        Assert.assertEquals("La lista de vehiculos disponibles deberia tener los vehiculos disponibles", vehiculosParaPedidoReal, vehiculosParaPedidoVista);
    }

    @Test
    public void testNuevoViajeSeleccionarTodo() {
        controlador.getVista().actualizar();
        robot.delay(GuiTestUtils.getDelay());
        try {
            pedidosPendientes.setSelectedIndex(0);
            choferesLibres.setSelectedIndex(0);
            vehiculosLibres.setSelectedIndex(0);
            robot.delay(GuiTestUtils.getDelay());

            Assert.assertTrue("Boton nuevo viaje deberia estar habilitado", nuevoViaje.isEnabled());

            GuiTestUtils.clickComponente(nuevoViaje, robot);

            Assert.assertNotEquals("El pedido no deberia estar en la lista de pedidos pendientes", 2, pedidosPendientes.getModel().getSize());
            Assert.assertNotEquals("El chofer no deberia estar en la lista de choferes libres", 2, choferesLibres.getModel().getSize());
            Assert.assertEquals("La lista de vehiculos disponibles deberia estar vacia", 0, vehiculosLibres.getModel().getSize());

            Assert.assertFalse("El boton de nuevo viaje deberia estar deshabilitado", nuevoViaje.isEnabled());

        } catch(Exception e) {}
    }

    @After
    public void tearDown() {
        JFrame ventana = (JFrame) controlador.getVista();
        ventana.setVisible(false);
    }
}
