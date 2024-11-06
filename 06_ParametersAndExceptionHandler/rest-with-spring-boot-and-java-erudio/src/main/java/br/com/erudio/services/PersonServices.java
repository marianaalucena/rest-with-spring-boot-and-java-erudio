package br.com.erudio.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import br.com.erudio.controllers.PersonController;
import br.com.erudio.data.vo.v1.PersonVo;
import br.com.erudio.data.vo.v2.PersonVoV2;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
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
		var persons = DozerMapper.parseListObjects(repository.findAll(), PersonVo.class);
		persons
		.stream()
		.forEach(p -> {
			try {
				p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		return persons;
	}

	public PersonVo findById(Long id) throws Exception {
		logger.info("Finding one person!");
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID")); 

		PersonVo vo = DozerMapper.parseObject(entity, PersonVo.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel()); //withSelfRel autorelacionamento
		return vo;
	}
	
	public PersonVo create(PersonVo person) throws Exception {
		if(person == null) throw new RequiredObjectIsNullException();
		
		logger.info("Creating one person!");
		var entity = DozerMapper.parseObject(person, Person.class); //Person é o que o bd reconhece
		PersonVo vo = DozerMapper.parseObject(repository.save(entity), PersonVo.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel()); //withSelfRel autorelacionamento
		return vo;
		
	}
	
	public PersonVoV2 createV2(PersonVoV2 person) {
		logger.info("Creating one person!");
		var entity = mapper.convertVoToEntity(person); //Person é o que o bd reconhece
		var vo = mapper.convertEntityToVo(entity);
		return vo;
		
	}
	
	public PersonVo update(PersonVo person) throws Exception {
		if(person == null) throw new RequiredObjectIsNullException();
		
		logger.info("Updating one person!");
		
		var entity = repository.findById(person.getKey())
		.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
	
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		PersonVo vo = DozerMapper.parseObject(repository.save(entity), PersonVo.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel()); //withSelfRel autorelacionamento
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person!");
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		repository.delete(entity);
	}
	
}
