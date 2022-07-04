package br.com.ptec.teste;

import javax.persistence.Persistence;

public class TesteJpa {
	
	public static void main(String[] args) {
		Persistence.createEntityManagerFactory("ptec");
	}

}
