package es.adif.tlg.bean;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public class DescargaNodoConsolaBean {
 private Date fechaInicioDescarga; public Date getFechaInicioDescarga(){return fechaInicioDescarga;} public void setFechaInicioDescarga(Date v){fechaInicioDescarga=v;}
 private Date fechaFinDescarga; public Date getFechaFinDescarga(){return fechaFinDescarga;} public void setFechaFinDescarga(Date v){fechaFinDescarga=v;}
 private TanqueBean tanque; public TanqueBean getTanque(){return tanque;} public void setTanque(TanqueBean v){tanque=v;}
 private Double volumenNetoFin; public Double getVolumenNetoFin(){return volumenNetoFin;} public void setVolumenNetoFin(Double v){volumenNetoFin=v;}
 private Double volumenNetoInicio; public Double getVolumenNetoInicio(){return volumenNetoInicio;} public void setVolumenNetoInicio(Double v){volumenNetoInicio=v;}
 private Integer difLitros; public Integer getDifLitros(){return difLitros;} public void setDifLitros(Integer v){difLitros=v;}
 private Integer opEspecial; public Integer getOpEspecial(){return opEspecial;} public void setOpEspecial(Integer v){opEspecial=v;}
 private String idOperacion; public String getIdOperacion(){return idOperacion;} public void setIdOperacion(String v){idOperacion=v;}
}
