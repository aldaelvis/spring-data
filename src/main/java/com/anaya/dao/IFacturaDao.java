package com.anaya.dao;

import org.springframework.data.repository.CrudRepository;

import com.anaya.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura, Long> {
	

}
