import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

import modeloDatos.Moto;
import modeloDatos.Cliente;
import modeloDatos.Pedido;

public class TestMoto {

    @Test
    public void testgetPuntajePedido() {
        Cliente cliente = new Cliente("franveron", "mandarina123", "Francisco Veron");
        Pedido pedido1 = new Pedido(cliente, 1, false, false, 6, Constantes.ZONA_STANDARD );
        Pedido pedido2 = new Pedido(cliente, 2, false, false, 6, Constantes.ZONA_STANDARD );


        Moto moto = new Moto("AAA123");

        Assert.assertEquals((Integer) 1000, moto.getPuntajePedido(pedido1));
        Assert.assertNull(moto.getPuntajePedido(pedido2));
    }

}