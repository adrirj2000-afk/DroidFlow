package com.droidflow.domain.engine;

import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DroidFlowBackgroundService_MembersInjector implements MembersInjector<DroidFlowBackgroundService> {
  private final Provider<FlowEngine> flowEngineProvider;

  public DroidFlowBackgroundService_MembersInjector(Provider<FlowEngine> flowEngineProvider) {
    this.flowEngineProvider = flowEngineProvider;
  }

  public static MembersInjector<DroidFlowBackgroundService> create(
      Provider<FlowEngine> flowEngineProvider) {
    return new DroidFlowBackgroundService_MembersInjector(flowEngineProvider);
  }

  @Override
  public void injectMembers(DroidFlowBackgroundService instance) {
    injectFlowEngine(instance, flowEngineProvider.get());
  }

  @InjectedFieldSignature("com.droidflow.domain.engine.DroidFlowBackgroundService.flowEngine")
  public static void injectFlowEngine(DroidFlowBackgroundService instance, FlowEngine flowEngine) {
    instance.flowEngine = flowEngine;
  }
}
