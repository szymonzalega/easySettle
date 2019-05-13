package com.easysettle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Payments.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "payments")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Payments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_date")
    private LocalDate date;

    @NotNull
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @NotNull
    @Column(name = "payer_id", nullable = false)
    private Long payer_id;

    @OneToMany(mappedBy = "payments")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Transfers> transfers;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public Payments amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public Payments name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Payments date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getGroup_id() {
        return groupId;
    }

    public Payments groupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }

    public void setGroup_id(Long groupId) {
        this.groupId = groupId;
    }

    public Long getPayer_id() {
        return payer_id;
    }

    public Payments payer_id(Long payer_id) {
        this.payer_id = payer_id;
        return this;
    }

    public void setPayer_id(Long payer_id) {
        this.payer_id = payer_id;
    }

    public Set<Transfers> getTransfers() {
        return transfers;
    }

    public Payments transfers(Set<Transfers> transfers) {
        this.transfers = transfers;
        return this;
    }

    public Payments addTransfers(Transfers transfers) {
        this.transfers.add(transfers);
        transfers.setPayments(this);
        return this;
    }

    public Payments removeTransfers(Transfers transfers) {
        this.transfers.remove(transfers);
        transfers.setPayments(null);
        return this;
    }

    public void setTransfers(Set<Transfers> transfers) {
        this.transfers = transfers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Payments payments = (Payments) o;
        if (payments.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payments.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Payments{" +
            "id=" + id +
            ", amount=" + amount +
            ", name='" + name + '\'' +
            ", date=" + date +
            ", groupId=" + groupId +
            ", payer_id=" + payer_id +
            ", transfers=" + transfers +
            '}';
    }
}
