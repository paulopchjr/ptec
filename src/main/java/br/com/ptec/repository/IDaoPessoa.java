package br.com.ptec.repository;

import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import br.com.ptec.entidades.Pessoa;

public interface IDaoPessoa {

	Pessoa consultarUsuario(String login, String senha);
	
	List<SelectItem>listarEstados();
	
	List<SelectItem>listarPerfils();
	
	
	List<Pessoa>relatorioPessoa(String nome, Long idperfil, Date dataInicio, Date dataFim);
	
}
