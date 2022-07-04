package br.com.ptec.converte;

import java.io.Serializable;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import br.com.ptec.entidades.Cidade;


@FacesConverter(forClass = Cidade.class, value = "cidadeConverter")
public class CidadeConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String codigoCidade) {
		
		EntityManager entityManager = CDI.current().select(EntityManager.class).get();
		Cidade cidades = entityManager.find(Cidade.class, Long.parseLong(codigoCidade));

		return cidades;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object objCidade) {
		if (objCidade == null) {
			return null;
		}

		if (objCidade instanceof Cidade) {
				
			 long cid =	((Cidade) objCidade).getId();
			return String.valueOf(cid);
		} else {
			return objCidade.toString();
		}
	}

}
