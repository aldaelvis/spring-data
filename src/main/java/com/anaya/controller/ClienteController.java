package com.anaya.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anaya.entity.Cliente;
import com.anaya.service.IClienteService;
import com.anaya.util.PageRender;

/*Implementamos una session para guardar 
 * la informacion 
 * del cliente que va hacer editado
 * */
@Controller
@SessionAttributes("cliente")
public class ClienteController {

	/* Inyectamos el servicio */
	@Autowired
	private IClienteService clienteService;

	@RequestMapping(value = "/listar")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Pageable pageable = new PageRequest(page, 5);

		Page<Cliente> clientes = clienteService.findAll(pageable);
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		/* Enviando a la vista datos */
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);

		/* retornamos la vista */
		return "listar";
	}
	
	/*Ver un cliente segun el ID*/
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable("id")Long id, Model model, RedirectAttributes redirect) {
		Cliente cliente = clienteService.findOne(id);
		if(cliente.getId() == null) {
			redirect.addFlashAttribute("error", "El cliente no existe en BD");
			return "redirect:/listar";
		}
		model.addAttribute("titulo", "Detalle cliente: " + cliente.getNombre());
		model.addAttribute("cliente", cliente);

		return "ver";
	}

	/* Asignar el cliente a la vista form */
	@RequestMapping("/form")
	public String crear(Model model) {
		Cliente cliente = new Cliente();

		model.addAttribute("titulo", "Formulario Cliente");
		model.addAttribute("cliente", cliente);

		/* retornamos la vista */
		return "form";
	}

	/* Guardar un cliente, recibir datos desde la vista(form) */
	@PostMapping("/form")
	public String guardar(@Valid Cliente cliente, BindingResult result, RedirectAttributes redirect,
			SessionStatus status) {
		if (result.hasErrors()) {
			return "form";
		}

		String mensaje = (cliente.getId() != null) ? "Cliente editado con exito." : "Cliente guardado con exito";
		clienteService.save(cliente);
		status.setComplete();
		redirect.addFlashAttribute("info", mensaje);

		return "redirect:/listar";
	}

	/* Implementar el metodo editar */
	@GetMapping("/form/{id}")
	public String editar(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {

		Cliente cliente;
		cliente = clienteService.findOne(id);
		System.out.println(cliente);
		if (cliente == null) {
			redirect.addFlashAttribute("error", "El Id del cliente no exite en la BD");
			return "redirect:/listar";
		}

		model.addAttribute("titulo", "Editar Cliente");
		model.addAttribute("cliente", cliente);

		return "form";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable("id")Long id, RedirectAttributes redirect) {
		if(id != null) {
//			Cliente cliente = clienteService.findOne(id);
			clienteService.delete(id);
			redirect.addFlashAttribute("success", "Cliente eliminado con exito");
		}
		
		return "redirect:/listar";
	}

}
