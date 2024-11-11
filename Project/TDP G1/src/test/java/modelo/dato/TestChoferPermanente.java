package modelo.dato;

import modeloDatos.*;
import org.junit.Assert;
import org.junit.Test;

public class TestChoferPermanente {

    @Test
    public void testChoferPermanenteLimiteSuperior() {
        ChoferPermanente chofer = new ChoferPermanente("12345678", "Mateo", 2999, 1);
        Assert.assertEquals(chofer.getDni(), "12345678");
        Assert.assertEquals(chofer.getNombre(), "Mateo");
        Assert.assertEquals(chofer.getAnioIngreso(), 2999);
        Assert.assertEquals(chofer.getCantidadHijos(), 1);
    }

    @Test
    public void testChoferPermanenteLimiteInferior() {
        ChoferPermanente chofer2 = new ChoferPermanente("a", "b", 1901, 0);
        Assert.assertEquals(chofer2.getDni(), "a");
        Assert.assertEquals(chofer2.getNombre(), "b");
        Assert.assertEquals(chofer2.getAnioIngreso(), 1901);
        Assert.assertEquals(chofer2.getCantidadHijos(), 0);
    }

    @Test
    public void testSetCantHijos() {
        ChoferPermanente chofer = new ChoferPermanente("12345678", "Mateo", 2999, 0);
        chofer.setCantidadHijos(10);
        Assert.assertEquals(chofer.getCantidadHijos(), 10);
    }

    @Test
    public void testGetSueldoBruto20Anios() {
        ChoferPermanente.setSueldoBasico(1000);
        ChoferPermanente chofer = new ChoferPermanente("chofer", "chofer", 2004, 0);

        Assert.assertEquals(chofer.getSueldoBruto(), 2000, 0.0000000001);
    }

    @Test
    public void testGetSueldoBrutoMas20Anios() {
        ChoferPermanente.setSueldoBasico(1000);
        ChoferPermanente chofer1 = new ChoferPermanente("chofer1", "chofer", 2000, 0);
        ChoferPermanente chofer2 = new ChoferPermanente("chofer2", "chofer", 2000, 1);

        Assert.assertEquals(chofer1.getSueldoBruto(), 2070, 0.0000000001); // TODO: Fallo
        Assert.assertEquals(chofer2.getSueldoBruto(), 2000, 0.0000000001); // TODO: Fallo
    }

    @Test
    public void testGetSueldoBrutoMenos20Anios() {
        ChoferPermanente.setSueldoBasico(1000);
        ChoferPermanente chofer1 = new ChoferPermanente("chofer1", "chofer1", 2010, 1);
        ChoferPermanente chofer2 = new ChoferPermanente("chofer2", "chofer2", 2010, 0);
        ChoferPermanente chofer3 = new ChoferPermanente("chofer3", "chofer3", 2024, 1);

        Assert.assertEquals(chofer1.getSueldoBruto(), 1770, 0.0000000001);
        Assert.assertEquals(chofer2.getSueldoBruto(), 1700, 0.0000000001);
        Assert.assertEquals(chofer3.getSueldoBruto(), 1070, 0.0000000001);
    }

}
