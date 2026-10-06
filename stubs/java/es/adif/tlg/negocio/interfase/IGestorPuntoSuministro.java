package es.adif.tlg.negocio.interfase;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public interface IGestorPuntoSuministro {
List<PuntoSuministroBean> consultarPuntosSuministro(); PuntoSuministroBean consultarDetallePuntoSuministro(PuntoSuministroBean p);
}
