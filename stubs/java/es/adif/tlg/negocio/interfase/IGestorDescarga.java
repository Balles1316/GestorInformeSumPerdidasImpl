package es.adif.tlg.negocio.interfase;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public interface IGestorDescarga {
List<DescargaBean> descargasPuntoFechas(SuministroBean s);
}
