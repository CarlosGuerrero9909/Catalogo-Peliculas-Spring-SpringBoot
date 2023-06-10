package com.cguerrero.peliculas.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.print.attribute.standard.Media;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cguerrero.peliculas.entities.Actor;
import com.cguerrero.peliculas.entities.Pelicula;
import com.cguerrero.peliculas.services.IActorService;
import com.cguerrero.peliculas.services.IArchivoService;
import com.cguerrero.peliculas.services.IGeneroService;
import com.cguerrero.peliculas.services.IPeliculaService;

import jakarta.validation.Valid;

@Controller
public class PeliculaController {

	private IPeliculaService peliculaService;
	private IGeneroService generoService;
	private IActorService actorService;
	private IArchivoService archivoService;

	public PeliculaController(IPeliculaService peliculaService, IGeneroService generoService,
			IActorService actorService, IArchivoService archivoService) {
		this.peliculaService = peliculaService;
		this.generoService = generoService;
		this.actorService = actorService;
		this.archivoService = archivoService;
	}

	@GetMapping("/pelicula")
	public String crear(Model model) {
		Pelicula pelicula = new Pelicula();
		model.addAttribute("pelicula", pelicula);
		model.addAttribute("generos", generoService.findAll());
		model.addAttribute("actores", actorService.findAll());
		model.addAttribute("titulo", "Nueva Pelicula");
		return "pelicula";
	}

	@GetMapping("/pelicula/{id}")
	public String editar(@PathVariable(name = "id") Long id, Model model) {
		Pelicula pelicula = peliculaService.findById(id);
		String ids = "";

		for (Actor actor : pelicula.getProtagonistas()) {
			if ("".equals(ids)) {
				ids = actor.getId().toString();
			} else {
				ids += ids + "," + actor.getId().toString();
			}
		}

		model.addAttribute("pelicula", pelicula);
		model.addAttribute("ids", ids);
		model.addAttribute("generos", generoService.findAll());
		model.addAttribute("actores", actorService.findAll());
		model.addAttribute("titulo", "Editar Pelicula");
		return "pelicula";
	}

	@PostMapping("/pelicula")
	public String guardar(@Valid Pelicula pelicula, BindingResult br, @ModelAttribute(name = "ids") String ids,
			Model model, @RequestParam("archivo") MultipartFile imagen) {
		// BindingResult es donde spring almacena el resultado de las validaciones del
		// formulario
		if (br.hasErrors()) {
			model.addAttribute("generos", generoService.findAll());
			model.addAttribute("actores", actorService.findAll());
			return "pelicula";
		}

		if (!imagen.isEmpty()) {
			String archivo = pelicula.getNombre() + getExtension(imagen.getOriginalFilename());
			pelicula.setImagen(archivo);
			try {
				archivoService.guardar(archivo, imagen.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			pelicula.setImagen("_default.jpg");
		}

		if (ids != null && !ids.equals("")) {
			List<Long> idsProtagonistas = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
			List<Actor> protagonistas = actorService.findAllById(idsProtagonistas);
			pelicula.setProtagonistas(protagonistas);
		}

		peliculaService.save(pelicula);
		return "redirect:home";
	}

	private String getExtension(String archivo) {
		return archivo.substring(archivo.lastIndexOf("."));
	}

	@GetMapping({ "/", "/home", "/index" })
	public String home(Model model, @RequestParam(name="pagina", required=false, defaultValue="0") Integer pagina) {

		PageRequest pr = PageRequest.of(pagina, 12);
		Page<Pelicula> page = peliculaService.findAll(pr);

		model.addAttribute("peliculas", page.getContent());

		if (page.getTotalPages() > 0) {
			List<Integer> paginas = IntStream.rangeClosed(1, page.getTotalPages()).boxed().toList();
			model.addAttribute("paginas", paginas);
		}

		model.addAttribute("actual", pagina + 1);
		model.addAttribute("titulo", "Catalogo de peliculas");

		// model.addAttribute("msj", "La app esta en mantenimiento");
		// model.addAttribute("tipoMsj", "danger");

		return "home";
	}

	@GetMapping("listado")
	public String listado(Model model, @RequestParam(required = false) String msg,
			@RequestParam(required = false) String tipoMsg) {
		model.addAttribute("titulo", "Listado de Peliculas");
		model.addAttribute("peliculas", peliculaService.findAll());

		if (!"".equals(tipoMsg) && !"".equals(msg)) {
			model.addAttribute("msj", msg);
			model.addAttribute("tipoMsj", tipoMsg);
		}

		return "listado";
	}

	@GetMapping("/pelicula/{id}/delete")
	public String eliminar(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAtt) {
		peliculaService.delete(id);

		redirectAtt.addAttribute("msg", "La pelicula fue eliminada correctamente");
		redirectAtt.addAttribute("tipoMsg", "success");

		return "redirect:/listado";
	}

}
