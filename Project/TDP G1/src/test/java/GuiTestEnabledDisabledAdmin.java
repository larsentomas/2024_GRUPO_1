import java.awt.*;
import controlador.Controlador;
import excepciones.UsuarioYaExisteException;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.After;
import org.junit.Assert;
import util.Constantes;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

public class GuiTestEnabledDisabledAdmin {

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


            // Reinicio empresa
            e.getVehiculos().clear();
            e.getChoferes().clear();
            e.getPedidos().clear();
            e.getViajesIniciados().clear();
            e.getViajesTerminados().clear();

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
    public void testCamposVacios() {
        robot.delay(GuiTestUtils.getDelay());
        // Deberian estar todos los text field vacios
        Assert.assertTrue("Campos dni deberia estar vacio", dni.getText().isEmpty());
        Assert.assertTrue("Campo nombre chofer deberia estar vacio", nombreChofer.getText().isEmpty());
        Assert.assertTrue("Campo cantidad de hijos deberia estar vacio", cantidadHijos.getText().isEmpty());
        Assert.assertTrue("Campo anio de ingreso deberia estar vacio", anioIngreso.getText().isEmpty());
        Assert.assertTrue("Campo patente deberia estar vacio", patenteVehiculo.getText().isEmpty());
        Assert.assertTrue("Campo cantidad de plazas deberia estar vacio", cantPlazas.getText().isEmpty());
        Assert.assertTrue("Campo puntaje de chofer deberia estar vacio", puntajeChofer.getText().isEmpty());
        Assert.assertTrue("Campo sueldo de chofer deberia estar vacio", sueldoChofer.getText().isEmpty());
 }

    @Test
    public void testRegistroChoferTemporario() {
        robot.delay(GuiTestUtils.getDelay());

        // Marco como temporario
        GuiTestUtils.clickComponente(temporario, robot);
        robot.delay(GuiTestUtils.getDelay());

        // Caso 1: Todo vacio, INVALIDO
        Assert.assertFalse("Nuevo chofer deberia estar deshabilitado", nuevoChofer.isEnabled());

        // Caso 2: Solo el DNI, INVALIDO
        GuiTestUtils.cargarJTextField(dni, "a", robot);
        Assert.assertFalse("Nuevo chofer deberia estar deshabilitado", nuevoChofer.isEnabled());

        // Caso 3: Dni y nombre, VALIDO
        GuiTestUtils.cargarJTextField(nombreChofer, "a", robot);
        Assert.assertTrue("Nuevo chofer deberia estar habilitado", nuevoChofer.isEnabled());

        // Caso 4: Solo el nombre, INVALIDO
        GuiTestUtils.limpiarYCargar(dni, "", robot);
        Assert.assertFalse("Nuevo chofer deberia estar deshabilitado", nuevoChofer.isEnabled());
    }

    @Test
    public void testRegistroChoferPermanente() {
        robot.delay(GuiTestUtils.getDelay());

        // Marco como permanente
        GuiTestUtils.clickComponente(permanente, robot);
        robot.delay(GuiTestUtils.getDelay());

        // Caso 1: Todo vacio INVALIDO
        Assert.assertFalse("Nuevo chofer deberia estar deshabilitado", nuevoChofer.isEnabled());

        // Caso 2: Todo valido, limite inferior de cantidad hijos y anio ingreso
        GuiTestUtils.cargarJTextField(dni, "a", robot);
        GuiTestUtils.cargarJTextField(nombreChofer, "a", robot);
        GuiTestUtils.cargarJTextField(cantidadHijos, "0", robot);
        GuiTestUtils.cargarJTextField(anioIngreso, "1900", robot);
        Assert.assertTrue("Nuevo chofer deberia estar habilitado", nuevoChofer.isEnabled());

        // Caso 3: Todo valido, limite superior de anio ingreso
        GuiTestUtils.limpiarYCargar(anioIngreso, "3000", robot);
        Assert.assertTrue("Nuevo chofer deberia estar habilitado", nuevoChofer.isEnabled());

        // Caso 4: Todo bien menos el dni (vacio) INVALIDO
        GuiTestUtils.limpiarYCargar(dni, "", robot);
        Assert.assertFalse("Nuevo chofer deberia estar deshabilitado", nuevoChofer.isEnabled());

        // Caso 5: Todo bien menos el nombre (vacio), INVALIDO
        GuiTestUtils.limpiarYCargar(dni, "a", robot);
        GuiTestUtils.limpiarYCargar(nombreChofer, "", robot);
        Assert.assertFalse("Nuevo chofer deberia estar deshabilitado", nuevoChofer.isEnabled());

        // Caso 6: Todo bien menos la cantidad de hijos (negativo), INVALIDO
        GuiTestUtils.limpiarYCargar(nombreChofer, "a", robot);
        GuiTestUtils.limpiarYCargar(cantidadHijos, "-1", robot);
        Assert.assertFalse("Nuevo chofer deberia estar deshabilitado", nuevoChofer.isEnabled());

        // Caso 7: Todo bien menos anio de ingreso (menor al limite inferior), INVALIDO
        GuiTestUtils.limpiarYCargar(cantidadHijos, "0", robot);
        GuiTestUtils.limpiarYCargar(anioIngreso, "1899", robot);
        Assert.assertFalse("Nuevo chofer deberia estar deshabilitado", nuevoChofer.isEnabled());

        // Caso 8: Todo bien menos anio de ingreso (mayor al limite superior), INVALIDO
        GuiTestUtils.limpiarYCargar(anioIngreso, "3001", robot);
        Assert.assertFalse("Nuevo chofer deberia estar deshabilitado", nuevoChofer.isEnabled());
    }

