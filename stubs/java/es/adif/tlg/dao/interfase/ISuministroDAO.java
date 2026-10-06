package es.adif.tlg.dao.interfase;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public interface ISuministroDAO {
List<SuministroNodoCabezalBean> consultarUltimosSumNodos(); List<SuministroBean> obtenerSaltoNumeraciosDAS(); List<SuministroNodoCabezalBean> consultarSinMovSumNodos(); List<SuministroSurtidorBean> obtenerSuministroSurtidorDetallePorIdSuministro(SuministroBean s);
}
