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
public final class AlarmReceiver_MembersInjector implements MembersInjector<AlarmReceiver> {
  private final Provider<FlowEngine> flowEngineProvider;

  public AlarmReceiver_MembersInjector(Provider<FlowEngine> flowEngineProvider) {
    this.flowEngineProvider = flowEngineProvider;
  }

  public static MembersInjector<AlarmReceiver> create(Provider<FlowEngine> flowEngineProvider) {
    return new AlarmReceiver_MembersInjector(flowEngineProvider);
  }

  @Override
  public void injectMembers(AlarmReceiver instance) {
    injectFlowEngine(instance, flowEngineProvider.get());
  }

  @InjectedFieldSignature("com.droidflow.domain.engine.AlarmReceiver.flowEngine")
  public static void injectFlowEngine(AlarmReceiver instance, FlowEngine flowEngine) {
    instance.flowEngine = flowEngine;
  }
}
