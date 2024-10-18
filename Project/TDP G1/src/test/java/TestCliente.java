
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import modeloDatos.Cliente;

public class TestCliente {

    Cliente c;

    @Before
    public void setUp() {
        c = new Cliente("maite", "maite123", "Maite Nigro");
    }

    @Test
    public void test() {
        Assert.assertEquals(c.getNombreUsuario(), "maite");
        Assert.assertEquals(c.getPass(), "maite123");
        Assert.assertEquals(c.getNombreReal(), "Maite Nigro");
    }

}
