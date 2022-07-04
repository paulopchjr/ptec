package br.com.ptec.repository;

import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import br.com.ptec.entidades.OrdemServico;

public interface IDaoOrdemServico {

	List<SelectItem>listPessoas();
	
	List<SelectItem>listarServicos();
	
	List<OrdemServico>consultarLimit10(Long codigoUsuario);
	
	List<OrdemServico>relatorioOrdemServicos(Long idServico, Date dataInicial, Date dataFinal);
	
}
