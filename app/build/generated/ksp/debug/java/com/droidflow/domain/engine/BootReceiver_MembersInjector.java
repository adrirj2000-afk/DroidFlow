package com.droidflow.domain.engine;

import com.droidflow.data.local.FlowDao;
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
public final class BootReceiver_MembersInjector implements MembersInjector<BootReceiver> {
  private final Provider<FlowDao> flowDaoProvider;

  private final Provider<AlarmScheduler> alarmSchedulerProvider;

  public BootReceiver_MembersInjector(Provider<FlowDao> flowDaoProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider) {
    this.flowDaoProvider = flowDaoProvider;
    this.alarmSchedulerProvider = alarmSchedulerProvider;
  }

  public static MembersInjector<BootReceiver> create(Provider<FlowDao> flowDaoProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider) {
    return new BootReceiver_MembersInjector(flowDaoProvider, alarmSchedulerProvider);
  }

  @Override
  public void injectMembers(BootReceiver instance) {
    injectFlowDao(instance, flowDaoProvider.get());
    injectAlarmScheduler(instance, alarmSchedulerProvider.get());
  }

  @InjectedFieldSignature("com.droidflow.domain.engine.BootReceiver.flowDao")
  public static void injectFlowDao(BootReceiver instance, FlowDao flowDao) {
    instance.flowDao = flowDao;
  }

  @InjectedFieldSignature("com.droidflow.domain.engine.BootReceiver.alarmScheduler")
  public static void injectAlarmScheduler(BootReceiver instance, AlarmScheduler alarmScheduler) {
    instance.alarmScheduler = alarmScheduler;
  }
}
