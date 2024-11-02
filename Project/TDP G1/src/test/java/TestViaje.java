
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

public class TestViaje {

    Cliente cliente;
    Pedido pedido;
    Auto auto;
    ChoferPermanente chofer;
    Viaje viaje;

    @Before
    public void setUp() {
        cliente = new Cliente("franveron", "mandarina123", "Francisco Veron");
        pedido = new Pedido(cliente, 2, false, true, 6, Constantes.ZONA_STANDARD);
        auto = new Auto("AAA123", 4, true);
        chofer = new ChoferPermanente("47859632", "Mateo", 2020, 4);
        viaje = new Viaje(pedido, chofer, auto);
    }

    @Test
    public void testViaje() {
        Assert.assertEquals(viaje.getPedido(), pedido);
        Assert.assertEquals(viaje.getChofer(), chofer);
        Assert.assertEquals(viaje.getVehiculo(), auto);
    }

    @Test
    public void testFinalizarViajeLimiteInferior() {
        viaje.finalizarViaje(0);
        Assert.assertEquals(0, viaje.getCalificacion());
        Assert.assertTrue(viaje.isFinalizado());
    }

    @Test
    public void testFinalizarViajeLimiteSuperior() {
        Viaje viaje2 = new Viaje(pedido, chofer, auto);
        viaje2.finalizarViaje(5);
        Assert.assertEquals(5, viaje2.getCalificacion());
        Assert.assertTrue(viaje2.isFinalizado());
    }

    @Test
    public void testSetValorBase() {
        Viaje.setValorBase(1);
        Assert.assertEquals(1, Viaje.getValorBase(), 0.0000000001);

        Viaje.setValorBase(102548526.25);
        Assert.assertEquals(102548526.25, Viaje.getValorBase(), 0.0000000001);
    }

    @Test
    public void testGetValorZonaStandar() {
        Viaje.setValorBase(1000);
        Pedido pedido1 = new Pedido(cliente, 1, false, false, 1, Constantes.ZONA_STANDARD);
        Viaje viaje = new Viaje(pedido1, chofer, auto);
        Assert.assertEquals(1200, viaje.getValor(), 0.0000000001); // TODO: Falla

        pedido1 = new Pedido(cliente, 1, true, true, 1, Constantes.ZONA_STANDARD);
        viaje = new Viaje(pedido1, chofer, auto);
        Assert.assertEquals(1650, viaje.getValor(), 0.0000000001); // TODO: Falla
    }

    @Test
    public void testGetValorZonaSinAsfaltar() {
        Viaje.setValorBase(1000);

        Pedido pedido1 = new Pedido(cliente, 2, false, false, 4, Constantes.ZONA_SIN_ASFALTAR);
        viaje = new Viaje(pedido1, chofer, auto);
        Assert.assertEquals(2000, viaje.getValor(), 0.0000000001);

        Pedido pedido2 = new Pedido(cliente, 2, false, false, 4, Constantes.ZONA_SIN_ASFALTAR);
        viaje = new Viaje(pedido2, chofer, auto);
        Assert.assertEquals(3400, viaje.getValor(), 0.0000000001); // TODO: Falla
    }

    @Test
    public void testGetValorZonaPeligrosa() {
        Viaje.setValorBase(1000);
        Pedido pedido1 = new Pedido(cliente, 2, false, false, 5, Constantes.ZONA_PELIGROSA);
        viaje = new Viaje(pedido1, chofer, auto);
        Assert.assertEquals(2200, viaje.getValor(), 0.0000000001);

        Pedido pedido2 = new Pedido(cliente, 2, true, true, 5, Constantes.ZONA_PELIGROSA);
        viaje = new Viaje(pedido2, chofer, auto);
        Assert.assertEquals(3850, viaje.getValor(), 0.0000000001); // TODO: Falla
    }

}