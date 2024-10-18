
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

import modeloDatos.Auto;
import modeloDatos.Cliente;
import modeloDatos.Pedido;

public class TestAuto {
    Auto auto;

    @Before
    public void setUp() {
        Auto auto = new Auto("AAA123", 4, false);
    }

    @Test
    public void testAuto() {
        Assert.assertEquals(auto.getPatente(), "AAA123");
        Assert.assertEquals(auto.getCantidadPlazas(), 4);
        Assert.assertFalse(auto.isMascota());
    }

    @Test
    public void testgetPuntajePedido() {
        Cliente cliente = new Cliente("franveron", "mandarina123", "Francisco Veron");
        Pedido pedido1 = new Pedido(cliente, 2, false, false, 6, Constantes.ZONA_STANDARD );
        Pedido pedido2 = new Pedido(cliente, 2, false, true, 6, Constantes.ZONA_STANDARD );
        Pedido pedido3 = new Pedido(cliente, 5, false, false, 6, Constantes.ZONA_STANDARD );

        Assert.assertEquals((Integer) 60, auto.getPuntajePedido(pedido1)); // ESTA DEVOLVIENDO 80 COMO SI NO TUVIIERA EN CUENTA EL BAUL
        Assert.assertEquals((Integer) 80, auto.getPuntajePedido(pedido2));
        Assert.assertNull(auto.getPuntajePedido(pedido3));
    }
    
}
