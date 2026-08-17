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
public final class BatteryReceiver_MembersInjector implements MembersInjector<BatteryReceiver> {
  private final Provider<FlowEngine> flowEngineProvider;

  public BatteryReceiver_MembersInjector(Provider<FlowEngine> flowEngineProvider) {
    this.flowEngineProvider = flowEngineProvider;
  }

  public static MembersInjector<BatteryReceiver> create(Provider<FlowEngine> flowEngineProvider) {
    return new BatteryReceiver_MembersInjector(flowEngineProvider);
  }

  @Override
  public void injectMembers(BatteryReceiver instance) {
    injectFlowEngine(instance, flowEngineProvider.get());
  }

  @InjectedFieldSignature("com.droidflow.domain.engine.BatteryReceiver.flowEngine")
  public static void injectFlowEngine(BatteryReceiver instance, FlowEngine flowEngine) {
    instance.flowEngine = flowEngine;
  }
}
