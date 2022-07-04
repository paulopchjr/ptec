package br.com.ptec.bean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.gson.Gson;

import br.com.ptec.dao.DaoGeneric;
import br.com.ptec.entidades.Cidade;
import br.com.ptec.repository.IDaoCidade;

@ViewScoped
@Named(value = "cidadeBean")
public class CidadeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Cidade cidade = new Cidade();
	private List<Cidade> listaCidades = new ArrayList<Cidade>();
	private List<SelectItem> estados;

	@Inject
	transient private DaoGeneric<Cidade> daoGeneric;

	@Inject
	transient private IDaoCidade iDaoCidade;

	public String saveCidade() {

		if (cidade != null) {
			boolean res = iDaoCidade.consultaExiste(cidade.getLocalidade(), cidade.getEstado().getId());
			if(res != true) {
				cidade = daoGeneric.Merge(cidade);
				cidade = new Cidade();
				carregaListaCidades();
				mostraMsg("Cidade inserida com sucesso");
			}else {
				carregaListaCidades();
				mostraMsg("Cidade Já Existe");
			}
		}else {
			carregaListaCidades();
			mostraMsg("Cidade está null");
		}
			
			return "";

	}

	public String excluirCidade() {

		if (cidade != null) {
			daoGeneric.deletepoId(cidade);
			cidade = new Cidade();
			carregaListaCidades();
			mostraMsg(" Cidade removido com Sucesso!");
		} else {
			carregaListaCidades();
			mostraMsg("Cidade não foi informada");
		}
		return "";
	}

	public String novo() {
		cidade = new Cidade();
		carregaListaCidades();
		return "";
	}

	public void mostraMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}

	@PostConstruct
	public void carregaListaCidades() {
		listaCidades = daoGeneric.getListEntityLimit10(Cidade.class);
	}

	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {

			URL url = new URL("https://viacep.com.br/ws/" + cidade.getCep() + "/json/");

			URLConnection connection = url.openConnection();

			InputStream inputStream = connection.getInputStream();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

			String cep = "";

			StringBuilder jsonCep = new StringBuilder();

			while ((cep = bufferedReader.readLine()) != null) {

				jsonCep.append(cep);

			}

			Cidade cidadeGson = new Gson().fromJson(jsonCep.toString(), Cidade.class);
			cidade.setLocalidade(cidadeGson.getLocalidade());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public DaoGeneric<Cidade> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Cidade> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public List<Cidade> getListaCidades() {
		return listaCidades;
	}

	public void setListaCidades(List<Cidade> listaCidades) {
		this.listaCidades = listaCidades;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}

	public List<SelectItem> getEstados() {
		estados = iDaoCidade.Lestados();
		return estados;
	}

}
