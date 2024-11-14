package modelo.dato;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

import modeloDatos.Moto;
import modeloDatos.Cliente;
import modeloDatos.Pedido;

public class TestMoto {

    Moto moto;

    @Before
    public void setUp() {
        moto = new Moto("AAA123");
    }

    @Test
    public void testMotoPatenteLarga() {
        Assert.assertEquals(moto.getPatente(), "AAA123");
        Assert.assertEquals(moto.getCantidadPlazas(), 1);
        Assert.assertFalse(moto.isMascota());
    }

    @Test
    public void testMotoPatenteVacia() {
        Moto moto2 = new Moto("");
        Assert.assertEquals(moto2.getPatente(), "");
        Assert.assertEquals(moto2.getCantidadPlazas(), 1);
        Assert.assertFalse(moto2.isMascota());
    }


    @Test
    public void testgetPuntajePedidoValido() {
        Cliente cliente = new Cliente("franveron", "mandarina123", "Francisco Veron");
        Pedido pedido1 = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD );

        Assert.assertEquals((Integer) 1000, moto.getPuntajePedido(pedido1));
    }

    @Test
    public void testgetPuntajePedidoInvalido1() {
        Cliente cliente = new Cliente("franveron", "mandarina123", "Francisco Veron");
        Pedido pedido2 = new Pedido(cliente, 2, true, true, 6, Constantes.ZONA_STANDARD );

        Assert.assertNull(moto.getPuntajePedido(pedido2));
    }

}