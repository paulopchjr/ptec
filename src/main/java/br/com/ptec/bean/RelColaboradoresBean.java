package br.com.ptec.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ptec.dao.DaoGeneric;
import br.com.ptec.entidades.Pessoa;
import br.com.ptec.repository.IDaoPessoa;

@ViewScoped
@Named(value = "relColaboradoresBean")
public class RelColaboradoresBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Pessoa> colaboradores = new ArrayList<Pessoa>();
	private Pessoa pessoa = new Pessoa();
	private List<SelectItem> itemsPerfils;
	private String nome;
	private Date dataInicial;
	private Date dataFinal;

	@Inject
	transient private IDaoPessoa idaoPessoa;

	@Inject
	transient private DaoGeneric<Pessoa> daoGeneric;

	public String limpar() {
		pessoa = new Pessoa();
		setNome(null);
		setDataInicial(null);
		setDataFinal(null);
		return "";
	}

	public void filtro() {

		if ((!nome.isEmpty() || nome != null) && pessoa.getPerfil() == null && dataInicial == null
				&& dataFinal == null) {
			colaboradores = idaoPessoa.relatorioPessoa(nome, null, dataInicial, dataFinal);
		}

		else if ((nome.isEmpty() || nome == null) && pessoa.getPerfil() == null && (dataInicial != null
				|| dataFinal != null)) {
			colaboradores = idaoPessoa.relatorioPessoa(null, null, dataInicial, dataFinal);
		}

		else if ((nome.isEmpty() || nome == null) && pessoa.getPerfil() != null && dataInicial == null
				&& dataFinal == null) {

			Long idPerfil = pessoa.getPerfil().getId();
			colaboradores = idaoPessoa.relatorioPessoa(null, idPerfil, dataInicial, dataFinal);
		}

		else if ((!nome.isEmpty() || nome != null) && pessoa.getPerfil() != null && dataInicial == null
				&& dataFinal == null) {
			Long idPerfil = pessoa.getPerfil().getId();
			colaboradores = idaoPessoa.relatorioPessoa(nome, idPerfil, dataInicial, dataFinal);
		}

		else if ((!nome.isEmpty() || nome != null) && pessoa.getPerfil() != null && dataInicial == null
				&& dataFinal == null) {
			colaboradores = idaoPessoa.relatorioPessoa(nome, pessoa.getId(), dataInicial, dataFinal);
		} else {
			colaboradores = daoGeneric.getListEntity(Pessoa.class);
		}

	}

	public RelColaboradoresBean() {

	}

	public List<Pessoa> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(List<Pessoa> colaboradores) {
		this.colaboradores = colaboradores;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public void setItemsPerfils(List<SelectItem> itemsPerfils) {
		this.itemsPerfils = itemsPerfils;
	}

	public List<SelectItem> getItemsPerfils() {
		itemsPerfils = idaoPessoa.listarPerfils();
		return itemsPerfils;
	}

}
