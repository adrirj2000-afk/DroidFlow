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
public final class DroidFlowAccessibilityService_MembersInjector implements MembersInjector<DroidFlowAccessibilityService> {
  private final Provider<FlowEngine> flowEngineProvider;

  public DroidFlowAccessibilityService_MembersInjector(Provider<FlowEngine> flowEngineProvider) {
    this.flowEngineProvider = flowEngineProvider;
  }

  public static MembersInjector<DroidFlowAccessibilityService> create(
      Provider<FlowEngine> flowEngineProvider) {
    return new DroidFlowAccessibilityService_MembersInjector(flowEngineProvider);
  }

  @Override
  public void injectMembers(DroidFlowAccessibilityService instance) {
    injectFlowEngine(instance, flowEngineProvider.get());
  }

  @InjectedFieldSignature("com.droidflow.domain.engine.DroidFlowAccessibilityService.flowEngine")
  public static void injectFlowEngine(DroidFlowAccessibilityService instance,
      FlowEngine flowEngine) {
    instance.flowEngine = flowEngine;
  }
}
