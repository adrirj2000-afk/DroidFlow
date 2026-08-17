package com.droidflow.ui.templates;

import com.droidflow.data.local.FlowDao;
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
public final class TemplatesViewModel_Factory implements Factory<TemplatesViewModel> {
  private final Provider<FlowDao> flowDaoProvider;

  public TemplatesViewModel_Factory(Provider<FlowDao> flowDaoProvider) {
    this.flowDaoProvider = flowDaoProvider;
  }

  @Override
  public TemplatesViewModel get() {
    return newInstance(flowDaoProvider.get());
  }

  public static TemplatesViewModel_Factory create(Provider<FlowDao> flowDaoProvider) {
    return new TemplatesViewModel_Factory(flowDaoProvider);
  }

  public static TemplatesViewModel newInstance(FlowDao flowDao) {
    return new TemplatesViewModel(flowDao);
  }
}
