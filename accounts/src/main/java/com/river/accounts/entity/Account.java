package com.river.accounts.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "accounts")
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class Account extends BaseEntity {

    @Id
    private Integer accountNumber;

    private String accountType;

    private String branchAddress;

    private Integer customerId;

}
