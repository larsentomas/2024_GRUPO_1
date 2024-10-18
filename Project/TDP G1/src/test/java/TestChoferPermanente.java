
import modeloDatos.*;
import org.junit.Assert;
import org.junit.Test;

public class TestChoferPermanente {
    @Test
    public void testChoferPermanente() {
        ChoferPermanente chofer = new ChoferPermanente("12345678", "Patricio", 1999, 2);
        Assert.assertEquals(chofer.getDni(), "12345678");
        Assert.assertEquals(chofer.getNombre(), "Patricio");
        Assert.assertEquals(chofer.getAnioIngreso(), 1999);
        Assert.assertEquals(chofer.getCantidadHijos(), 2);
    }

}
