package com.application.usecase.openfinance;

import java.util.List;

import com.domain.entities.AccountEntity;
import com.domain.entities.AccountItemEntity;
import com.domain.gateway.openfinance.IOpenFinance;
import com.domain.gateway.openfinance.models.OpenFinanceAccountItem;
import com.domain.usecase.accountitem.ICreateAccountItemUseCase;
import com.domain.usecase.accountitem.IListAccountItemUseCase;
import com.domain.usecase.accountitem.IUpdateAccountItemUseCase;
import com.domain.usecase.openfinance.IAccountItemSynchronizerUseCase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AccountItemSynchronizerUseCase implements IAccountItemSynchronizerUseCase {

  @Inject
  private IOpenFinance openFinance;

  @Inject
  private IListAccountItemUseCase listAccountItemUseCase;

  @Inject
  private ICreateAccountItemUseCase createAccountItemUseCase;

  @Inject
  private IUpdateAccountItemUseCase updateAccountItemUseCase;

  @Override
  public void synchronizeAccountItems(AccountEntity account) {
    OpenFinanceAccountItem[] openFinanceAccountItems = openFinance.listAccountItems(account.getConnectionId());
    List<AccountItemEntity> accountItems = listAccountItemUseCase.listAccountItems(account.getId());

    for (OpenFinanceAccountItem openFinanceAccountItem : openFinanceAccountItems) {
      AccountItemEntity accountItem = accountItems.stream()
          .filter(item -> openFinanceAccountItem.getId().equals(item.getItemId()))
          .findFirst()
          .orElse(null);

      if (accountItem == null) {
        createAccountItemUseCase.createAccountItem(account, openFinanceAccountItem.getId(),
            openFinanceAccountItem.getName());
      } else {
        updateAccountItemUseCase.updateAccountItem(accountItem.getId(), openFinanceAccountItem.getName());
      }
    }
  }
}
