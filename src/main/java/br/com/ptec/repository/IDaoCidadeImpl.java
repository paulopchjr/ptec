package br.com.ptec.repository;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.ptec.entidades.Estado;

@Named
public class IDaoCidadeImpl implements IDaoCidade {

	@Inject
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<SelectItem> Lestados() {

		List<SelectItem> selectItems = new ArrayList<SelectItem>();

		List<Estado> estados = entityManager.createQuery("from Estado").getResultList();

		for (Estado estado : estados) {
			selectItems.add(new SelectItem(estado, estado.getNome()));
		}
		return selectItems;
	}

	@Override
	public boolean consultaExiste(String nome, Long estado) {

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		Long result  = (Long) entityManager.createQuery(
				"SELECT COUNT(*) FROM Cidade c where c.localidade='"+nome +"' and c.estado=" + estado)
				.getSingleResult();
		transaction.commit();

		if(result !=0) {
			return true;
		}
		return false;
	}

}
