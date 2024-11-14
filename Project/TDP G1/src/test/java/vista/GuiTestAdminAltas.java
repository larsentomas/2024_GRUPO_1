package vista;

import controlador.Controlador;
import excepciones.ChoferRepetidoException;
import excepciones.VehiculoRepetidoException;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;
import util.Mensajes;

import javax.swing.*;
import java.awt.*;

public class GuiTestAdminAltas {

    Robot robot;
    Controlador controlador;
    Empresa e;

    JTextField dni;
    JTextField nombreChofer;
    JTextField cantidadHijos;
    JTextField anioIngreso;
    JRadioButton permanente;
    JRadioButton temporario;
    JButton nuevoChofer;

    JTextField patenteVehiculo;
    JTextField cantPlazas;
    JRadioButton auto;
    JRadioButton combi;
    JRadioButton moto;
    JCheckBox mascota;
    JButton nuevoVehiculo;
    JList<Chofer> choferesLibres;

    JList<Chofer> choferes;

    JList<Cliente> clientes;
    JList<Vehiculo> vehiculos;

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
            dni = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
            nombreChofer = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
            cantidadHijos = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);
            anioIngreso = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CH_ANIO);
            permanente = (JRadioButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PERMANENTE);
            temporario = (JRadioButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.TEMPORARIO);
            nuevoChofer = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
            patenteVehiculo = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.PATENTE);
            cantPlazas = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
            auto = (JRadioButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.AUTO);
            combi = (JRadioButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.COMBI);
            moto = (JRadioButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.MOTO);
            mascota = (JCheckBox) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CHECK_VEHICULO_ACEPTA_MASCOTA);
            nuevoVehiculo = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
            choferesLibres = (JList<Chofer>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_CHOFERES_LIBRES);
            choferes = (JList<Chofer>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_CHOFERES_TOTALES);
            clientes = (JList<Cliente>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTADO_DE_CLIENTES);
            vehiculos = (JList<Vehiculo>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_TOTALES);

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
    public void testRegistroChoferValido() {
        int cantChoferesLibres = choferesLibres.getModel().getSize();
        int cantChoferes = choferes.getModel().getSize();

        GuiTestUtils.cargarJTextField(dni, "11111111", robot);
        GuiTestUtils.cargarJTextField(nombreChofer, "Pepe", robot);
        GuiTestUtils.cargarJTextField(cantidadHijos, "2", robot);
        GuiTestUtils.cargarJTextField(anioIngreso, "2000", robot);
        GuiTestUtils.clickComponente(permanente, robot);
        GuiTestUtils.clickComponente(nuevoChofer, robot);

        Assert.assertEquals("Deberia haber un chofer mas en la lista de choferes libres", cantChoferesLibres + 1, choferesLibres.getModel().getSize());
        Assert.assertEquals("Deberia haber un chofer mas en la lista de choferes", cantChoferes + 1, choferes.getModel().getSize());

        Assert.assertTrue("El campo DNI deberia estar vacio", dni.getText().isEmpty());
        Assert.assertTrue("El campo nombre deberia estar vacio", nombreChofer.getText().isEmpty());
        Assert.assertTrue("El campo cantidad de hijos deberia estar vacio", cantidadHijos.getText().isEmpty());
        Assert.assertTrue("El campo anio de ingreso deberia estar vacio", anioIngreso.getText().isEmpty());
    }

    @Test
    public void testRegistroChoferInvalido() {
        try {
            e.agregarChofer(new ChoferTemporario("22222222", "Ramona"));
            controlador.getVista().actualizar();

            int cantChoferesLibres = choferesLibres.getModel().getSize();
            int cantChoferes = choferes.getModel().getSize();

            GuiTestUtils.cargarJTextField(dni, "22222222", robot);
            GuiTestUtils.cargarJTextField(nombreChofer, "Juancito", robot);
            GuiTestUtils.clickComponente(temporario, robot);
            GuiTestUtils.clickComponente(nuevoChofer, robot);

            robot.delay(GuiTestUtils.getDelay());

            Assert.assertEquals("Deberia haber la misma cantidad de choferes libres", cantChoferesLibres, choferesLibres.getModel().getSize());
            Assert.assertEquals("Deberia haber la misma cantidad de choferes", cantChoferes, choferes.getModel().getSize());

            Assert.assertEquals("El mensaje es incorrecto", Mensajes.CHOFER_YA_REGISTRADO.getValor(), panel.getMessage());
        } catch(ChoferRepetidoException e) {}
    }

    @Test
    public void testRegistroVehiculoValido() {
        int cantVehiculos = vehiculos.getModel().getSize();

        GuiTestUtils.cargarJTextField(patenteVehiculo, "AAA111", robot);
        GuiTestUtils.cargarJTextField(cantPlazas, "4", robot);
        GuiTestUtils.clickComponente(auto, robot);
        GuiTestUtils.clickComponente(mascota, robot);
        GuiTestUtils.clickComponente(nuevoVehiculo, robot);

        Assert.assertEquals("Deberia haber un vehiculo mas en la lista de vehiculos", cantVehiculos + 1, vehiculos.getModel().getSize());
        Assert.assertTrue("El campo patente deberia estar vacio", patenteVehiculo.getText().isEmpty());
        Assert.assertTrue("El campo cantidad de plazas deberia estar vacio", cantPlazas.getText().isEmpty());
        Assert.assertFalse("El campo mascota deberia estar deseleccionado", mascota.isSelected());
    }

    @Test
    public void testRegistroVehiculoInvalido() {
        try {
            e.agregarVehiculo(new Auto("BBB111", 4, true));
            controlador.getVista().actualizar();
            int cantVehiculos = vehiculos.getModel().getSize();

            GuiTestUtils.cargarJTextField(patenteVehiculo, "BBB111", robot);
            GuiTestUtils.clickComponente(moto, robot);
            GuiTestUtils.clickComponente(nuevoVehiculo, robot);

            robot.delay(GuiTestUtils.getDelay());
            Assert.assertEquals("Deberia haber la misma cantidad de vehiculos", cantVehiculos, vehiculos.getModel().getSize());
            Assert.assertEquals("El mensaje es incorrecto", Mensajes.VEHICULO_YA_REGISTRADO.getValor(), panel.getMessage());
        } catch(VehiculoRepetidoException e) {}
    }

}
