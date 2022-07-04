package br.com.ptec.repository;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.ptec.entidades.OrdemServico;
import br.com.ptec.entidades.Pessoa;
import br.com.ptec.entidades.Servicos;

@Named
public class IDaoOrdemServicoImpl implements IDaoOrdemServico, Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<SelectItem> listPessoas() {
		List<SelectItem> itemsPessoa = new ArrayList<SelectItem>();
		try {

			List<Pessoa> pessoas = entityManager
					.createQuery("from Pessoa p  where p.login !='admin' and p.status = 'ATIVO' ").getResultList();

			for (Pessoa pessoa : pessoas) {
				itemsPessoa.add(new SelectItem(pessoa, pessoa.getNome()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsPessoa;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SelectItem> listarServicos() {
		List<SelectItem> itemServico = new ArrayList<SelectItem>();
		List<Servicos> Listservicos = entityManager.createQuery("from Servicos").getResultList();

		for (Servicos servicos : Listservicos) {
			itemServico.add(new SelectItem(servicos, servicos.getDescricao()));
		}

		return itemServico;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrdemServico> consultarLimit10(Long codigoUsuario) {
		List<OrdemServico> listaOS = null;

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		listaOS = entityManager.createQuery("FROM OrdemServico where pessoa.id = " + codigoUsuario + "order by id desc")
				.setMaxResults(10).getResultList();

		transaction.commit();
		return listaOS;
	}

	@Override
	public List<OrdemServico> relatorioOrdemServicos(Long idServico, Date dataInicial, Date dataFinal) {

		List<OrdemServico> ordernsServicos = new ArrayList<OrdemServico>();
		StringBuilder sql = new StringBuilder();

		sql.append(" select os  From OrdemServico os ");

		if (dataInicial == null && dataFinal == null && idServico != null) {
			sql.append(" where os.servicos = ").append(idServico);
		} 
		else if (idServico == null && dataFinal == null && dataInicial != null) {
		
			String dataInicio = new SimpleDateFormat("yyyy-MM-dd").format(dataInicial);
			sql.append(" where os.dataInicial >= '").append(dataInicio).append("'");
		
		}else if(idServico == null && dataInicial == null && dataFinal !=null) {
			
			String dataFim = new SimpleDateFormat("yyyy-MM-dd").format(dataFinal);
			sql.append(" where os.dataFinal <= '").append(dataFim).append("'");
			
		}else if(idServico == null && dataInicial != null && dataFinal !=null) {
			
				String dataInicio = new SimpleDateFormat("yyyy-MM-dd").format(dataInicial);
				String dataFim = new SimpleDateFormat("yyyy-MM-dd").format(dataFinal);
				sql.append(" where os.dataInicial >= '").append(dataInicio).append("' ");
				sql.append(" and os.dataFinal <= '").append(dataFim).append("'");
				
		}

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		ordernsServicos = entityManager.createQuery(sql.toString()).getResultList();
		transaction.commit();
		return ordernsServicos;
	}

}
