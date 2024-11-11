package modelo.dato;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

import modeloDatos.Auto;
import modeloDatos.Cliente;
import modeloDatos.Pedido;

public class TestAuto {
    Auto auto;
    Cliente cliente1;

    @Before
    public void setUp() {
        auto = new Auto("AAA000", 4, false);
        cliente1 = new Cliente("franveron", "mandarina123", "Francisco Veron");
    }

    @Test
    public void testAutoPatenteLarga() {
        Assert.assertEquals(auto.getPatente(), "AAA000");
        Assert.assertEquals(auto.getCantidadPlazas(), 4);
        Assert.assertFalse(auto.isMascota());
    }

    @Test
    public void testAutoPatenteVacia() {
        Auto auto1 = new Auto("", 1, true);
        Assert.assertEquals(auto1.getPatente(), "");
        Assert.assertEquals(auto1.getCantidadPlazas(), 1);
        Assert.assertTrue(auto1.isMascota());
    }

    @Test
    public void testgetPuntajePedidoConBaul() {
        Pedido pedido2 = new Pedido(cliente1, 2, false, true, 6, Constantes.ZONA_STANDARD );

        Assert.assertEquals((Integer) 80, auto.getPuntajePedido(pedido2));
    }

    @Test
    public void testgetPuntajePedidoSinBaul() {
        Pedido pedido1 = new Pedido(cliente1, 2, false, false, 6, Constantes.ZONA_STANDARD );

        Assert.assertEquals((Integer) 60, auto.getPuntajePedido(pedido1)); // TODO: Incumple el contrato, devuelve 80 no tiene en cuenta el baul
    }

    @Test
    public void testgetPuntajePedidoInvalido() {
        Pedido pedido3 = new Pedido(cliente1, 5, false, false, 6, Constantes.ZONA_STANDARD );

        Assert.assertNull(auto.getPuntajePedido(pedido3));
    }

}