    @Test
    public void testRegistroMoto() {
        robot.delay(GuiTestUtils.getDelay());

        Assert.assertFalse("Nuevo vehiculo deberia estar deshabilitado", nuevoVehiculo.isEnabled());

        // Marco como moto
        GuiTestUtils.clickComponente(moto, robot);
        robot.delay(GuiTestUtils.getDelay());

        // Caso 1: Todo vacio, INVALIDO
        Assert.assertFalse("Nuevo vehiculo deberia estar deshabilitado", nuevoVehiculo.isEnabled());

        // Caso 2: Solo la patente, VALIDO
        GuiTestUtils.cargarJTextField(patenteVehiculo, "a", robot);
        Assert.assertTrue("Nuevo vehiculo deberia estar habilitado", nuevoVehiculo.isEnabled());
    }

    @Test
    public void testRegistroAuto() {
        robot.delay(GuiTestUtils.getDelay());

        Assert.assertFalse("Nuevo vehiculo deberia estar deshabilitado", nuevoVehiculo.isEnabled());

        // Marco como auto
        GuiTestUtils.clickComponente(auto, robot);
        robot.delay(GuiTestUtils.getDelay());

        // Caso 1: Todo vacio, INVALIDO
        Assert.assertFalse("Nuevo vehiculo deberia estar deshabilitado", nuevoVehiculo.isEnabled());

        // Caso 2: Todo valido, limite inferior de cantidad de plazas
        GuiTestUtils.cargarJTextField(patenteVehiculo, "a", robot);
        GuiTestUtils.cargarJTextField(cantPlazas, "1", robot);
        Assert.assertTrue("Nuevo vehiculo deberia estar habilitado", nuevoVehiculo.isEnabled());

        // Caso 3: Todo valido, limite superior de cantidad de plazas
        GuiTestUtils.limpiarYCargar(cantPlazas, "4", robot);
        Assert.assertTrue("Nuevo vehiculo deberia estar habilitado", nuevoVehiculo.isEnabled());

        // Caso 4: Todo bien menos la patente (vacia), INVALIDO
        GuiTestUtils.limpiarYCargar(patenteVehiculo, "", robot);
        Assert.assertFalse("Nuevo vehiculo deberia estar deshabilitado", nuevoVehiculo.isEnabled());

        // Caso 5: Todo bien menos la cantidad de plazas (menor a limite inferior), INVALIDO
        GuiTestUtils.limpiarYCargar(patenteVehiculo, "a", robot);
        GuiTestUtils.limpiarYCargar(cantPlazas, "0", robot);
        Assert.assertFalse("Nuevo vehiculo deberia estar deshabilitado", nuevoVehiculo.isEnabled()); // TODO: Salta error, incumple el contrato,no deberia permitir 0 plazas

        // Caso 6: Todo bien menos la cantidad de plazas (mayor a limite superior), INVALIDO
        GuiTestUtils.limpiarYCargar(cantPlazas, "5", robot);
        Assert.assertFalse("Nuevo vehiculo deberia estar deshabilitado", nuevoVehiculo.isEnabled());
    }

