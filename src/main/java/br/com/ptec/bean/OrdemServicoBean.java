package br.com.ptec.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ptec.dao.DaoGeneric;
import br.com.ptec.entidades.OrdemServico;
import br.com.ptec.entidades.Pessoa;
import br.com.ptec.repository.IDaoOrdemServico;


@ViewScoped
@Named(value = "ordemservicoBean")
public class OrdemServicoBean  implements Serializable{

	private static final long serialVersionUID = 1L;
	private OrdemServico ordemServico = new OrdemServico();
	private List<OrdemServico> listOrdemServicos = new ArrayList<OrdemServico>();
	private List<SelectItem> pessoas;
	private List<SelectItem> itemServicos;

	@Inject
	transient private DaoGeneric<OrdemServico> daoGeneric;
	
	@Inject
	transient private IDaoOrdemServico daoOrdemServico;

	public String novo() {
		ordemServico = new OrdemServico();
		return "";
	}

	public String save() {
		try {
			if (ordemServico != null) {
				ordemServico = daoGeneric.Merge(ordemServico);
				ordemServico = new OrdemServico();
				listarOrdemdeServicos();
				msgs("Ordem de serviços cadastado com Sucesso!");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("mensagem do erro: " + e.getMessage());
			System.out.println("Local: " + e.getLocalizedMessage());
			System.out.println("Causa: " + e.getCause());
		}
		
		
		
		return "";
	}

	public String delete() {
		if (ordemServico != null && ordemServico.getId() > 0) {
			daoGeneric.deletepoId(ordemServico);
			listarOrdemdeServicos();
			msgs("Ordem de Serviços Excluida com Sucesso!");
		}else {
			msgs("Não foi selecionado a ordem");
		}
		return "";
	}
	
	
	

	@PostConstruct
	public void listarOrdemdeServicos() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoauser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
		if(!pessoauser.getIsSuperAdmin().equalsIgnoreCase("SUPERADMIN")) {
			listOrdemServicos = daoOrdemServico.consultarLimit10(pessoauser.getId());
			
		}else {
			listOrdemServicos = daoGeneric.getListEntity(OrdemServico.class);
		}
		
	}
	
	public OrdemServicoBean() {
		
	}

	private void msgs(String mensagem) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(mensagem);
		context.addMessage(null, message);
	}
	
	


	public OrdemServico getOrdemServico() {
		return ordemServico;
	}

	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}

	public DaoGeneric<OrdemServico> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<OrdemServico> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public List<OrdemServico> getListOrdemServicos() {
		return listOrdemServicos;
	}

	public void setListOrdemServicos(List<OrdemServico> listOrdemServicos) {
		this.listOrdemServicos = listOrdemServicos;
	}

	public void setPessoas(List<SelectItem> pessoas) {
		this.pessoas = pessoas;
	}

	public List<SelectItem> getPessoas() {
		pessoas = daoOrdemServico.listPessoas();
		return pessoas;
	}

	public List<SelectItem> getItemServicos() {
		itemServicos = daoOrdemServico.listarServicos();
		return itemServicos;
	}

	public void setItemServicos(List<SelectItem> itemServicos) {

		this.itemServicos = itemServicos;
	}

}
