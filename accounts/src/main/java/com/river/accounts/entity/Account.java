package com.river.accounts.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "accounts")
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class Account extends BaseEntity {

    @Id
    private Long accountNumber;

    private String accountType;

    private String branchAddress;

    private Integer customerId;

    private Boolean communicationSw;

}
