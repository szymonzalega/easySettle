package com.easysettle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.easysettle.domain.Transfers;
import com.easysettle.service.TransfersService;
import com.easysettle.web.rest.errors.BadRequestAlertException;
import com.easysettle.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Transfers.
 */
@RestController
@RequestMapping("/api")
public class TransfersResource {

    private final Logger log = LoggerFactory.getLogger(TransfersResource.class);

    private static final String ENTITY_NAME = "transfers";

    private final TransfersService transfersService;

    public TransfersResource(TransfersService transfersService) {
        this.transfersService = transfersService;
    }

    /**
     * POST  /transfers : Create a new transfers.
     *
     * @param transfers the transfers to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transfers, or with status 400 (Bad Request) if the transfers has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transfers")
    @Timed
    public ResponseEntity<Transfers> createTransfers(@Valid @RequestBody Transfers transfers) throws URISyntaxException {
        log.debug("REST request to save Transfers : {}", transfers);
        if (transfers.getId() != null) {
            throw new BadRequestAlertException("A new transfers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Transfers result = transfersService.save(transfers);
        return ResponseEntity.created(new URI("/api/transfers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transfers : Updates an existing transfers.
     *
     * @param transfers the transfers to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transfers,
     * or with status 400 (Bad Request) if the transfers is not valid,
     * or with status 500 (Internal Server Error) if the transfers couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transfers")
    @Timed
    public ResponseEntity<Transfers> updateTransfers(@Valid @RequestBody Transfers transfers) throws URISyntaxException {
        log.debug("REST request to update Transfers : {}", transfers);
        if (transfers.getId() == null) {
            return createTransfers(transfers);
        }
        Transfers result = transfersService.save(transfers);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transfers.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transfers : get all the transfers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transfers in body
     */
    @GetMapping("/transfers")
    @Timed
    public List<Transfers> getAllTransfers() {
        log.debug("REST request to get all Transfers");
        return transfersService.findAll();
        }

    /**
     * GET  /transfers/:id : get the "id" transfers.
     *
     * @param id the id of the transfers to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transfers, or with status 404 (Not Found)
     */
    @GetMapping("/transfers/{id}")
    @Timed
    public ResponseEntity<Transfers> getTransfers(@PathVariable Long id) {
        log.debug("REST request to get Transfers : {}", id);
        Transfers transfers = transfersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transfers));
    }

    /**
     * DELETE  /transfers/:id : delete the "id" transfers.
     *
     * @param id the id of the transfers to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transfers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransfers(@PathVariable Long id) {
        log.debug("REST request to delete Transfers : {}", id);
        transfersService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
