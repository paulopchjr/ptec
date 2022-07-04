package br.com.ptec.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.constraints.br.CPF;

@SuppressWarnings("deprecation")
@Entity
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty(message = "O nome deve ser informado")
	@NotNull
	@Size(min = 5, max = 50, message = "O nome deve ter entre 5 a 50 caracter")
	private String nome;

	@DecimalMax(value = "60", message = "Idade deve ser menor que 60")
	@DecimalMin(value = "18", message = "Idade deve ser maior que 10")
	private int idade;
	@Temporal(TemporalType.DATE)
	private Date dataNascimento = new Date();

	@Email(message = "Email invalido")
	private String email;

	@NotEmpty(message = "Preencha o login")
	@NotNull(message = "O login nao pode ser nulo")
	@Size(min = 5, max = 10, message = "login tem que ser maior que 5 characteres e menor que 10")
	private String login;

	@NotEmpty(message = "Preencha a Senha")
	@NotNull
	@Size(min = 5, max = 8, message = "A Senha tem que ter entre 5 e 8 charactres")
	private String senha;

	@NotNull(message = "Você não selecionou um Sexo!")
	private String sexo;

	@ManyToOne
	@ForeignKey(name = "fk_cidade_id")
	private Cidade cidade;

	@Transient
	private Estado estado;

	@ManyToOne
	@ForeignKey(name = "fk_perfil_id")
	private Perfil perfil;

	private String isSuperAdmin;

	@Column(columnDefinition = "text") /* tipo text grava arquivos em base 64 */
	private String fotoIconBase64;

	private String extensao; /* jpg, png, jpeg */

	@Lob /* Gravar arquivos no banco de dados */
	@Basic(fetch = FetchType.LAZY)
	private byte[] fotoIconBase64Original;

	@CPF(message = "Cpf Inválido")
	private String cpf;

	@NotEmpty(message = "o campo do RG está vazio")
	@Size(min = 9, max = 9, message = "RG INVÁLIDO")
	private String rg;

	@NotNull(message = "Selecione um Status")
	private String status;

	public Pessoa() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getSexo() {
		return sexo;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setIsSuperAdmin(String isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public String getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public String getFotoIconBase64() {
		return fotoIconBase64;
	}

	public void setFotoIconBase64(String fotoIconBase64) {
		this.fotoIconBase64 = fotoIconBase64;
	}

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	public byte[] getFotoIconBase64Original() {
		return fotoIconBase64Original;
	}

	public void setFotoIconBase64Original(byte[] fotoIconBase64Original) {
		this.fotoIconBase64Original = fotoIconBase64Original;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		
		return status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		return Objects.equals(id, other.id);
	}

}
