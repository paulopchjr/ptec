package br.com.ptec.converte;

import java.io.Serializable;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import br.com.ptec.entidades.Servicos;

@FacesConverter(forClass = Servicos.class, value = "servicosConverter")
public class ServicosConveter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String idServico) {
	
		EntityManager entityManager = CDI.current().select(EntityManager.class).get();
		Servicos servicos = entityManager.find(Servicos.class, Long.parseLong(idServico));
		
		return servicos;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object servicos) {
		if(servicos == null) {
			return null;
		}
		
		if(servicos instanceof Servicos) {
			return ((Servicos) servicos).getId().toString();
			
		}else {
			return servicos.toString();
		}
	}

}
