package com.droidflow.ui.home;

import com.droidflow.data.local.FlowRepository;
import com.droidflow.domain.engine.AlarmScheduler;
import com.droidflow.domain.engine.FlowEngine;
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
public final class FlowViewModel_Factory implements Factory<FlowViewModel> {
  private final Provider<FlowRepository> repositoryProvider;

  private final Provider<AlarmScheduler> alarmSchedulerProvider;

  private final Provider<FlowEngine> flowEngineProvider;

  public FlowViewModel_Factory(Provider<FlowRepository> repositoryProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider, Provider<FlowEngine> flowEngineProvider) {
    this.repositoryProvider = repositoryProvider;
    this.alarmSchedulerProvider = alarmSchedulerProvider;
    this.flowEngineProvider = flowEngineProvider;
  }

  @Override
  public FlowViewModel get() {
    return newInstance(repositoryProvider.get(), alarmSchedulerProvider.get(), flowEngineProvider.get());
  }

  public static FlowViewModel_Factory create(Provider<FlowRepository> repositoryProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider, Provider<FlowEngine> flowEngineProvider) {
    return new FlowViewModel_Factory(repositoryProvider, alarmSchedulerProvider, flowEngineProvider);
  }

  public static FlowViewModel newInstance(FlowRepository repository, AlarmScheduler alarmScheduler,
      FlowEngine flowEngine) {
    return new FlowViewModel(repository, alarmScheduler, flowEngine);
  }
}
