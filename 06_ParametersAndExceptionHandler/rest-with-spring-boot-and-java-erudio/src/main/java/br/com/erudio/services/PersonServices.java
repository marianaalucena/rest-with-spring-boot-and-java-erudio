package br.com.erudio.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.data.vo.v1.PersonVo;
import br.com.erudio.data.vo.v2.PersonVoV2;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.mapper.custom.PersonMapper;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;

@Service
public class PersonServices {
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	@Autowired
	PersonMapper mapper;
	
	public List<PersonVo> findAll() {
		return DozerMapper.parseListObjects(repository.findAll(), PersonVo.class);
	}

	public PersonVo findById(Long id) {
		logger.info("Finding one person!");
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID")); 
		return DozerMapper.parseObject(entity, PersonVo.class);
	}
	
	public PersonVo create(PersonVo person) {
		logger.info("Creating one person!");
		var entity = DozerMapper.parseObject(person, Person.class); //Person é o que o bd reconhece
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVo.class);
		return vo;
		
	}
	
	public PersonVoV2 createV2(PersonVoV2 person) {
		logger.info("Creating one person!");
		var entity = mapper.convertVoToEntity(person); //Person é o que o bd reconhece
		var vo = mapper.convertEntityToVo(entity);
		return vo;
		
	}
	
	public PersonVo update(PersonVo person) {
		logger.info("Updating one person!");
		
		var entity = repository.findById(person.getId())
		.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
	
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVo.class);
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person!");
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		repository.delete(entity);
	}
	
}
