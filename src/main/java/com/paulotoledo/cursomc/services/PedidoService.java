package com.paulotoledo.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paulotoledo.cursomc.domain.ItemPedido;
import com.paulotoledo.cursomc.domain.PagamentoComBoleto;
import com.paulotoledo.cursomc.domain.Pedido;
import com.paulotoledo.cursomc.domain.enums.EstadoPagamento;
import com.paulotoledo.cursomc.repositories.ItemPedidoRepository;
import com.paulotoledo.cursomc.repositories.PagamentoRepository;
import com.paulotoledo.cursomc.repositories.PedidoRepository;
import com.paulotoledo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired	private PedidoRepository repo;
	
	@Autowired	private BoletoService boletoService;
	
	@Autowired	private PagamentoRepository pagamentoRepository;
	
	@Autowired	private ProdutoService produtoService; 
	
	@Autowired	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired	private ClienteService clienteService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id +
				", Tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		
		/*
		 * usar ClienteService ao invés de ClienteRepository
		 */
		
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			/*
			 * continuar usando o produtoService aqui
			 */
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		System.out.println(obj);
		return obj;
		
	}

}
