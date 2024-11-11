package vista;

import controlador.Controlador;
import excepciones.UsuarioYaExisteException;
import excepciones.VehiculoRepetidoException;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GuiTestAdminListados {

    Robot robot;
    Controlador controlador;
    Empresa e;
    JList<Chofer> choferesLibres;

    JTextField totalSueldos;

    JList<Chofer> choferes;
    JList<Viaje> viajesChofer;
    JTextField puntajeChofer;
    JTextField sueldoChofer;

    JList<Cliente> clientes;
    JList<Vehiculo> vehiculos;
    JList<Viaje> viajesHistoricos;

    Cliente cliente;

    FalsoOptionPane panel = new FalsoOptionPane();

    @Before
    public void setUp() {
        try {
            e = Empresa.getInstance();

            robot = new Robot();
            controlador = new Controlador();
            controlador.getVista().setOptionPane(panel);

            // Login admin
            JTextField nombre_usuario = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
            JTextField password = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PASSWORD);
            JButton login = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LOGIN);

            GuiTestUtils.cargarJTextField(nombre_usuario, "admin", robot);
            GuiTestUtils.cargarJTextField(password, "admin", robot);
            GuiTestUtils.clickComponente(login, robot);
            robot.delay(GuiTestUtils.getDelay());

            robot.delay(GuiTestUtils.getDelay());
            choferesLibres = (JList<Chofer>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_CHOFERES_LIBRES);
            totalSueldos = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.TOTAL_SUELDOS_A_PAGAR);
            choferes = (JList<Chofer>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_CHOFERES_TOTALES);
            viajesChofer = (JList<Viaje>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_VIAJES_DE_CHOFER);
            puntajeChofer = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CALIFICACION_CHOFER);
            sueldoChofer = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.SUELDO_DE_CHOFER);
            clientes = (JList<Cliente>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTADO_DE_CLIENTES);
            vehiculos = (JList<Vehiculo>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_TOTALES);
            viajesHistoricos = (JList<Viaje>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_VIAJES_HISTORICOS);

            // Reinicio empresa
            e.getVehiculos().clear();
            e.getChoferes().clear();
            e.getPedidos().clear();
            e.getViajesIniciados().clear();
            e.getViajesTerminados().clear();
            e.getClientes().clear();
            controlador.getVista().actualizar();
            robot.delay(GuiTestUtils.getDelay());
        } catch (AWTException e) {}
    }

    @Test
    public void testListaClientes() {
        try {
            Assert.assertEquals("La lista de clientes deberia estar vacia", 0, clientes.getModel().getSize());

            e.agregarCliente("pepito", "1234", "Pepe Ramon");
            controlador.getVista().actualizar();

            ArrayList<Cliente> clientesReales = new ArrayList<>(e.getClientes().values());
            ArrayList<Cliente> clientesVista = new ArrayList<>();
            for (int i = 0; i < clientes.getModel().getSize(); i++) {
                clientesVista.add(clientes.getModel().getElementAt(i));
            }

            Assert.assertEquals("La lista de clientes es incorrecta",clientesReales, clientesVista);
            Assert.assertEquals("La lista de clientes deberia tener un cliente", 1, clientes.getModel().getSize());
        } catch (UsuarioYaExisteException ex) {}
    }

    @Test
    public void testListaVehiculos() {
        try {
            Assert.assertEquals("La lista de vehiculos deberia estar vacia", 0, vehiculos.getModel().getSize());

            e.agregarVehiculo(new Moto("WWW234"));
            controlador.getVista().actualizar();

            ArrayList<Vehiculo> vehiculosReales = new ArrayList<>(e.getVehiculos().values());
            ArrayList<Vehiculo> vehiculosVista = new ArrayList<>();
            for (int i = 0; i < vehiculos.getModel().getSize(); i++) {
                vehiculosVista.add(vehiculos.getModel().getElementAt(i));
            }

            Assert.assertEquals("La lista de vehiculos no es correcta", vehiculosReales, vehiculosVista);
            Assert.assertEquals("La lista de vehiculos deberia tener un vehiculo", 1, vehiculos.getModel().getSize());
        } catch (VehiculoRepetidoException e) {}
    }

    @Test
    public void testListaViajesHistoricos() {
        try {
            Assert.assertEquals("La lista de viajes historicos deberia estar vacia", 0, viajesHistoricos.getModel().getSize());

            e.agregarCliente("pepito", "1234", "Pepe Ramon");
            cliente = e.getClientes().get("pepito");
            Pedido p = new Pedido(cliente, 2, false, false, 1, Constantes.ZONA_STANDARD);
            Vehiculo autito = new Auto("AAA123", 4, true);
            Chofer chofer = new ChoferPermanente("12345678", "Pepe", 2000, 2);

            e.agregarChofer(chofer);
            e.agregarVehiculo(autito);
            e.agregarPedido(p);

            e.crearViaje(p, chofer, autito);
            e.pagarYFinalizarViaje(5);

            controlador.getVista().actualizar();

            ArrayList<Viaje> viajeshistoricosReales = e.getViajesTerminados();
            ArrayList<Viaje> viajesHistoricosVista = new ArrayList<>();
            for (int i = 0; i < viajesHistoricos.getModel().getSize(); i++) {
                viajesHistoricosVista.add(viajesHistoricos.getModel().getElementAt(i));
            }
            Assert.assertEquals("La lista de viajes historicos no es correcta", viajeshistoricosReales, viajesHistoricosVista);
            Assert.assertEquals("La lista de viajes historicos deberia tener un viaje", 1, viajesHistoricos.getModel().getSize());

        } catch (Exception e) {}
    }

}
