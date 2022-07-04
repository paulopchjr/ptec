package br.com.ptec.filtro;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.ptec.entidades.Pessoa;
import br.com.ptec.util.JpaUtil;

@WebFilter("/*")
public class FiltroAutenticacaoLogin extends HttpFilter implements Filter {

	
	private static final long serialVersionUID = 1L;

	@Inject
	private JpaUtil jpaUtil;
	
	public FiltroAutenticacaoLogin() {
		super();
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpSession session = httpServletRequest.getSession();
		Pessoa pessoaUsuarioLogado = (Pessoa) session.getAttribute("usuarioLogado");

		String urlParaAutenticar = httpServletRequest.getServletPath();

		if (!urlParaAutenticar.equalsIgnoreCase("index.jsf") && pessoaUsuarioLogado == null) {
			request.setAttribute("msg", "Por favor realize o login");
			request.getRequestDispatcher("/index.jsf").forward(request, response);
			return;
		} else {

			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		jpaUtil.getEntityManager();
	}

}
