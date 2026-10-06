package es.adif.tlg.bean.wrappers;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public class FiltroNumerarWrapperBean { private Integer id; private PuntoSuministroBean p; private Date d,h;
 public void setIdPuntoSuministro(Integer v){id=v;} public void setPuntoSuministro(PuntoSuministroBean v){p=v;}
 public void setFechaInicioDesde(Date v){d=v;} public Date getFechaInicioDesde(){return d;} public void setFechaInicioHasta(Date v){h=v;} public Date getFechaInicioHasta(){return h;} }
