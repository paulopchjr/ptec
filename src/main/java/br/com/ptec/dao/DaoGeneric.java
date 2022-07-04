package br.com.ptec.dao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.ptec.util.JpaUtil;

@Named
public class DaoGeneric<E> {

	@Inject
	private JpaUtil jpaUtil;

	private EntityManager entityManager;

	@Inject
	public DaoGeneric(EntityManager eManager) {
		this.entityManager = eManager;

	}

	public void Save(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();
	}

	public E Merge(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E updateEntity = entityManager.merge(entidade);
		transaction.commit();
		return updateEntity;

	}

	public void delete(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.remove(entidade);	
		transaction.commit();
	}

	public void deletepoId(E entidade) {

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		Object id = jpaUtil.getPrimaryKey(entidade);
		/* pessoa */
		entityManager.createQuery("delete from " + entidade.getClass().getCanonicalName() + " WHERE id = " + id)
				.executeUpdate();
		entityTransaction.commit();

	}

	@SuppressWarnings("unchecked")
	public List<E> getListEntity(Class<E> entidade) {

		List<E> listEntidades = entityManager.createQuery("FROM " + entidade.getName()  ).getResultList();

		return listEntidades;
	}
	
	@SuppressWarnings("unchecked")
	public List<E> getListEntityLimit10(Class<E> entidade) {

		List<E> listEntidades = entityManager.createQuery("FROM " + entidade.getName() + " order by id desc"  )
				.setMaxResults(10)
				.getResultList();

		return listEntidades;
	}

	public E pesquisarporid(Long id, Class<E> entidade) {
		E e = (E) entityManager.find(entidade, id);
		return e;
	}

	public E consultar(Class<E> entidade, String codigo) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E objeto = (E) entityManager.find(entidade, Long.parseLong(codigo));

		return objeto;
	}

}
