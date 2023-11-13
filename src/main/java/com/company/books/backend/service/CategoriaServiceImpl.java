package com.company.books.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.company.books.backend.model.Categoria;
import com.company.books.backend.model.dao.ICategoriaDao;
import com.company.books.backend.reponse.CategoriaResponseRest;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaServiceImpl implements ICategoriaService {

    private static final Logger log = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    @Autowired
    private ICategoriaDao categoriaDAO;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CategoriaResponseRest> buscarCategorias() {

        log.info("inicio metodo buscarCategorias()");

        CategoriaResponseRest response = new CategoriaResponseRest();

        try {
            List<Categoria> categoria = (List<Categoria>) categoriaDAO.findAll();

            if (categoria.isEmpty())
                log.info("esta vacia la cat");

            response.getCategoriaResponse().setCategoria(categoria);

            response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");

        } catch (Exception e) {
            response.setMetadata("Respuesta no ok", "-1", "Error al consultar categorias");
            log.error("el error al consultar categorias: ", e.getMessage());
            e.getStackTrace();
            return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.OK); // devuelve 200
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CategoriaResponseRest> buscarPorId(Long id) {

        log.info("Inicio método buscarPorId");

        CategoriaResponseRest response = new CategoriaResponseRest();

        List<Categoria> list = new ArrayList<>();
        try {

            Optional<Categoria> categoria = categoriaDAO.findById(id);
            
            if(categoria.isPresent()) {
            	list.add(categoria.get());
            	response.getCategoriaResponse().setCategoria(list);
            	
            } else {
            	log.error("Error en consultar categoria");
            	response.setMetadata("Respuesta nok", "-1", "Categoria no encontrada");
                return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
        	log.error("Error en consultar categoria");
        	response.setMetadata("Respuesta nok", "-1", "Categoria no encontrada");
            return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
        return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.OK); // devuelve 200

    }

	@Override
	public ResponseEntity<CategoriaResponseRest> crear(Categoria categoria) {
		log.info("Inicio método crear categoria");

        CategoriaResponseRest response = new CategoriaResponseRest();

        List<Categoria> list = new ArrayList<>();
        
        try {
        	
        	Categoria categoriaGuardada = categoriaDAO.save(categoria);
        	
        	if(categoriaGuardada != null) {
        		list.add(categoria);
            	response.getCategoriaResponse().setCategoria(list);
        	}
        	
        	
        	
        } catch(Exception e) {
        	log.error("Error en crear categoria");
        	response.setMetadata("Respuesta nok", "-1", "Categoria no grabada");
            return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        response.setMetadata("Respuesta ok", "00", "Categoria creada");
        return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.OK);
		
	}

}
