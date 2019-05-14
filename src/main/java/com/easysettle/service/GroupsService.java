package com.easysettle.service;

import com.easysettle.domain.Groups;
import com.easysettle.domain.Members;
import com.easysettle.repository.GroupsRepository;
import com.easysettle.repository.MembersRepository;
import com.easysettle.service.dto.GroupsWithMembers;
import com.easysettle.service.dto.PaymentsAllInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing Groups.
 */
@Service
@Transactional
public class GroupsService {

    private final Logger log = LoggerFactory.getLogger(GroupsService.class);

    private final GroupsRepository groupsRepository;

    private final MembersService membersService ;

    public GroupsService(GroupsRepository groupsRepository, MembersService membersService) {
        this.groupsRepository = groupsRepository;
        this.membersService = membersService;
    }

    public List<GroupsWithMembers> getGroupsWithMembers(){

        List<GroupsWithMembers> groupsWithMembersList = new ArrayList<>();

        List<Groups> groupsList = groupsRepository.findAll();
        for (Groups group : groupsList){
            List<Members> membersList = membersService.getMembersByGroup(group.getId());

            GroupsWithMembers groupsWithMembers = GroupsWithMembers.builder()
                .id(group.getId())
                .name(group.getName())
                .members(membersList)
                .build();

            groupsWithMembersList.add(groupsWithMembers);
        }
        return groupsWithMembersList;
    }

    /**
     * Save a groups.
     *
     * @param groups the entity to save
     * @return the persisted entity
     */
    public Groups save(Groups groups) {
        log.debug("Request to save Groups : {}", groups);
        return groupsRepository.save(groups);
    }

    /**
     * Get all the groups.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Groups> findAll() {
        log.debug("Request to get all Groups");
        return groupsRepository.findAll();
    }

    /**
     * Get one groups by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Groups findOne(Long id) {
        log.debug("Request to get Groups : {}", id);
        return groupsRepository.findOne(id);
    }

    /**
     * Delete the groups by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Groups : {}", id);
        groupsRepository.delete(id);
    }
}
