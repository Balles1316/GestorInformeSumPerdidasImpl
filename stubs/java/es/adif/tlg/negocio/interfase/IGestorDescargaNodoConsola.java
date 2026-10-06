package es.adif.tlg.negocio.interfase;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public interface IGestorDescargaNodoConsola {
List<DescargaNodoConsolaBean> recuperarDescargasEntreFechasTanque(DescargaNodoConsolaBean d); List<DescargaNodoConsolaBean> consultarListaDescargasNodoConsolaSinProcesar(PuntoSuministroBean p);
}
