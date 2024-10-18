import modeloDatos.*;
import org.junit.Assert;
import org.junit.Test;

public class TestChoferTemporario {

    @Test
    public void testChofer() {
        ChoferTemporario c = new ChoferTemporario("12345678", "Maite Nigro"); // NO SE PUEDE HACER, ES ABSTRACTO. COMO SE TESTEA ENTONCES
        Assert.assertEquals(c.getDni(), "12345678");
        Assert.assertEquals(c.getNombre(), "Maite Nigro");
    }
}
