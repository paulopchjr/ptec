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
import br.com.ptec.entidades.Perfil;

@ViewScoped
@Named(value = "perfilBean")
public class PerfilBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Perfil perfil = new Perfil();
	
	private List<Perfil> perfils = new ArrayList<Perfil>();
	
	@Inject
  transient private DaoGeneric<Perfil>daoGeneric;
	
	public String save() {
		perfil = daoGeneric.Merge(perfil);
		perfil = new Perfil();
		listaPerfils();
		mensagem("Perfil inserido com Sucesso");
		return "";
	}
	
	
	public String novo() {
		perfil = new Perfil();
		listaPerfils();
		return "";
	}
	
	public String excluirPerfil() {
		if(perfil !=null) {
			daoGeneric.deletepoId(perfil);
			perfil = new Perfil();
			listaPerfils();
			mensagem("Perfil excluído com Sucesso!");
		}else {
			perfil = new Perfil();
			listaPerfils();
			mensagem("Selecione um perfil");
			
		}
			return "";
	}
	
	
	public void mensagem(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage msgm = new FacesMessage(msg);
		listaPerfils();	
		context.addMessage(null, msgm);
	}
	
	@PostConstruct
	public void listaPerfils() {
		perfils = daoGeneric.getListEntity(Perfil.class);
	}
	
	
	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	public List<Perfil> getPerfils() {
		return perfils;
	}
	public void setPerfils(List<Perfil> perfils) {
		this.perfils = perfils;
	}
	
	
	
	
	
	
	
	
	
	
}
