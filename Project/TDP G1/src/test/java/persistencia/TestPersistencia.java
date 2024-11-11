package persistencia;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPersistencia {

    private static final String FILENAME = "empresa.xml";

    @Before
    public void setUp() {
        File archivo = new File(FILENAME);
        if (archivo.exists())
            archivo.delete();
    }

    /**
     * Intento despersistir sin un archivo, deberia lanzar una excepcion
     */
    @Test
    public void testAbrirSinArchivo() {
        try {
            PersistenciaBIN persistenciaBIN = new PersistenciaBIN();

            persistenciaBIN.abrirInput(FILENAME);
            persistenciaBIN.cerrarInput();
            Assert.fail("Deberia haber lanzado la excepcion de FileNotFound");
        } catch (IOException e) {

        }
    }

    /**
     * Persisto y corroboro que se creo el archivo con el FILENAME indicado;
     */
    @Test
    public void testCrearArchivo() {
        try {
            EmpresaDTO copia = UtilPersistencia.EmpresaDtoFromEmpresa();

            PersistenciaBIN persistenciaBIN = new PersistenciaBIN();
            persistenciaBIN.abrirOutput(FILENAME);

            persistenciaBIN.escribir(copia);
            persistenciaBIN.cerrarOutput();

            File archivo = new File(FILENAME);

            Assert.assertTrue("Deberia existir el archivo", archivo.exists());
        } catch (IOException e) {
            Assert.fail("No se deberia lanzar la excepcion");
        }
    }

    @Test
    public void testLeerEscribir(){
        try{
            PersistenciaBIN persistenciaBIN = new PersistenciaBIN();
            persistenciaBIN.abrirOutput(FILENAME);
            persistenciaBIN.abrirInput(FILENAME);

            EmpresaDTO nuevo = UtilPersistencia.EmpresaDtoFromEmpresa();
            persistenciaBIN.escribir(nuevo);
            persistenciaBIN.cerrarOutput();

            EmpresaDTO dto = (EmpresaDTO) persistenciaBIN.leer();
            persistenciaBIN.cerrarInput();

            Assert.assertEquals("Las empresas deberian ser iguales, fallo en la persistencia",nuevo,dto);

        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("No se deberia lanzar la excepcion");
        }
    }
}
