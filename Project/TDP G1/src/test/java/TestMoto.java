
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
    public void testMoto() {
        Assert.assertEquals(moto.getPatente(), "AAA123");
        Assert.assertEquals(moto.getCantidadPlazas(), 1);
        Assert.assertFalse(moto.isMascota());
    }


    @Test
    public void testgetPuntajePedido() {
        Cliente cliente = new Cliente("franveron", "mandarina123", "Francisco Veron");
        Pedido pedido1 = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD );
        Pedido pedido2 = new Pedido(cliente, 2, false, false, 6, Constantes.ZONA_STANDARD );

        Assert.assertEquals((Integer) 1000, moto.getPuntajePedido(pedido1));
        Assert.assertNull(moto.getPuntajePedido(pedido2));
    }

}