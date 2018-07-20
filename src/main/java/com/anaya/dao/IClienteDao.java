package com.anaya.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.anaya.entity.Cliente;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long>  {
	

}
