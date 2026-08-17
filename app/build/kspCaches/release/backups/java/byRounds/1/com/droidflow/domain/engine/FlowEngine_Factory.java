package com.droidflow.domain.engine;

import android.content.Context;
import com.droidflow.data.local.FlowDao;
import com.droidflow.data.local.HistoryDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class FlowEngine_Factory implements Factory<FlowEngine> {
  private final Provider<Context> contextProvider;

  private final Provider<FlowDao> flowDaoProvider;

  private final Provider<HistoryDao> historyDaoProvider;

  public FlowEngine_Factory(Provider<Context> contextProvider, Provider<FlowDao> flowDaoProvider,
      Provider<HistoryDao> historyDaoProvider) {
    this.contextProvider = contextProvider;
    this.flowDaoProvider = flowDaoProvider;
    this.historyDaoProvider = historyDaoProvider;
  }

  @Override
  public FlowEngine get() {
    return newInstance(contextProvider.get(), flowDaoProvider.get(), historyDaoProvider.get());
  }

  public static FlowEngine_Factory create(Provider<Context> contextProvider,
      Provider<FlowDao> flowDaoProvider, Provider<HistoryDao> historyDaoProvider) {
    return new FlowEngine_Factory(contextProvider, flowDaoProvider, historyDaoProvider);
  }

  public static FlowEngine newInstance(Context context, FlowDao flowDao, HistoryDao historyDao) {
    return new FlowEngine(context, flowDao, historyDao);
  }
}
