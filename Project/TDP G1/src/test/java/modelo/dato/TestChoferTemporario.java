package modelo.dato;

import modeloDatos.*;
import org.junit.Assert;
import org.junit.Test;

public class TestChoferTemporario {

    @Test
    public void testChoferTemporario() {
        ChoferTemporario c = new ChoferTemporario("a", "b");
        Assert.assertEquals(c.getDni(), "a");
        Assert.assertEquals(c.getNombre(), "b");
    }
}
