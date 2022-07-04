package br.com.ptec.repository;

import java.util.List;

import javax.faces.model.SelectItem;

public interface IDaoCidade {

	List<SelectItem>Lestados();
	
	boolean consultaExiste(String nome, Long estado);
	
}
