package br.com.ptec.converte;

import java.io.Serializable;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import br.com.ptec.entidades.Perfil;

@FacesConverter(forClass = Perfil.class, value = "perfilConverter")
public class PerfilConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String codigoPerfil) {
		EntityManager entityManager = CDI.current().select(EntityManager.class).get();
		Perfil perfils = entityManager.find(Perfil.class, Long.parseLong(codigoPerfil));
		return perfils;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object objPerfil) {
		if (objPerfil == null) {
			return null;
		}

		if (objPerfil instanceof Perfil) {
			long pfl = ((Perfil) objPerfil).getId();

			return String.valueOf(pfl);
		} else {
			return objPerfil.toString();
		}
	}

}
