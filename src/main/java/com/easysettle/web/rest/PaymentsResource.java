package com.easysettle.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.easysettle.domain.Payments;
import com.easysettle.domain.Transfers;
import com.easysettle.domain.type.ActionResultStatus;
import com.easysettle.service.MembersService;
import com.easysettle.service.PaymentsService;
import com.easysettle.service.TransfersService;
import com.easysettle.service.dto.ActionResult;
import com.easysettle.service.dto.NewPaymentRequest;
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
 * REST controller for managing Payments.
 */
@RestController
@RequestMapping("/api")
public class PaymentsResource {

    private final Logger log = LoggerFactory.getLogger(PaymentsResource.class);

    private static final String ENTITY_NAME = "payments";

    private final PaymentsService paymentsService;

    private final TransfersService transfersService;

    private final MembersService membersService;

    public PaymentsResource(PaymentsService paymentsService, TransfersService transfersService, MembersService membersService) {
        this.paymentsService = paymentsService;
        this.transfersService = transfersService;
        this.membersService = membersService;
    }

    @PostMapping("/payments/newPayment")
    public ResponseEntity<ActionResult> newPayment(@Valid @RequestBody NewPaymentRequest request) throws URISyntaxException {
        try {
            log.debug("REST request to save Payments");
            Payments payments = paymentsService.newPayment(request);
            transfersService.saveTransfers(payments, request);
            membersService.changeMemberBalance(request);
            ActionResult result = ActionResult.builder().result(ActionResultStatus.SUCCESS.toString()).messageError(null).build();
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            ActionResult result = ActionResult.builder().result(ActionResultStatus.ERROR.toString()).messageError("Error adding payments").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/payments/getAllPayments")
    public List<Payments> getAllPayments(@Valid @PathVariable Long groupId) {
        log.debug("GET all payments");
        List<Payments> paymentsList = paymentsService.getAllPayments(groupId);
        return paymentsList;
    }


    /**
     * POST  /payments : Create a new payments.
     *
     * @param payments the payments to create
     * @return the ResponseEntity with status 201 (Created) and with body the new payments, or with status 400 (Bad Request) if the payments has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payments")
    @Timed
    public ResponseEntity<Payments> createPayments(@Valid @RequestBody Payments payments) throws URISyntaxException {
        log.debug("REST request to save Payments : {}", payments);
        if (payments.getId() != null) {
            throw new BadRequestAlertException("A new payments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Payments result = paymentsService.save(payments);
        return ResponseEntity.created(new URI("/api/payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payments : Updates an existing payments.
     *
     * @param payments the payments to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated payments,
     * or with status 400 (Bad Request) if the payments is not valid,
     * or with status 500 (Internal Server Error) if the payments couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payments")
    @Timed
    public ResponseEntity<Payments> updatePayments(@Valid @RequestBody Payments payments) throws URISyntaxException {
        log.debug("REST request to update Payments : {}", payments);
        if (payments.getId() == null) {
            return createPayments(payments);
        }
        Payments result = paymentsService.save(payments);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, payments.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payments : get all the payments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of payments in body
     */
    @GetMapping("/payments")
    @Timed
    public List<Payments> getAllPayments() {
        log.debug("REST request to get all Payments");
        return paymentsService.findAll();
    }

    /**
     * GET  /payments/:id : get the "id" payments.
     *
     * @param id the id of the payments to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the payments, or with status 404 (Not Found)
     */
    @GetMapping("/payments/{id}")
    @Timed
    public ResponseEntity<Payments> getPayments(@PathVariable Long id) {
        log.debug("REST request to get Payments : {}", id);
        Payments payments = paymentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(payments));
    }

    /**
     * DELETE  /payments/:id : delete the "id" payments.
     *
     * @param id the id of the payments to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payments/{id}")
    @Timed
    public ResponseEntity<Void> deletePayments(@PathVariable Long id) {
        log.debug("REST request to delete Payments : {}", id);
        paymentsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
