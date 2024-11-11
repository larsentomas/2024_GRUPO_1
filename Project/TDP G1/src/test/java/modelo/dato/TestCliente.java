package modelo.dato;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import modeloDatos.Cliente;

public class TestCliente {

    Cliente c;

    @Before
    public void setUp() {
        c = new Cliente("a", "b", "c");
    }

    @Test
    public void test() {
        Assert.assertEquals(c.getNombreUsuario(), "a");
        Assert.assertEquals(c.getPass(), "b");
        Assert.assertEquals(c.getNombreReal(), "c");
    }

}
