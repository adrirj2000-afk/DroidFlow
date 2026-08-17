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
public final class BluetoothReceiver_MembersInjector implements MembersInjector<BluetoothReceiver> {
  private final Provider<FlowEngine> flowEngineProvider;

  public BluetoothReceiver_MembersInjector(Provider<FlowEngine> flowEngineProvider) {
    this.flowEngineProvider = flowEngineProvider;
  }

  public static MembersInjector<BluetoothReceiver> create(Provider<FlowEngine> flowEngineProvider) {
    return new BluetoothReceiver_MembersInjector(flowEngineProvider);
  }

  @Override
  public void injectMembers(BluetoothReceiver instance) {
    injectFlowEngine(instance, flowEngineProvider.get());
  }

  @InjectedFieldSignature("com.droidflow.domain.engine.BluetoothReceiver.flowEngine")
  public static void injectFlowEngine(BluetoothReceiver instance, FlowEngine flowEngine) {
    instance.flowEngine = flowEngine;
  }
}
