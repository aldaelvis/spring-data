package com.anaya.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anaya.entity.Cliente;
import com.anaya.entity.Factura;
import com.anaya.entity.ItemFactura;
import com.anaya.entity.Producto;
import com.anaya.service.IClienteService;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value="id")Long id,Model model,
			RedirectAttributes redirectAttributes) {
		
		Factura factura = clienteService.findFacturaById(id);
		if(factura==null) {
			redirectAttributes.addFlashAttribute("error", "La factura no exite el BD");
			return "redirect:/listar";
		}
		model.addAttribute("titulo","Factura: ".concat(factura.getDescripcion()));
		model.addAttribute("factura",factura);
		
		return "factura/ver";
		
	}
	
	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable("clienteId")Long clienteId,
			Model model, RedirectAttributes redirectAttributes) {
		
		Cliente cliente = clienteService.findOne(clienteId);
		if(cliente == null) {
			redirectAttributes.addFlashAttribute("error", "El cliente no existe en la BD");
			return "redirect:/listar";
		}
		
		Factura factura = new Factura();
		factura.setCliente(cliente);
		
		model.addAttribute("titulo", "Crear Factura");
		model.addAttribute("factura", factura);
		
		return "factura/form";
	}
	
	@GetMapping(value="/cargar-productos/{term}", produces= {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable("term")String term) {
		return clienteService.findByNombre(term);
	}
	
	@PostMapping("/form")
	public String guardar(@Valid Factura factura, BindingResult result,@RequestParam(name="item_id[]", required=false)Long[] itemId,
			@RequestParam(name="cantidad[]", required=false)Integer[] cantidad,
			RedirectAttributes redirectAttributes, 
			SessionStatus status, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Crear Factura");
			return "factura/form";
		}
		if(itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", "Crear Factura");
			model.addAttribute("error", "Error: La factura no puede no tener lineas");
			return "factura/form";
		}
		
		for (int i = 0; i < itemId.length; i++) {
			Producto producto = clienteService.findProductoById(itemId[i]);
			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItemFactura(linea);
		}
		
		clienteService.saveFactura(factura);
		status.setComplete();
		redirectAttributes.addFlashAttribute("success","Factura creada con exito");
		
		return "redirect:/ver/" + factura.getCliente().getId();
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable("id")Long id, RedirectAttributes redirectAttributes) {
		Factura factura = clienteService.findFacturaById(id);
		if(factura != null) {
			clienteService.deleteFactura(id);
			redirectAttributes.addFlashAttribute("success","Factura eliminada con exito");
			return "redirect:/ver/" + factura.getCliente().getId();
		} else {
			redirectAttributes.addFlashAttribute("error","FLa factura no exite en la BD");
		}
		return "redirect:/listar";
	}

}
