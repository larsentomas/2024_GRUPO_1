import java.awt.*;
import java.util.Optional;

import controlador.Controlador;
import excepciones.*;
import modeloDatos.*;
import modeloNegocio.Empresa;
import org.junit.After;
import org.junit.Assert;
import util.Constantes;

import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

public class GuiTestEnabledDisabledAdmin {

    Robot robot;
    Controlador controlador;

    @Before
    public void setUp() {
        robot = new Robot();
        controlador = new Controlador();
    }

}
