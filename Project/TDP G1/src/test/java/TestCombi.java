import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

import modeloDatos.Combi;
import modeloDatos.Cliente;
import modeloDatos.Pedido;

public class TestCombi {

    @Test
    public void testgetPuntajePedido() {
        Cliente cliente = new Cliente("franveron", "mandarina123", "Francisco Veron");
        Pedido pedido1 = new Pedido(cliente, 10, true, false, 6, Constantes.ZONA_SIN_ASFALTAR );
        Pedido pedido2 = new Pedido(cliente, 5, false, false, 6, Constantes.ZONA_SIN_ASFALTAR );
        Pedido pedido3 = new Pedido(cliente, 5, false, true, 6, Constantes.ZONA_SIN_ASFALTAR );


        Combi combi = new Combi("AAA123", 9, false);

        Assert.assertNull(combi.getPuntajePedido(pedido1));
        Assert.assertEquals((Integer) 50, combi.getPuntajePedido(pedido2));
        Assert.assertEquals((Integer) 150, combi.getPuntajePedido(pedido3)); // ESTA DEVOLVIENDO 50, COMO SI NO TUVIERA EN CUENTA EL BAUL
    }

}

