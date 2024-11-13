package persistencia;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPersistenciaEscenario2 {

    private static final String FILENAME = "empresa.xml";

    @Before
    public void setUp() {
        try {
            File archivo = new File(FILENAME);

            if(!archivo.exists())
                archivo.createNewFile();
        }
        catch(Exception e) {
            Assert.fail("No deberia lanzar excepcion");
        }
    }

    /**
     * Prueba la lectura y escritura de los clientes dentro de la empresa sea cual sea su contenido, al guardar los
     * datos de empresa original y compararla con los leidos deberian ser iguales
     */
    @Test
    public void testLeerEscribirClientes() {
        try {
            PersistenciaBIN persistenciaBIN = new PersistenciaBIN();
            persistenciaBIN.abrirOutput(FILENAME);
            persistenciaBIN.abrirInput(FILENAME);

            EmpresaDTO original = UtilPersistencia.EmpresaDtoFromEmpresa();
            persistenciaBIN.escribir(original);
            persistenciaBIN.cerrarOutput();

            EmpresaDTO dto = (EmpresaDTO) persistenciaBIN.leer();
            persistenciaBIN.cerrarInput();

            Assert.assertEquals("Los clientes deberian ser iguales", original.getClientes(), dto.getClientes());

        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("No se deberia lanzar la excepcion");
        }
    }

    /**
     * Prueba la lectura y escritura de los choferes dentro de la empresa sea cual sea su contenido, al guardar los
     * datos de empresa original y compararla con los leidos deberian ser iguales
     */
    @Test
    public void testLeerEscribirChoferes() {
        try {
            PersistenciaBIN persistenciaBIN = new PersistenciaBIN();
            persistenciaBIN.abrirOutput(FILENAME);
            persistenciaBIN.abrirInput(FILENAME);

            EmpresaDTO original = UtilPersistencia.EmpresaDtoFromEmpresa();
            persistenciaBIN.escribir(original);
            persistenciaBIN.cerrarOutput();

            EmpresaDTO dto = (EmpresaDTO) persistenciaBIN.leer();
            persistenciaBIN.cerrarInput();

            Assert.assertEquals("Los choferes deberian ser iguales", original.getChoferes(), dto.getChoferes());

        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("No se deberia lanzar la excepcion");
        }
    }


    /**
     * Prueba la lectura y escritura de los vehiculos dentro de la empresa sea cual sea su contenido, al guardar los
     * datos de empresa original y compararla con los leidos deberian ser iguales
     */

    @Test
    public void testLeerEscribirVehiculos() {
        try {
            PersistenciaBIN persistenciaBIN = new PersistenciaBIN();
            persistenciaBIN.abrirOutput(FILENAME);
            persistenciaBIN.abrirInput(FILENAME);

            EmpresaDTO original = UtilPersistencia.EmpresaDtoFromEmpresa();
            persistenciaBIN.escribir(original);
            persistenciaBIN.cerrarOutput();

            EmpresaDTO dto = (EmpresaDTO) persistenciaBIN.leer();
            persistenciaBIN.cerrarInput();

            Assert.assertEquals("Los vehiculos deberian ser iguales", original.getVehiculos(), dto.getVehiculos());

        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("No se deberia lanzar la excepcion");
        }
    }

    /**
     * Prueba la lectura y escritura de los choferes desocupados dentro de la empresa sea cual sea su contenido, al guardar los
     * datos de empresa original y compararla con los leidos deberian ser iguales
     */

    @Test
    public void testLeerEscribirChoferesDesocupados() {
        try {
            PersistenciaBIN persistenciaBIN = new PersistenciaBIN();
            persistenciaBIN.abrirOutput(FILENAME);
            persistenciaBIN.abrirInput(FILENAME);

            EmpresaDTO original = UtilPersistencia.EmpresaDtoFromEmpresa();
            persistenciaBIN.escribir(original);
            persistenciaBIN.cerrarOutput();

            EmpresaDTO dto = (EmpresaDTO) persistenciaBIN.leer();
            persistenciaBIN.cerrarInput();

            Assert.assertEquals("Los choferes desocupados deberian ser iguales", original.getChoferesDesocupados(), dto.getChoferesDesocupados());

        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("No se deberia lanzar la excepcion");
        }
    }

    /**
     * Prueba la lectura y escritura de los vehiculos desocupados dentro de la empresa sea cual sea su contenido, al guardar los
     * datos de empresa original y compararla con los leidos deberian ser iguales
     */

    @Test
    public void testLeerEscribirVehiculosDesocupados() {
        try {
            PersistenciaBIN persistenciaBIN = new PersistenciaBIN();
            persistenciaBIN.abrirOutput(FILENAME);
            persistenciaBIN.abrirInput(FILENAME);

            EmpresaDTO original = UtilPersistencia.EmpresaDtoFromEmpresa();
            persistenciaBIN.escribir(original);
            persistenciaBIN.cerrarOutput();

            EmpresaDTO dto = (EmpresaDTO) persistenciaBIN.leer();
            persistenciaBIN.cerrarInput();

            Assert.assertEquals("Los vehiculos desocupados deberian ser iguales", original.getVehiculosDesocupados(), dto.getVehiculosDesocupados());

        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("No se deberia lanzar la excepcion");
        }
    }

    /**
     * Prueba la lectura y escritura de los pedidos dentro de la empresa sea cual sea su contenido, al guardar los
     * datos de empresa original y compararla con los leidos deberian ser iguales
     */

    @Test

    public void testLeerEscribirPedidos() {
        try {
            PersistenciaBIN persistenciaBIN = new PersistenciaBIN();
            persistenciaBIN.abrirOutput(FILENAME);
            persistenciaBIN.abrirInput(FILENAME);

            EmpresaDTO original = UtilPersistencia.EmpresaDtoFromEmpresa();
            persistenciaBIN.escribir(original);
            persistenciaBIN.cerrarOutput();

            EmpresaDTO dto = (EmpresaDTO) persistenciaBIN.leer();
            persistenciaBIN.cerrarInput();

            Assert.assertEquals("Los pedidos deberian ser iguales", original.getPedidos(), dto.getPedidos());

        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("No se deberia lanzar la excepcion");
        }
    }

    /**
     * Prueba la lectura y escritura de los viajes iniciados dentro de la empresa sea cual sea su contenido, al guardar los
     * datos de empresa original y compararla con los leidos deberian ser iguales
     */

    @Test

    public void testLeerEscribirViajesIniciados() {
        try {
            PersistenciaBIN persistenciaBIN = new PersistenciaBIN();
            persistenciaBIN.abrirOutput(FILENAME);
            persistenciaBIN.abrirInput(FILENAME);

            EmpresaDTO original = UtilPersistencia.EmpresaDtoFromEmpresa();
            persistenciaBIN.escribir(original);
            persistenciaBIN.cerrarOutput();

            EmpresaDTO dto = (EmpresaDTO) persistenciaBIN.leer();
            persistenciaBIN.cerrarInput();

            Assert.assertEquals("Los viajes iniciados deberian ser iguales", original.getViajesIniciados(), dto.getViajesIniciados());

        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("No se deberia lanzar la excepcion");
        }
    }

    /**
     * Prueba la lectura y escritura de los viajes terminados dentro de la empresa sea cual sea su contenido, al guardar los
     * datos de empresa original y compararla con los leidos deberian ser iguales
     */

    @Test

    public void testLeerEscribirViajesTerminados() {
        try {
            PersistenciaBIN persistenciaBIN = new PersistenciaBIN();
            persistenciaBIN.abrirOutput(FILENAME);
            persistenciaBIN.abrirInput(FILENAME);

            EmpresaDTO original = UtilPersistencia.EmpresaDtoFromEmpresa();
            persistenciaBIN.escribir(original);
            persistenciaBIN.cerrarOutput();

            EmpresaDTO dto = (EmpresaDTO) persistenciaBIN.leer();
            persistenciaBIN.cerrarInput();

            Assert.assertEquals("Los viajes finalizados deberian ser iguales", original.getViajesTerminados(), dto.getViajesTerminados());

        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("No se deberia lanzar la excepcion");
        }
    }

    /**
     * Prueba la lectura y escritura del usuario logeado dentro de la empresa sea cual sea su contenido, al guardar los
     * datos de empresa original y compararla con los leidos deberian ser iguales
     */
    @Test
    public void testLeerEscribirUsuarioLogeado() {
        try {
            PersistenciaBIN persistenciaBIN = new PersistenciaBIN();
            persistenciaBIN.abrirOutput(FILENAME);
            persistenciaBIN.abrirInput(FILENAME);

            EmpresaDTO original = UtilPersistencia.EmpresaDtoFromEmpresa();
            persistenciaBIN.escribir(original);
            persistenciaBIN.cerrarOutput();

            EmpresaDTO dto = (EmpresaDTO) persistenciaBIN.leer();
            persistenciaBIN.cerrarInput();

            Assert.assertEquals("El usuario logeado deberia ser igual", original.getUsuarioLogeado(), dto.getUsuarioLogeado());

        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("No se deberia lanzar la excepcion");
        }
    }
}
