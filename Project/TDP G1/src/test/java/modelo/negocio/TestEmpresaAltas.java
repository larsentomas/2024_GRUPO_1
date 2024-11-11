package modelo.negocio;

import excepciones.ChoferRepetidoException;
import excepciones.UsuarioYaExisteException;
import excepciones.VehiculoRepetidoException;
import modeloDatos.Auto;
import modeloDatos.ChoferPermanente;
import modeloDatos.Cliente;
import modeloDatos.Pedido;
import modeloNegocio.Empresa;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Constantes;
import util.Mensajes;

public class TestEmpresaAltas {
    static Empresa emp;

    @BeforeClass
    public static void setUp() throws UsuarioYaExisteException {
        emp = Empresa.getInstance();

    }

    @Test
    public void testAgregarChofer() {
        ChoferPermanente chofer = new ChoferPermanente("47859632", "Mateo", 2020, 4);
        try {
            emp.agregarChofer(chofer);
            Assert.assertTrue(emp.getChoferes().containsValue(chofer));

            emp.agregarChofer(chofer);
            Assert.fail("Deberia haber lanzado la excepcion ChoferRepetidoException");
        } catch (ChoferRepetidoException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.CHOFER_YA_REGISTRADO.getValor());
            Assert.assertEquals(e.getDniPrentendido(), chofer.getDni());
            Assert.assertEquals(e.getChoferExistente(), emp.getChoferes().get(chofer.getDni()));
        }
    }

    @Test
    public void testAgregarCliente() {
        try {
            emp.agregarCliente("pepe123", "mandarina123", "Pedro");
            Assert.assertTrue(emp.getClientes().containsKey("pepe123"));

            emp.agregarCliente("pepe123", "mandarina123", "Pedro");
            Assert.fail("Deberia haber lanzado la excepcion UsuarioYaExisteException");
        } catch (UsuarioYaExisteException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.USUARIO_REPETIDO.getValor());
            Assert.assertEquals(e.getUsuarioPretendido(), "pepe123");
        }
    }

    @Test
    public void testAgregarVehiculo() {
        Auto auto = new Auto("AAA123", 4, false);
        try {
            emp.agregarVehiculo(auto);
            Assert.assertTrue(emp.getVehiculos().containsValue(auto));

            emp.agregarVehiculo(auto);
            Assert.fail("Deberia haber arrojado excepcion VehiculoRepetidoException");
        } catch (VehiculoRepetidoException e) {
            Assert.assertEquals(e.getMessage(), Mensajes.VEHICULO_YA_REGISTRADO.getValor());
            Assert.assertEquals(e.getVehiculoExistente(), auto);
            Assert.assertEquals(e.getPatentePrentendida(), auto.getPatente());
        }
    }
}
