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
public final class SystemStateReceiver_MembersInjector implements MembersInjector<SystemStateReceiver> {
  private final Provider<FlowEngine> flowEngineProvider;

  public SystemStateReceiver_MembersInjector(Provider<FlowEngine> flowEngineProvider) {
    this.flowEngineProvider = flowEngineProvider;
  }

  public static MembersInjector<SystemStateReceiver> create(
      Provider<FlowEngine> flowEngineProvider) {
    return new SystemStateReceiver_MembersInjector(flowEngineProvider);
  }

  @Override
  public void injectMembers(SystemStateReceiver instance) {
    injectFlowEngine(instance, flowEngineProvider.get());
  }

  @InjectedFieldSignature("com.droidflow.domain.engine.SystemStateReceiver.flowEngine")
  public static void injectFlowEngine(SystemStateReceiver instance, FlowEngine flowEngine) {
    instance.flowEngine = flowEngine;
  }
}
