import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

import modeloDatos.Auto;
import modeloDatos.Cliente;
import modeloDatos.Pedido;

public class TestAuto {

    @Test
    public void testgetPuntajePedido() {
        Cliente cliente = new Cliente("franveron", "mandarina123", "Francisco Veron");
        Pedido pedido = new Pedido(cliente, 3, false, true, 6, Constantes.ZONA_STANDARD );

        Auto auto = new Auto("AAA123", 4, false);

        Assert.assertEquals((Integer) 120, auto.getPuntajePedido(pedido));
    }
    
}
