package vista;

import controlador.Controlador;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GuiTestAdminGestionChoferes {
    Robot robot;
    Controlador controlador;
    Empresa e;

    JList<Chofer> choferesLibres;

    JList<Chofer> choferes;
    JList<Viaje> viajesChofer;
    JTextField puntajeChofer;
    JTextField sueldoChofer;

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
            choferes = (JList<Chofer>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_CHOFERES_TOTALES);
            viajesChofer = (JList<Viaje>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_VIAJES_DE_CHOFER);
            puntajeChofer = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CALIFICACION_CHOFER);
            sueldoChofer = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.SUELDO_DE_CHOFER);

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
    public void testListaChoferesTotalesConChofer() {
        try {
            e.agregarCliente("pepito", "1234", "Pepe Ramon");
            Cliente cliente = e.getClientes().get("pepito");
            Pedido p = new Pedido(cliente, 2, false, false, 1, Constantes.ZONA_STANDARD);
            Vehiculo autito = new Auto("AAA123", 4, true);
            Chofer chofer = new ChoferPermanente("12345678", "Pepe", 2000, 2);

            e.agregarChofer(chofer);
            e.agregarVehiculo(autito);
            e.agregarPedido(p);

            e.crearViaje(p, chofer, autito);
            e.pagarYFinalizarViaje(5);

            controlador.getVista().actualizar();

            ArrayList<Chofer> choferesReales = new ArrayList<>(e.getChoferes().values());
            ArrayList<Chofer> choferesVista = new ArrayList<>();
            for (int i = 0; i < choferes.getModel().getSize(); i++) {
                choferesVista.add(choferes.getModel().getElementAt(i));
            }

            Assert.assertEquals("Lista de choferes totales es incorrecta", choferesReales, choferesVista);
            Assert.assertEquals("La lista de viajes del chofer deberia estar vacia", 0, viajesChofer.getModel().getSize());
            Assert.assertTrue("La calificacion del chofer deberia estar vacia", puntajeChofer.getText().isEmpty());
            Assert.assertTrue("El sueldo del chofer no deberia tener un valor", sueldoChofer.getText().isEmpty());

            // selecciono el chofer que acabo de crear
            choferes.setSelectedIndex(0);

            ArrayList<Viaje> viajesReales = e.getHistorialViajeChofer(chofer);
            ArrayList<Viaje> viajesVista = new ArrayList<>();

            Assert.assertNotEquals("La lista de choferes es incorrecta", viajesReales, viajesVista);
            Assert.assertFalse("La calificacion del chofer deberia tener el puntaje", puntajeChofer.getText().isEmpty());

            // Comparacion de doubles
            String sueldoRondeado = String.format("%.2f", chofer.getSueldoNeto());
            Assert.assertEquals("El sueldo del chofer es incorrecto", sueldoRondeado, sueldoChofer.getText());

        } catch (Exception ex) {}
    }

    @Test
    public void testListaChoferesSinChofer() {
        Assert.assertEquals("La lista de viajes del chofer deberia estar vacia", 0, viajesChofer.getModel().getSize());
        Assert.assertEquals("La lista de choferes deberia estar vacia", 0, choferes.getModel().getSize());
        Assert.assertTrue("La calificacion de chofer", puntajeChofer.getText().isEmpty());
        Assert.assertTrue("El sueldo del chofer no deberia tener un valor", sueldoChofer.getText().isEmpty());
    }

}
