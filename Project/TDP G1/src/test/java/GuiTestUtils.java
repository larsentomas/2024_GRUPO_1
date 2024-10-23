import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class GuiTestUtils {

    private static int delay = 20;

    public static void setDelay(int delay) {
        GuiTestUtils.delay = delay;
    }

    public static int getDelay() {
        return delay;
    }

    public static Component getComponentByName(Component padre, String nombre) {
       Component retorno = null;
       if (padre.getName() != null && padre.getName().equals(nombre)) {
           retorno = padre;
       } else {
           if (padre instanceof Container) {
               Component[] hijos = ((Container) padre).getComponents();
               int i = 0;
               while (i < hijos.length && retorno == null) {
                     retorno = getComponentByName(hijos[i], nombre);
                     i++;
               }
           }
       }
        return retorno;
    }

    public static Point getCentro(Component componente) {
        Point retorno = null;
        if (componente != null) {
            retorno = componente.getLocationOnScreen();
            retorno.x += componente.getWidth() / 2;
            retorno.y += componente.getHeight() / 2;
        }
        return retorno;
    }

    public static void clickComponente(Component c, Robot robot) {
        Point punto = getCentro(c);
        robot.mouseMove(punto.x, punto.y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(delay);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(delay);
    }

    public static void tipeaTexto(String texto, Robot robot) {
        String mayusculas = texto.toUpperCase();
        char[] letras = mayusculas.toCharArray();
        for (int i = 0; i < letras.length; i++) {
            boolean mayusc = texto.charAt(i) >= 'A' && texto.charAt(i) <= 'Z';
            if (mayusc)
                robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(letras[i]);
            robot.delay(delay);
            robot.keyRelease(letras[i]);
            robot.delay(delay);
            if (mayusc)
                robot.keyRelease(KeyEvent.VK_SHIFT);
        }
    }

    public static void borraJTextField(JTextField jtextfield, Robot robot) {
        Point punto = null;
        if (jtextfield != null) {
            robot.delay(delay);
            punto = jtextfield.getLocationOnScreen();
            robot.mouseMove(punto.x, punto.y);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(delay);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            while (!jtextfield.getText().isEmpty()) {
                robot.delay(delay);
                robot.keyPress(KeyEvent.VK_DELETE);
                robot.delay(delay);
                robot.keyRelease(KeyEvent.VK_DELETE);
            }
        }
    }

}
