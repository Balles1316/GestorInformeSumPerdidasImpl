package es.adif.tlg.negocio.impl;

import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import es.adif.tlg.bean.FilaControl;

public class GestorInformeSumPerdidasImplTest {

    @Test
    public void generaHtmlDeLasTablas() throws Exception {
        List<FilaControl> filas = new ArrayList<>();
        filas.add(fila("Abroñigal", 22851, 16484, 9912, 3573, 28, 1000000, 1003573, 16480, 22826));
        filas.add(fila("Monforte", 71753, 46187, 31806, 6102, -138, 2000000, 2006107, 46100, 71700)); // Dif TOT vs SUM = 5 -> rojo
        FilaControl salamanca = fila("Salamanca", 78607, 58777, 31791, 10880, -1081, 3000000, 3010880, 58700, 78500); // AVG ~9,9 % -> rojo
        // Para ver las tablas CUB (valores inventados)
        salamanca.fechaCub = new Date();
        salamanca.ntICub = 50000;
        salamanca.etIniCub = 49000;
        salamanca.tlDCub = 100000;
        salamanca.tlSCub = 50000;
        salamanca.difCub = -5000;                                                                     // AVG 10 % -> rojo
        filas.add(salamanca);

        GestorInformeSumPerdidasImpl gestor = new GestorInformeSumPerdidasImpl();   // sin Spring: solo usamos el pintado
        StringBuilder sb = new StringBuilder("<html><body>");
        gestor.crearTablasControl(sb, filas);
        sb.append("</body></html>");

        Path salida = Paths.get("target", "informe-control.html");
        Files.createDirectories(salida.getParent());
        Files.write(salida, sb.toString().getBytes(StandardCharsets.UTF_8));

        String html = sb.toString();
        assertTrue(html.contains("Comprobación diaria Totalizadores:"));
        assertTrue(html.contains("Comprobación diaria Litros Registrados:"));
        assertTrue(html.contains("Comprobación diaria Litros registrados desde CUB:"));
        assertTrue(html.contains("Comprobación diaria Mermas o Excesos:"));
        assertTrue(html.contains("Comprobación diaria Mermas o Excesos desde CUB:"));
        assertTrue(html.contains("color:red"));   // hay algún valor en rojo
        }

    private FilaControl fila(String nombre, int ntF, int ntI, double tlD, double tlS, double difLitros,
                             double totIni, double totFin, double etIni, double etFin) {
        FilaControl f = new FilaControl();
        f.nombre = nombre;
        f.ntF = ntF;
        f.ntI = ntI;
        f.tlD = tlD;
        f.tlS = tlS;
        f.difLitros = difLitros;
        f.totIni = totIni;
        f.totFin = totFin;
        f.etIni = etIni;
        f.etFin = etFin;
        f.numSum = 10;
        f.numSumCab = 10;
        f.numDes = 2;
        f.numDesCon = 2;
        return f;
    }
}
