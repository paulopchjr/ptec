package br.com.ptec.converte;

import java.io.Serializable;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import br.com.ptec.entidades.Pessoa;

@FacesConverter(forClass = Pessoa.class, value = "pessoaConverter")
public class PessoasConverter implements Converter, Serializable {


	private static final long serialVersionUID = 1L;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String codigoPessoa) {
		EntityManager entityManager = CDI.current().select(EntityManager.class).get();
		Pessoa pessoas = entityManager.find(Pessoa.class, Long.parseLong(codigoPessoa));
		return pessoas;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object pessoa) {
		if(pessoa == null) {
			return null;
		}
		
		if(pessoa instanceof Pessoa) {
			return ((Pessoa) pessoa).getId().toString();
		}else {
			return pessoa.toString();
		}
	}

	
}
