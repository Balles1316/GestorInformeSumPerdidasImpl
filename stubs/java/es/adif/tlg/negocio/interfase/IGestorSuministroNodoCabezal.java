package es.adif.tlg.negocio.interfase;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public interface IGestorSuministroNodoCabezal {
List<SuministroNodoCabezalBean> consultarSuministrosNodoCabezal(SuministroNodoCabezalBean s); List<SuministroNodoCabezalBean> consultarSuministrosNoProcesados(PuntoSuministroBean p); double obtenerTotalizadoresPorDia(Date d, Integer id);
}
