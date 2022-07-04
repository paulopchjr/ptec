package br.com.ptec.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ptec.dao.DaoGeneric;
import br.com.ptec.entidades.OrdemServico;
import br.com.ptec.repository.IDaoOrdemServico;

@ViewScoped
@Named(value = "relatorioOsBean")
public class RelOrdemServicos implements Serializable {

	private static final long serialVersionUID = 1L;

	private OrdemServico ordemServico = new OrdemServico();
	private List<OrdemServico> listaOs = new ArrayList<OrdemServico>();

	@Inject
	transient private DaoGeneric<OrdemServico> daoGeneric;

	@Inject
	transient private IDaoOrdemServico daoOrdemServico;

	private Date dataInicial;
	private Date dataFinal;

	public String limpar() {
		
		setDataFinal(null);
		setDataInicial(null);
		setListaOs(null);
		
		return "";
	}
	
	public void buscar() {
		
				
		if (ordemServico.getServicos() == null && dataInicial == null && dataFinal == null) {
			listaOs = daoGeneric.getListEntity(OrdemServico.class);
		}
		else if(ordemServico.getServicos() == null && dataInicial !=null && dataFinal !=null) {
			listaOs = daoOrdemServico.relatorioOrdemServicos(null, dataInicial, dataFinal);
		}
		else {
			Long servico_id = ordemServico.getServicos().getId();
			listaOs = daoOrdemServico.relatorioOrdemServicos(servico_id, dataInicial, dataFinal);
			
		}

	}

	public OrdemServico getOrdemServico() {
		return ordemServico;
	}

	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
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



	public List<OrdemServico> getListaOs() {
		return listaOs;
	}

	public void setListaOs(List<OrdemServico> listaOs) {
		this.listaOs = listaOs;
	}

}