    @Test
    public void testRegistroCombi() {
        robot.delay(GuiTestUtils.getDelay());

        Assert.assertFalse("Nuevo vehiculo deberia estar deshabilitado", nuevoVehiculo.isEnabled());

        // Marco como combi
        GuiTestUtils.clickComponente(combi, robot);
        robot.delay(GuiTestUtils.getDelay());

        // Caso 1: Todo vacio, INVALIDO
        Assert.assertFalse("Nuevo vehiculo deberia estar deshabilitado", nuevoVehiculo.isEnabled());

        // Caso 2: Todo valido, limite inferior de cantidad de plazas
        GuiTestUtils.cargarJTextField(patenteVehiculo, "a", robot);
        GuiTestUtils.cargarJTextField(cantPlazas, "5", robot);
        Assert.assertTrue("Nuevo vehiculo deberia estar habilitado", nuevoVehiculo.isEnabled());

        // Caso 3: Todo valido, limite superior de cantidad de plazas
        GuiTestUtils.limpiarYCargar(cantPlazas, "10", robot);
        Assert.assertTrue("Nuevo vehiculo deberia estar habilitado", nuevoVehiculo.isEnabled());

        // Caso 4: Todo bien menos la patente (vacia), INVALIDO
        GuiTestUtils.limpiarYCargar(patenteVehiculo, "", robot);
        Assert.assertFalse("Nuevo vehiculo deberia estar deshabilitado", nuevoVehiculo.isEnabled());

        // Caso 5: Todo bien menos la cantidad de plazas (menor a limite inferior), INVALIDO
        GuiTestUtils.limpiarYCargar(patenteVehiculo, "a", robot);
        GuiTestUtils.limpiarYCargar(cantPlazas, "4", robot);
        Assert.assertFalse("Nuevo vehiculo deberia estar deshabilitado", nuevoVehiculo.isEnabled());

        // Caso 6: Todo bien menos la cantidad de plazas (mayor a limite superior), INVALIDO
        GuiTestUtils.limpiarYCargar(cantPlazas, "11", robot);
        Assert.assertFalse("Nuevo vehiculo deberia estar deshabilitado", nuevoVehiculo.isEnabled());
    }

    @Test
    public void testGestionPedidos() {
        robot.delay(GuiTestUtils.getDelay());

        // Creo un cliente, pedido, chofer y vehiculo
        String u = "pepito";
        Cliente c;
        Pedido p;
        Chofer ch;
        Vehiculo v;
        try {
            e.agregarCliente(u, "1234", "Pepe Ramon");
            c = e.getClientes().get(u);
            v = new Auto("AAA123", 4, true);
            e.agregarVehiculo(v);
            ch = new ChoferPermanente("12457896", "Pepe", 2000, 2);
            e.agregarChofer(ch);
            p = new Pedido(c, 2, false, false, 1, Constantes.ZONA_STANDARD);
            e.agregarPedido(p);
            controlador.getVista().actualizar();
        }  catch(Exception e) {
            Assert.fail("No deberia haber fallado la creacion de los objetos");
        }


        robot.delay(GuiTestUtils.getDelay());
        Assert.assertEquals("Vehiculos disponibles deberia estar vacio porque no seleccione pedido", 0, vehiculosLibres.getModel().getSize());
        Assert.assertEquals("Pedidos pendientes deberia tener solo el pedido recien agregado", 1, pedidosPendientes.getModel().getSize());
        Assert.assertEquals("Lista de choferes deberia tener solo el chofer recien agregado", 1, choferesLibres.getModel().getSize());
        Assert.assertFalse("Nuevo viaje deberia estar deshabilitado", nuevoViaje.isEnabled());

        // Selecciono un pedido
        pedidosPendientes.setSelectedIndex(0);
        robot.delay(GuiTestUtils.getDelay());

        Assert.assertFalse("Nuevo viaje deberia estar deshabilitado", nuevoViaje.isEnabled());
        Assert.assertEquals("Deberia aparecer solamente el vehiculo que agregue", 1, vehiculosLibres.getModel().getSize());

        // Selecciono chofer y vehiculo
        vehiculosLibres.setSelectedIndex(0);
        robot.delay(GuiTestUtils.getDelay());
        choferesLibres.setSelectedIndex(0);

        Assert.assertTrue("Nuevo viaje deberia estar habilitado", nuevoViaje.isEnabled());
    }

    @After
    public void tearDown() {
        JFrame ventana = (JFrame) controlador.getVista();
        ventana.setVisible(false);
    }

}
