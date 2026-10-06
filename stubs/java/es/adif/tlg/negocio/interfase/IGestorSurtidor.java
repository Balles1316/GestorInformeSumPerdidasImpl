package es.adif.tlg.negocio.interfase;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public interface IGestorSurtidor {
List<SurtidorBean> consultarSurtidoresPuntoSuministro(PuntoSuministroBean p);
}
