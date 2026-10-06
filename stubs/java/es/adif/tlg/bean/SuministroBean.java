package es.adif.tlg.bean;
import java.util.*;
import java.math.*;
import es.adif.tlg.bean.*;
public class SuministroBean {
 private PuntoSuministroBean puntoSuministro; public PuntoSuministroBean getPuntoSuministro(){return puntoSuministro;} public void setPuntoSuministro(PuntoSuministroBean v){puntoSuministro=v;}
 private Integer idSuministro; public Integer getIdSuministro(){return idSuministro;} public void setIdSuministro(Integer v){idSuministro=v;}
 private String numeroDocumento; public String getNumeroDocumento(){return numeroDocumento;} public void setNumeroDocumento(String v){numeroDocumento=v;}
 private String numeroDocumentoSuministro; public String getNumeroDocumentoSuministro(){return numeroDocumentoSuministro;} public void setNumeroDocumentoSuministro(String v){numeroDocumentoSuministro=v;}
 private Date fechaInicio; public Date getFechaInicio(){return fechaInicio;} public void setFechaInicio(Date v){fechaInicio=v;}
 private Date fechaFin; public Date getFechaFin(){return fechaFin;} public void setFechaFin(Date v){fechaFin=v;}
 private Integer litrosSuministrados15; public Integer getLitrosSuministrados15(){return litrosSuministrados15;} public void setLitrosSuministrados15(Integer v){litrosSuministrados15=v;}
}
