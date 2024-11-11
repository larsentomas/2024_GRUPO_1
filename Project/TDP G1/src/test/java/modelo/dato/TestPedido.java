package modelo.dato;

import excepciones.UsuarioYaExisteException;
import modeloDatos.Cliente;
import modeloDatos.Pedido;
import modeloNegocio.Empresa;
import org.junit.Assert;
import org.junit.Test;
import util.Constantes;

public class TestPedido {
    @Test
    public void testPedido() {
        try {
            Empresa emp = Empresa.getInstance();
            emp.agregarCliente("franveron", "mandarina123", "Francisco Veron");
            Cliente cliente = emp.getClientes().get("franveron");
            Pedido pedido = new Pedido(cliente, 2, true, false, 0, Constantes.ZONA_PELIGROSA);

            Assert.assertEquals(pedido.getCliente(), cliente);
            Assert.assertEquals(pedido.getCantidadPasajeros(), 2);
            Assert.assertTrue(pedido.isMascota());
            Assert.assertFalse(pedido.isBaul());
            Assert.assertEquals(pedido.getKm(), 0);
            Assert.assertEquals(pedido.getZona(), Constantes.ZONA_PELIGROSA);
        } catch (UsuarioYaExisteException e) {}
    }
}
