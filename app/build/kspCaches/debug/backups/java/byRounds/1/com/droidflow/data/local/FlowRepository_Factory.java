package com.droidflow.data.local;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class FlowRepository_Factory implements Factory<FlowRepository> {
  private final Provider<FlowDao> flowDaoProvider;

  private final Provider<HistoryDao> historyDaoProvider;

  public FlowRepository_Factory(Provider<FlowDao> flowDaoProvider,
      Provider<HistoryDao> historyDaoProvider) {
    this.flowDaoProvider = flowDaoProvider;
    this.historyDaoProvider = historyDaoProvider;
  }

  @Override
  public FlowRepository get() {
    return newInstance(flowDaoProvider.get(), historyDaoProvider.get());
  }

  public static FlowRepository_Factory create(Provider<FlowDao> flowDaoProvider,
      Provider<HistoryDao> historyDaoProvider) {
    return new FlowRepository_Factory(flowDaoProvider, historyDaoProvider);
  }

  public static FlowRepository newInstance(FlowDao flowDao, HistoryDao historyDao) {
    return new FlowRepository(flowDao, historyDao);
  }
}
