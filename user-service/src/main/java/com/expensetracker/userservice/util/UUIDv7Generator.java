package com.expensetracker.userservice.util;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.lang.NonNull;

import com.github.f4b6a3.uuid.UuidCreator;

public class UUIDv7Generator implements IdentifierGenerator {

  @Override
  @NonNull
  public Serializable generate(SharedSessionContractImplementor session, Object object)
      throws HibernateException {
    return UuidCreator.getTimeOrderedEpoch();
  }
}
