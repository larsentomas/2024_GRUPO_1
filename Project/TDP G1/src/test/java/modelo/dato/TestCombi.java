package modelo.dato;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

import modeloDatos.Combi;
import modeloDatos.Cliente;
import modeloDatos.Pedido;

public class TestCombi {

    Combi combi;
    Cliente cliente;

    @Before
    public void setUp() {
        combi = new Combi("AAA123", 9, false);
        cliente = new Cliente("franveron", "mandarina123", "Francisco Veron");
    }

    @Test
    public void testCombi() {
        Combi combi1 = new Combi("", 5, true);
        Assert.assertEquals(combi1.getPatente(), "");
        Assert.assertEquals(combi1.getCantidadPlazas(), 5);
        Assert.assertTrue(combi1.isMascota());

        Combi combi2 = new Combi("BBB000", 10, true);
        Assert.assertEquals(combi2.getPatente(), "BBB000");
        Assert.assertEquals(combi2.getCantidadPlazas(), 10);
        Assert.assertTrue(combi2.isMascota());
    }

    @Test
    public void testgetPuntajePedidoConBaulMascota() {
        Pedido pedido3 = new Pedido(cliente, 5, true, true, 6, Constantes.ZONA_SIN_ASFALTAR );
        Assert.assertEquals((Integer) 50, combi.getPuntajePedido(pedido3));
    }

    @Test
    public void testgetPuntajePedidoSinBaulMascota() {
        Pedido pedido2 = new Pedido(cliente, 5, false, false, 6, Constantes.ZONA_SIN_ASFALTAR );

        Assert.assertEquals((Integer) 50, combi.getPuntajePedido(pedido2));
    }

    @Test
    public void testgetPuntajePedidoInvalido() {
        Pedido pedido1 = new Pedido(cliente, 10, true, false, 6, Constantes.ZONA_SIN_ASFALTAR );

        Assert.assertNull(combi.getPuntajePedido(pedido1));
    }

}