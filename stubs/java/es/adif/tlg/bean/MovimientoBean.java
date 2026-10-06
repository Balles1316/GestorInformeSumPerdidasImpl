package es.adif.tlg.bean;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public class MovimientoBean implements Comparable<MovimientoBean> {
 private Date fechaInicio; public Date getFechaInicio(){return fechaInicio;} public void setFechaInicio(Date v){fechaInicio=v;}
 private Date fechaFin; public Date getFechaFin(){return fechaFin;} public void setFechaFin(Date v){fechaFin=v;}
 private String tipo; public String getTipo(){return tipo;} public void setTipo(String v){tipo=v;}
 private SurtidorBean surtidor; public SurtidorBean getSurtidor(){return surtidor;} public void setSurtidor(SurtidorBean v){surtidor=v;}
 private String idOperacion; public String getIdOperacion(){return idOperacion;} public void setIdOperacion(String v){idOperacion=v;}
 private Integer numSuministro; public Integer getNumSuministro(){return numSuministro;} public void setNumSuministro(Integer v){numSuministro=v;}
 private Integer litrosTotalInicio; public Integer getLitrosTotalInicio(){return litrosTotalInicio;} public void setLitrosTotalInicio(Integer v){litrosTotalInicio=v;}
 private Integer litrosTotalFin; public Integer getLitrosTotalFin(){return litrosTotalFin;} public void setLitrosTotalFin(Integer v){litrosTotalFin=v;}
 private String numDocumento; public String getNumDocumento(){return numDocumento;} public void setNumDocumento(String v){numDocumento=v;}
 private Integer litrosSum; public Integer getLitrosSum(){return litrosSum;} public void setLitrosSum(Integer v){litrosSum=v;}
 private Integer idSuministro; public Integer getIdSuministro(){return idSuministro;} public void setIdSuministro(Integer v){idSuministro=v;}
 private TanqueBean tanque; public TanqueBean getTanque(){return tanque;} public void setTanque(TanqueBean v){tanque=v;}
 private Integer volumenTotalInicio; public Integer getVolumenTotalInicio(){return volumenTotalInicio;} public void setVolumenTotalInicio(Integer v){volumenTotalInicio=v;}
 private Integer volumenTotalFin; public Integer getVolumenTotalFin(){return volumenTotalFin;} public void setVolumenTotalFin(Integer v){volumenTotalFin=v;}
 private Integer idDescarga; public Integer getIdDescarga(){return idDescarga;} public void setIdDescarga(Integer v){idDescarga=v;}
 private Integer litrosDes; public Integer getLitrosDes(){return litrosDes;} public void setLitrosDes(Integer v){litrosDes=v;}
 public int compareTo(MovimientoBean o){return 0;}
}
