package com.easysettle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.easysettle.domain.Members;
import com.easysettle.domain.type.ActionResultStatus;
import com.easysettle.service.MembersService;
import com.easysettle.service.dto.ActionResult;
import com.easysettle.service.dto.SettleDebtResult;
import com.easysettle.web.rest.errors.BadRequestAlertException;
import com.easysettle.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Members.
 */
@RestController
@RequestMapping("/api")
public class MembersResource {

    private final Logger log = LoggerFactory.getLogger(MembersResource.class);

    private static final String ENTITY_NAME = "members";

    private final MembersService membersService;

    public MembersResource(MembersService membersService) {
        this.membersService = membersService;
    }

    /**
     * POST  /members : Create a new members.
     *
     * @param members the members to create
     * @return the ResponseEntity with status 201 (Created) and with body the new members, or with status 400 (Bad Request) if the members has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/members")
    @Timed
    public ResponseEntity<Members> createMembers(@Valid @RequestBody Members members) throws URISyntaxException {
        log.debug("REST request to save Members : {}", members);
        if (members.getId() != null) {
            throw new BadRequestAlertException("A new members cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Members result = membersService.save(members);
        return ResponseEntity.created(new URI("/api/members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /members : Updates an existing members.
     *
     * @param members the members to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated members,
     * or with status 400 (Bad Request) if the members is not valid,
     * or with status 500 (Internal Server Error) if the members couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/members")
    @Timed
    public ResponseEntity<Members> updateMembers(@Valid @RequestBody Members members) throws URISyntaxException {
        log.debug("REST request to update Members : {}", members);
        if (members.getId() == null) {
            return createMembers(members);
        }
        Members result = membersService.save(members);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, members.getId().toString()))
            .body(result);
    }

    /**
     * GET  /members : get all the members.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of members in body
     */
    @GetMapping("/members")
    @Timed
    public List<Members> getAllMembers() {
        log.debug("REST request to get all Members");
        return membersService.findAll();
        }

    /**
     * GET  /members/:id : get the "id" members.
     *
     * @param id the id of the members to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the members, or with status 404 (Not Found)
     */
    @GetMapping("/members/{id}")
    @Timed
    public ResponseEntity<Members> getMembers(@PathVariable Long id) {
        log.debug("REST request to get Members : {}", id);
        Members members = membersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(members));
    }

    /**
     * DELETE  /members/:id : delete the "id" members.
     *
     * @param id the id of the members to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/members/{id}")
    @Timed
    public ResponseEntity<Void> deleteMembers(@PathVariable Long id) {
        log.debug("REST request to delete Members : {}", id);
        membersService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/members/settleDebt/{groupId}")
    public List<SettleDebtResult> settleDebt(@Valid @PathVariable Long groupId) {
        List<SettleDebtResult> settleDebtResultList = membersService.settleDebts(groupId);
        return settleDebtResultList;
    }

    @PostMapping("members/saveMembers")
    public ResponseEntity<ActionResult> saveMembers(@Valid @RequestBody List<Members> membersList) throws URISyntaxException {
        try {
            log.debug("REST request to save Members list");
            membersService.saveMembers(membersList);
            ActionResult result = ActionResult.builder().result(ActionResultStatus.SUCCESS.toString()).messageError(null).build();
            return ResponseEntity.ok(result);
        } catch(Exception ex) {
            ActionResult result = ActionResult.builder().result(ActionResultStatus.ERROR.toString()).messageError("Error adding payments").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/members/getMembersByGroup/{groupId}")
    public List<Members> getMembersByGroup(@PathVariable Long groupId) {
        List<Members> membersList = membersService.getMembersByGroup(groupId);
        return membersList;
    }
}
