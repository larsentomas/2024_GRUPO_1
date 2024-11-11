package modelo.negocio;

import excepciones.*;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.Constantes;

import modeloDatos.Auto;
import modeloDatos.Cliente;
import modeloDatos.Pedido;


public class TestEmpresaGral {
    Empresa emp;

    @Before
    public void setUp() {
        emp = Empresa.getInstance();
    }

    @Test
    public void testGetInstance() {
        Assert.assertEquals(emp, Empresa.getInstance());
        Empresa empresa2 = Empresa.getInstance();
        Assert.assertEquals(emp, empresa2);
    }

    @Test
    public void testSueldosTotales() {
        try {
            emp.agregarChofer(new ChoferPermanente("12345678", "Pepe", 2000, 2));
            emp.agregarChofer(new ChoferTemporario("87654321", "Juan"));

            double totalSalarios = 0;
            for (Chofer chofer : emp.getChoferes().values()) {
                totalSalarios += chofer.getSueldoNeto();
            }

            Assert.assertEquals("El total de sueldos a pagar es incorrecto", emp.getTotalSalarios(), totalSalarios, 0.0000000001);
        } catch(ChoferRepetidoException e) {}
    }

}
