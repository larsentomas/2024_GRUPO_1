import controlador.Controlador;
import excepciones.ChoferRepetidoException;
import excepciones.UsuarioYaExisteException;
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

public class GuiTestAdmin {

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

    JList<Pedido> pedidosPendientes;
    JList<Chofer> choferesLibres;
    JList<Vehiculo> vehiculosLibres;
    JButton nuevoViaje;

    JTextField totalSueldos;

    JList<Chofer> choferes;
    JList<Viaje> viajesChofer;
    JTextField puntajeChofer;
    JTextField sueldoChofer;

    JList<Cliente> clientes;
    JList<Vehiculo> vehiculos;
    JList<Viaje> viajesHistoricos;

    JButton cerrarSesion;

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


            // Reinicio empresa
            e.getVehiculos().clear();
            e.getChoferes().clear();
            e.getPedidos().clear();
            e.getViajesIniciados().clear();
            e.getViajesTerminados().clear();
            e.getClientes().clear();

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
            pedidosPendientes = (JList<Pedido>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_PEDIDOS_PENDIENTES);
            choferesLibres = (JList<Chofer>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_CHOFERES_LIBRES);
            vehiculosLibres = (JList<Vehiculo>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_DISPONIBLES);
            nuevoViaje = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.NUEVO_VIAJE);
            totalSueldos = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.TOTAL_SUELDOS_A_PAGAR);
            choferes = (JList<Chofer>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_CHOFERES_TOTALES);
            viajesChofer = (JList<Viaje>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_VIAJES_DE_CHOFER);
            puntajeChofer = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CALIFICACION_CHOFER);
            sueldoChofer = (JTextField) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.SUELDO_DE_CHOFER);
            clientes = (JList<Cliente>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTADO_DE_CLIENTES);
            vehiculos = (JList<Vehiculo>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_TOTALES);
            viajesHistoricos = (JList<Viaje>) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.LISTA_VIAJES_HISTORICOS);
            cerrarSesion = (JButton) GuiTestUtils.getComponentByName((Component) controlador.getVista(), Constantes.CERRAR_SESION_ADMIN);


        } catch (AWTException e) {}
    }

    @Test
    public void testListaChoferesTotalesConChofer() {
        try {
            e.agregarCliente("pepito", "1234", "Pepe Ramon");
            cliente = e.getClientes().get("pepito");
            Pedido p = new Pedido(cliente, 2, false, false, 1, Constantes.ZONA_STANDARD);
            Vehiculo autito = new Auto("AAA123", 4, true);
            Chofer chofer = new ChoferPermanente("12345678", "Pepe", 2000, 2);

            e.agregarChofer(chofer);
            e.agregarVehiculo(autito);
            e.agregarPedido(p);

            e.crearViaje(p, chofer, autito);
            Viaje viaje = e.getViajesIniciados().get(cliente);

            controlador.getVista().actualizar();

            Assert.assertNotEquals("Lista de choferes totales deberia tener al menos un chofer", 0, choferes.getModel().getSize());
            Assert.assertEquals("La lista de viajes del chofer deberia estar vacia", 0, viajesChofer.getModel().getSize());
            Assert.assertTrue("La calificacion del chofer deberia estar vacia", puntajeChofer.getText().isEmpty());
            Assert.assertTrue("El sueldo del chofer no deberia tener un valor", sueldoChofer.getText().isEmpty());

            // selecciono el chofer que acabo de crear
            choferes.setSelectedIndex(0);

            Assert.assertNotEquals("La lista de choferes deberia contener los viajes del chofer", 0, viajesChofer.getModel().getSize());
            Assert.assertFalse("La calificacion del chofer deberia tener el puntaje", puntajeChofer.getText().isEmpty());

            // Comparacion de doubles
            String sueldoRondeado = String.format("%.2f", chofer.getSueldoNeto());
            Assert.assertEquals("El sueldo del chofer es incorrecto", sueldoRondeado, sueldoChofer.getText());

        } catch (Exception ex) {}
    }

    @Test
    public void testListaChoferesSinChofer() {
        Assert.assertEquals("La lista de choferes deberia estar vacia", 0, choferes.getModel().getSize());
        Assert.assertTrue("La calificacion de chofer", puntajeChofer.getText().isEmpty());
        Assert.assertTrue("El sueldo del chofer no deberia tener un valor", sueldoChofer.getText().isEmpty());
    }

    @Test
    public void testListaClientes() {
        try {
            Assert.assertEquals("La lista de clientes deberia estar vacia", 0, clientes.getModel().getSize());

            e.agregarCliente("pepito", "1234", "Pepe Ramon");
            controlador.getVista().actualizar();

            Assert.assertEquals("La lista de clientes deberia tener un cliente", 1, clientes.getModel().getSize());
        } catch (UsuarioYaExisteException ex) {}
    }

    @Test
    public void testListaVehiculos() {
        try {
            Assert.assertEquals("La lista de vehiculos deberia estar vacia", 0, vehiculos.getModel().getSize());

            e.agregarVehiculo(new Moto("WWW234"));
            controlador.getVista().actualizar();

            Assert.assertEquals("La lista de vehiculos deberia tener un vehiculo", 1, vehiculos.getModel().getSize());
        } catch (VehiculoRepetidoException e) {}
    }

    @Test
    public void testSueldosTotales() {
        try {
            e.agregarChofer(new ChoferPermanente("12345678", "Pepe", 2000, 2));
            e.agregarChofer(new ChoferTemporario("87654321", "Juan"));

            controlador.getVista().actualizar();

            double total = 0.0;
            for (Chofer chofer : e.getChoferes().values()) {
                total += chofer.getSueldoNeto();
            }
            String sueldosRondeado = String.format("%.2f", total);

            Assert.assertEquals("El total de sueldos a pagar es incorrecto", sueldosRondeado, totalSueldos.getText());
        } catch(ChoferRepetidoException e) {}
    }

    @Test
    public void testRegistroChoferValido() {
        GuiTestUtils.cargarJTextField(dni, "12345678", robot);
        GuiTestUtils.cargarJTextField(nombreChofer, "Pepe", robot);
        GuiTestUtils.cargarJTextField(cantidadHijos, "2", robot);
        GuiTestUtils.cargarJTextField(anioIngreso, "2000", robot);
        GuiTestUtils.clickComponente(permanente, robot);
        GuiTestUtils.clickComponente(nuevoChofer, robot);

        Assert.assertEquals("La cantidad de choferes deberia ser 1", 1, e.getChoferes().size());
        Assert.assertEquals("El chofer deberia estar en la lista de choferes", 1, choferes.getModel().getSize());
        Assert.assertTrue("El campo DNI deberia estar vacio", dni.getText().isEmpty());
        Assert.assertTrue("El campo nombre deberia estar vacio", nombreChofer.getText().isEmpty());
        Assert.assertTrue("El campo cantidad de hijos deberia estar vacio", cantidadHijos.getText().isEmpty());
        Assert.assertTrue("El campo anio de ingreso deberia estar vacio", anioIngreso.getText().isEmpty());
    }

    @Test
    public void testRegistroChoferInvalido() {
        try {
            e.agregarChofer(new ChoferTemporario("12345678", "Ramona"));
            controlador.getVista().actualizar();

            GuiTestUtils.cargarJTextField(dni, "12345678", robot);
            GuiTestUtils.cargarJTextField(nombreChofer, "Juancito", robot);
            GuiTestUtils.clickComponente(temporario, robot);
            GuiTestUtils.clickComponente(nuevoChofer, robot);

            robot.delay(GuiTestUtils.getDelay());
            Assert.assertEquals("El mensaje es incorrecto", Mensajes.CHOFER_YA_REGISTRADO.getValor(), panel.getMessage());
        } catch(ChoferRepetidoException e) {}
    }

    @Test
    public void testRegistroVehiculoValido() {
        GuiTestUtils.cargarJTextField(patenteVehiculo, "AAA123", robot);
        GuiTestUtils.cargarJTextField(cantPlazas, "4", robot);
        GuiTestUtils.clickComponente(auto, robot);
        GuiTestUtils.clickComponente(mascota, robot);
        GuiTestUtils.clickComponente(nuevoVehiculo, robot);

        Assert.assertEquals("La cantidad de vehiculos deberia ser 1", 1, e.getVehiculos().size());
        Assert.assertEquals("El vehiculo deberia estar en la lista de vehiculos", 1, vehiculos.getModel().getSize());
        Assert.assertTrue("El campo patente deberia estar vacio", patenteVehiculo.getText().isEmpty());
        Assert.assertTrue("El campo cantidad de plazas deberia estar vacio", cantPlazas.getText().isEmpty());
        Assert.assertFalse("El campo mascota deberia estar deseleccionado", mascota.isSelected());
    }

    @Test
    public void testRegistroVehiculoInvalido() {
        try {
            e.agregarVehiculo(new Auto("AAA123", 4, true));
            controlador.getVista().actualizar();

            GuiTestUtils.cargarJTextField(patenteVehiculo, "AAA123", robot);
            GuiTestUtils.clickComponente(moto, robot);
            GuiTestUtils.clickComponente(nuevoVehiculo, robot);

            robot.delay(GuiTestUtils.getDelay());
            Assert.assertEquals("El mensaje es incorrecto", Mensajes.VEHICULO_YA_REGISTRADO.getValor(), panel.getMessage());
        } catch(VehiculoRepetidoException e) {}
    }

    @Test
    public void testNuevoViaje() {
        try {
            e.agregarCliente("pepito", "1234", "Pepe Ramon");
            cliente = e.getClientes().get("pepito");
            Pedido p = new Pedido(cliente, 2, false, false, 1, Constantes.ZONA_STANDARD);
            Vehiculo autito = new Auto("AAA123", 4, true);
            Chofer chofer = new ChoferPermanente("12345678", "Pepe", 2000, 2);

            e.agregarChofer(chofer);
            e.agregarVehiculo(autito);
            e.agregarPedido(p);

            pedidosPendientes.setSelectedIndex(0);
            choferesLibres.setSelectedIndex(0);
            vehiculosLibres.setSelectedIndex(0);

            GuiTestUtils.clickComponente(nuevoViaje, robot);

            Assert.assertEquals("El pedido no deberia estar en la lista de pedidos pendientes", 0, pedidosPendientes.getModel().getSize());
            Assert.assertEquals("El chofer no deberia estar en la lista de choferes libres", 0, choferesLibres.getModel().getSize());
            Assert.assertEquals("La lista de vehiculos disponibles deberia estar vacia", 0, vehiculosLibres.getModel().getSize());

            Assert.assertFalse("El boton de nuevo viaje deberia estar deshabilitado", nuevoViaje.isEnabled());

        } catch(Exception e) {}
    }
}
