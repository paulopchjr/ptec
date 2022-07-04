package br.com.ptec.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ptec.dao.DaoGeneric;
import br.com.ptec.entidades.Servicos;


@ViewScoped
@Named(value = "servicoBean")
public class ServicoBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Servicos servico = new Servicos();
	private List<Servicos>listServicos = new ArrayList<Servicos>();
	
	@Inject
	transient private DaoGeneric<Servicos>daoGeneric;
	
	
	public String novo() {
		servico = new Servicos();
		return "";
	}
	
	public String save() {
		if(servico !=null) {
			servico = daoGeneric.Merge(servico);
			servico = new Servicos();
			carregaListaServicos();
			mostraMsg("Serviço cadastrado com Sucesso!");
		}
		
		
		return "";
	}
	
	
	public String delete() {
	if( servico != null && servico.getId().hashCode() > 0) {
			daoGeneric.deletepoId(servico);
			servico = new Servicos();
			carregaListaServicos();
			mostraMsg("Serviço Excluido com Sucesso");
		}
		return "";
	}
	
	
	
	@PostConstruct
	public void carregaListaServicos() {
		listServicos = daoGeneric.getListEntity(Servicos.class);
	}
	
	private void mostraMsg(String msg ) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}
	
	
	
	public ServicoBean() {
	}
	
	public Servicos getServico() {
		return servico;
	}
	public void setServico(Servicos servico) {
		this.servico = servico;
	}
	public DaoGeneric<Servicos> getDaoGeneric() {
		return daoGeneric;
	}
	public void setDaoGeneric(DaoGeneric<Servicos> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}
	
	public List<Servicos> getListServicos() {
		return listServicos;
	}
	public void setListServicos(List<Servicos> listServicos) {
		this.listServicos = listServicos;
	}
	
	
	
	
}
