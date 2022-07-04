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
import br.com.ptec.entidades.Estado;

@ViewScoped
@Named(value = "estadoBean")
public class EstadoBean  implements Serializable{

	private static final long serialVersionUID = 1L;
	private Estado estado = new Estado();
	private List<Estado> listaEstados = new ArrayList<Estado>();

	@Inject
	 transient private DaoGeneric<Estado> daoGeneric;

	public String novo() {
		estado = new Estado();
		return "";
	}

	public String saveEstado() {
		estado = daoGeneric.Merge(estado);
		estado = new Estado();
		listarEstados();
		mostrarMsg("Estado salvo com sucesso");
		return "";
	}

	public String deleteEstado() {
		daoGeneric.delete(estado);
		estado = new Estado();
		listarEstados();
		mostrarMsg("Estado removido com sucesso");
		return "";
	}

	public void mostrarMsg(String mensagem) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(mensagem);
		context.addMessage(null, message);
	}

	@PostConstruct
	public void listarEstados() {
		listaEstados = daoGeneric.getListEntity(Estado.class);
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Estado> getListaEstados() {
		return listaEstados;
	}

	public void setListaEstados(List<Estado> listaEstados) {
		this.listaEstados = listaEstados;
	}

}
