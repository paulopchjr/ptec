package br.com.ptec.bean;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import br.com.ptec.dao.DaoGeneric;
import br.com.ptec.entidades.Cidade;
import br.com.ptec.entidades.Estado;
import br.com.ptec.entidades.Pessoa;
import br.com.ptec.repository.IDaoPessoa;
import br.com.ptec.util.JpaUtil;
import net.bootsfaces.component.selectOneMenu.SelectOneMenu;

@ViewScoped
@Named(value = "pessoaBean")
public class PessoaBean  implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private Pessoa pessoa = new Pessoa();
	private List<Pessoa> listaPessoas = new ArrayList<Pessoa>();
	private List<SelectItem> cidades;
	private List<SelectItem> estados;
	private List<SelectItem> pessoaPerfils;
	
	
	@Inject
	 transient private DaoGeneric<Pessoa> daoGeneric;

	@Inject
	 transient private IDaoPessoa daoPessoa;

	
	@Inject
	private JpaUtil jpaUtil;
	
	private Part arquivoFoto;

	

	public String novo() {
		
		pessoa = new Pessoa();
		carregarListaPessoas();
		return "";
	}

	@SuppressWarnings("static-access")
	public String salvarPessoa() {

		try {
				
			byte[] imagemByte = null;
			if(arquivoFoto !=null) {
				imagemByte = getBtye(arquivoFoto.getInputStream());
			}
			if(imagemByte !=null && imagemByte.length > 0) {
				pessoa.setFotoIconBase64Original(imagemByte);
				
				BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByte));
				
				int tipoImg = bufferedImage.getType() == 0 ? bufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
				
				int largura =200;
				int altura = 200;
				
				BufferedImage resizedImage = new BufferedImage(largura, altura, tipoImg);
				Graphics2D g = resizedImage.createGraphics();
				g.drawImage(bufferedImage, 0, 0, altura, largura, null);
				g.dispose();
				
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				String extensao = arquivoFoto.getContentType().split("\\/")[1];
				ImageIO.write(resizedImage, extensao, baos);
				
				String miniImagem = "data:"+arquivoFoto.getContentType()+";base64,"+ 
				DatatypeConverter.printBase64Binary(baos.toByteArray());
				
				pessoa.setFotoIconBase64(miniImagem);
				pessoa.setExtensao(extensao);
			
			}
			
				if (pessoa != null) {
				pessoa = daoGeneric.Merge(pessoa);
				pessoa = new Pessoa();
				carregarListaPessoas();
				mostrarMsg("Pessoa cadastrada com sucesso !");
			}
		} catch (Exception e) {
			mostrarMsg("Erro ao registrar");
			System.out.println("Mensagem: " + e.getMessage());
			System.out.println("Causa: " + e.getCause());
			System.out.println("Local: " + e.getLocalizedMessage());
			System.out.println("Classe: " + e.getClass());
		}

		return "";
	}
	
	
	

	public String excluirPessoa() {
		
		if(pessoa != null) {
			daoGeneric.deletepoId(pessoa);
			carregarListaPessoas();
			pessoa = new Pessoa();
			mostrarMsg("Pessoa removida com sucesso!");
		}else {
			carregarListaPessoas();
			mostrarMsg("Nenhuma pessoa selecionada!");
		}
		
		return "";

	}
	
	
	/*Método que converte um inputStream para array de byte*/
	private byte[] getBtye(InputStream inputStream) throws IOException {
		
		int len;
		int size =1024;
		
		byte[] buf = null;
		
		if(inputStream instanceof ByteArrayInputStream) {
			size = inputStream.available();
			buf = new byte[size];
			len = inputStream.read(buf,0, size);
		}else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			
			while((len = inputStream.read(buf,0, size)) != -1) {
				bos.write(buf, 0, len);
			}
			
			buf = bos.toByteArray();
		}
		
		return buf;
	}
	
	public void download() throws IOException {
		Map<String, String>params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileDownload = params.get("fileDownload");
		Pessoa pessoa = daoGeneric.consultar(Pessoa.class, fileDownload);
		
		/*Dando a resposta para o navegador*/
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
										.getExternalContext().getResponse();
		
		response.addHeader("Content-Disposition", "attachment; filename=download." + pessoa.getExtensao());
		response.setContentType("application/octet-stream");
		response.setContentLength(pessoa.getFotoIconBase64Original().length);
		response.getOutputStream().write(pessoa.getFotoIconBase64Original());
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	
	

	public void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}

	public String logar() {
		Pessoa pessoaLogado = daoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());
		if (pessoaLogado != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuarioLogado", pessoaLogado);
			return "home.jsf";
		}else {
			
			mostrarMsg("Usuário nao encontrado");
			
		}
		return "index.jsf";
	}

	@SuppressWarnings("static-access")
	public String deslogar() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuarioLogado");

		HttpServletRequest httpServletRequest = (HttpServletRequest) context.getCurrentInstance().getExternalContext()
				.getRequest();
		httpServletRequest.getSession().invalidate();

		return "index.jsf";
	}
	
	public void buscarEstadoporID() {
		System.out.println("okaoksoksoksoa");
	}

	@SuppressWarnings("unchecked")
	public void carregaCidadeAcordoEstado(AjaxBehaviorEvent event) {

		Estado estado = (Estado) ((SelectOneMenu) event.getSource()).getValue();

		if (estado != null) {
			pessoa.setEstado(estado);

			List<Cidade> listCidades = jpaUtil.getEntityManager()
					.createQuery("from Cidade where estado.id =" + estado.getId() + "order by localidade")
					.getResultList();
			List<SelectItem> itemsCidade = new ArrayList<SelectItem>();
			for (Cidade cid : listCidades) {
				itemsCidade.add(new SelectItem(cid, cid.getLocalidade()));
			}

			setCidades(itemsCidade);
		} else {
			mostrarMsg("Selecione um estado!");
		}
	}

	@SuppressWarnings("unchecked")
	public String editarPessoa() {
		if (pessoa.getCidade() != null) {
			Estado estado = pessoa.getCidade().getEstado();
			pessoa.setEstado(estado);
			List<Cidade> listaCidades = jpaUtil.getEntityManager()
					.createQuery("from Cidade where estado.id = " + estado.getId() + "order by localidade")
					.getResultList();

			List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();

			for (Cidade cidade : listaCidades) {
				selectItemsCidade.add(new SelectItem(cidade, cidade.getLocalidade()));
			}
			setCidades(selectItemsCidade);
		}

		return "";
	}

	public boolean permiteAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");

		return pessoaUser.getIsSuperAdmin().equals(acesso);
	}

	@PostConstruct
	public void carregarListaPessoas() {
		listaPessoas = daoGeneric.getListEntityLimit10(Pessoa.class);
	}
	
	
	public PessoaBean() {
	
	
	}
	
	

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DaoGeneric<Pessoa> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public List<Pessoa> getListaPessoas() {
		return listaPessoas;
	}

	public void setListaPessoas(List<Pessoa> listaPessoas) {
		this.listaPessoas = listaPessoas;
	}

	public List<SelectItem> getCidades() {
		return cidades;
	}

	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}

	public List<SelectItem> getEstados() {
		estados = daoPessoa.listarEstados();
		return estados;
	}

	public void setPessoaPerfils(List<SelectItem> pessoaPerfils) {
		this.pessoaPerfils = pessoaPerfils;
	}

	public List<SelectItem> getPessoaPerfils() {

		pessoaPerfils = daoPessoa.listarPerfils();
		return pessoaPerfils;
	}

	public void setArquivoFoto(Part arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}

	public Part getArquivoFoto() {
		return arquivoFoto;
	}
	
	
	
	
}
