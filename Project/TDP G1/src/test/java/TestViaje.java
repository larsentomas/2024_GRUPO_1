import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import modeloDatos.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

public class TestViaje {

    Cliente cliente;
    Pedido pedido;
    Auto auto;
    ChoferPermanente chofer;

    @Before
    public void setUp() {
        cliente = new Cliente("franveron", "mandarina123", "Francisco Veron");
        pedido = new Pedido(cliente, 2, false, true, 6, Constantes.ZONA_STANDARD);
        auto = new Auto("AAA123", 4, false);
        chofer = new ChoferPermanente("47859632", "Mateo", 2020, 4);
    }

    @Test
    public void testFinalizarViaje() {
        int calificacion = 3;
        Viaje viaje = new Viaje(pedido, chofer, auto);
        viaje.finalizarViaje(calificacion);

        Assert.assertEquals(calificacion, viaje.getCalificacion());
        Assert.assertTrue(viaje.isFinalizado());
    }

    @Test
    public void testSetValorViaje() {
        double valorBase = 1000.0;
        Viaje.setValorBase(valorBase);

        Assert.assertEquals(valorBase, Viaje.getValorBase(), 0.0000000001); // que onda con este delta? si lo saco tira error
    }


}
