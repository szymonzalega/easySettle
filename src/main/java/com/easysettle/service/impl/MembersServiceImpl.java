package com.easysettle.service.impl;

import com.easysettle.service.MembersService;
import com.easysettle.domain.Members;
import com.easysettle.repository.MembersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Members.
 */
@Service
@Transactional
public class MembersServiceImpl implements MembersService {

    private final Logger log = LoggerFactory.getLogger(MembersServiceImpl.class);

    private final MembersRepository membersRepository;

    public MembersServiceImpl(MembersRepository membersRepository) {
        this.membersRepository = membersRepository;
    }

    /**
     * Save a members.
     *
     * @param members the entity to save
     * @return the persisted entity
     */
    @Override
    public Members save(Members members) {
        log.debug("Request to save Members : {}", members);
        return membersRepository.save(members);
    }

    /**
     * Get all the members.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Members> findAll() {
        log.debug("Request to get all Members");
        return membersRepository.findAll();
    }

    /**
     * Get one members by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Members findOne(Long id) {
        log.debug("Request to get Members : {}", id);
        return membersRepository.findOne(id);
    }

    /**
     * Delete the members by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Members : {}", id);
        membersRepository.delete(id);
    }
}
