package com.infrastructure.persistence.repositories;

import com.infrastructure.persistence.entities.AccountEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountRepository implements PanacheRepositoryBase<AccountEntity, String> {
}
