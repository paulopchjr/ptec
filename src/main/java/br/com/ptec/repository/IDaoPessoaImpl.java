package br.com.ptec.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.ptec.entidades.Estado;
import br.com.ptec.entidades.Perfil;
import br.com.ptec.entidades.Pessoa;

@Named
public class IDaoPessoaImpl implements IDaoPessoa {

	@Inject
	private EntityManager entityManager;

	@Override
	public Pessoa consultarUsuario(String login, String senha) {

		Pessoa usuarioPessoa = null;

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			usuarioPessoa = (Pessoa) entityManager
					.createQuery("SELECT p FROM Pessoa p where p.login ='" + login + "' and senha ='" + senha + "'")
					.getSingleResult();
		} catch (javax.persistence.NoResultException e) { /* Se nao encontrar o usuario com login e senha */

		}

		transaction.commit();

		return usuarioPessoa;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SelectItem> listarEstados() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		List<Estado> estados = entityManager.createQuery("from Estado").getResultList();

		for (Estado estado : estados) {
			items.add(new SelectItem(estado, estado.getNome()));
		}
		return items;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SelectItem> listarPerfils() {
		List<SelectItem> itemsPerfils = new ArrayList<SelectItem>();
		try {
			List<Perfil> perfils = entityManager.createQuery("from Perfil").getResultList();

			for (Perfil perfil : perfils) {
				itemsPerfils.add(new SelectItem(perfil, perfil.getTipoPerfil()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Classe: " + e.getClass());
			System.out.println("Mensagem do Erro: " + e.getMessage());
			System.out.println("Localização: " + e.getLocalizedMessage());
			System.out.println("Causa: " + e.getCause());

		}

		return itemsPerfils;

	}

	@Override
	public List<Pessoa> relatorioPessoa(String nome, Long idperfil, Date dataInicio, Date dataFim) {

		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT p FROM Pessoa p ");

		
		if(nome== null && idperfil !=null && dataInicio == null && dataFim == null) {
			sql.append("where p.perfil = ").append(idperfil);
		}
		
		else if (nome == null && idperfil == null && dataFim == null && dataInicio != null) {

			String dataI = new SimpleDateFormat("yyyy-MM-dd").format(dataInicio);
			sql.append("where p.dataNascimento >='").append(dataI).append("'");
		}

		else if (nome == null && idperfil == null && dataInicio == null && dataFim != null) {

			String dataFin = new SimpleDateFormat("yyyy-MM-dd").format(dataFim);
			sql.append("where p.dataNascimento <= '").append(dataFin).append("'");
		}

		else if (nome == null && idperfil == null && dataInicio != null && dataFim != null) {
			String dataI = new SimpleDateFormat("yyyy-MM-dd").format(dataInicio);
			String dataFin = new SimpleDateFormat("yyyy-MM-dd").format(dataFim);
			sql.append("where p.datanascimento >='").append(dataI).append("' ");
			sql.append("and  p.datanascimento <='").append(dataFin).append("' ");

		} 
		else if (nome != null && idperfil == null && dataInicio == null && dataFim == null) {
			sql.append("where p.nome like '%").append(nome).append("%'");
		} 
		else if (nome != null && idperfil != null && dataInicio == null && dataFim == null) {
			sql.append("where p.nome like '%").append(nome).append("%'");
			sql.append(" and p.perfil=").append(idperfil);
		}

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		pessoas = entityManager.createQuery(sql.toString()).getResultList();

		transaction.commit();

		return pessoas;
	}

}
