package br.com.ptec.teste;

import javax.inject.Inject;

import org.junit.Test;

import br.com.ptec.dao.DaoGeneric;
import br.com.ptec.entidades.Pessoa;

public class TesteCrud {
	
	@Inject
	private transient DaoGeneric<Pessoa> daoGeneric;
	
	
	@Test
	public void salvar() {
		
		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Camila");
		pessoa.setIdade(23);
		
		daoGeneric.Save(pessoa);
	}
	
	@Test
	public void delete() {
		
		Pessoa pessoa = daoGeneric.pesquisarporid(4L, Pessoa.class);
		
		daoGeneric.deletepoId(pessoa);
	}
	

}
